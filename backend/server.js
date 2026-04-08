const express = require('express');
const mysql = require('mysql2');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const cors = require('cors');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());

const db = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME
});

db.connect((err) => {
    if (err) {
        console.error('Database connection failed:', err);
        return;
    }
    console.log('Connected to MySQL database');
});

app.post('/register', async (req, res) => {
    const { name, email, password } = req.body;

    if (!name || !email || !password) {
        return res.status(400).json({ error: 'All fields are required' });
    }

    try {
        const hashedPassword = await bcrypt.hash(password, 10);

        const query = 'INSERT INTO users (name, email, password) VALUES (?, ?, ?)';
        db.query(query, [name, email, hashedPassword], (err, result) => {
            if (err) {
                if (err.code === 'ER_DUP_ENTRY') {
                    return res.status(409).json({ error: 'Email already registered' });
                }
                return res.status(500).json({ error: 'Registration failed' });
            }
            res.status(201).json({ message: 'User registered successfully' });
        });
    } catch (err) {
        res.status(500).json({ error: 'Server error' });
    }
});

app.post('/login', async (req, res) => {
    const { email, password } = req.body;

    if (!email || !password) {
        return res.status(400).json({ error: 'Email and password are required' });
    }

    const query = 'SELECT * FROM users WHERE email = ?';
    db.query(query, [email], async (err, results) => {
        if (err) {
            return res.status(500).json({ error: 'Server error' });
        }

        if (results.length === 0) {
            return res.status(401).json({ error: 'Invalid email or password' });
        }

        const user = results[0];
        const passwordMatch = await bcrypt.compare(password, user.password);

        if (!passwordMatch) {
            return res.status(401).json({ error: 'Invalid email or password' });
        }

        const token = jwt.sign(
            { id: user.id, email: user.email, name: user.name },
            process.env.JWT_SECRET,
            { expiresIn: '24h' }
        );

        res.json({
            message: 'Login successful',
            token: token,
            user: {
                id: user.id,
                name: user.name,
                email: user.email
            }
        });
    });
});

app.delete('/users/:id', (req, res) => {
    const { id } = req.params;
    
    const query = 'DELETE FROM users WHERE id = ?';
    db.query(query, [id], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to delete account' });
        }
        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'User not found' });
        }
        res.json({ message: 'Account deleted successfully' });
    });
});

// Get all properties for a user
app.get('/users/:userId/properties', (req, res) => {
    const { userId } = req.params;
 
    const query = 'SELECT * FROM properties WHERE user_id = ?';
    db.query(query, [userId], (err, results) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to fetch properties' });
        }
        // Map snake_case DB columns to camelCase for the Android app
        const properties = results.map(row => ({
            id:           row.id,
            userId:       row.user_id,
            name:         row.name,
            addressLine1: row.address_line1,
            addressLine2: row.address_line2 || '',
            eircode:      row.eircode
        }));
        res.json({ properties });
    });
});
 
// Add a new property
app.post('/properties', (req, res) => {
    const { userId, name, addressLine1, addressLine2, eircode } = req.body;
 
    if (!userId || !name || !addressLine1 || !eircode) {
        return res.status(400).json({ error: 'userId, name, addressLine1 and eircode are required' });
    }
 
    const query = `
        INSERT INTO properties (user_id, name, address_line1, address_line2, eircode)
        VALUES (?, ?, ?, ?, ?)
    `;
    db.query(query, [userId, name, addressLine1, addressLine2 || '', eircode], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to save property' });
        }
        // Return the full property object including the new DB-generated id
        res.status(201).json({
            message: 'Property saved successfully',
            property: {
                id:           result.insertId,
                userId:       userId,
                name:         name,
                addressLine1: addressLine1,
                addressLine2: addressLine2 || '',
                eircode:      eircode
            }
        });
    });
});
 
// Delete a property
app.delete('/properties/:propertyId', (req, res) => {
    const { propertyId } = req.params;
 
    const query = 'DELETE FROM properties WHERE id = ?';
    db.query(query, [propertyId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to delete property' });
        }
        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Property not found' });
        }
        res.json({ message: 'Property deleted successfully' });
    });
});

// Update a property
app.put('/properties/:propertyId', (req, res) => {
    const { propertyId } = req.params;
    const { name, addressLine1, addressLine2, eircode } = req.body;

    if (!name || !addressLine1 || !eircode) {
        return res.status(400).json({ error: 'name, addressLine1 and eircode are required' });
    }

    const query = `
        UPDATE properties
        SET name = ?, address_line1 = ?, address_line2 = ?, eircode = ?
        WHERE id = ?
    `;
    db.query(query, [name, addressLine1, addressLine2 || '', eircode, propertyId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to update property' });
        }
        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Property not found' });
        }
        res.json({
            message: 'Property updated successfully',
            property: {
                id:           parseInt(propertyId),
                name:         name,
                addressLine1: addressLine1,
                addressLine2: addressLine2 || '',
                eircode:      eircode
            }
        });
    });
});

app.get('/health', (req, res) => {
    res.json({ status: 'API is running' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
