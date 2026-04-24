# Solutions 4 U

**Team Phantom Menace** | Project Name: Solution4forU

## Why This Idea Was Chosen

We decided to build a utility price comparison app because, honestly, everyone's feeling the sting of higher bills these days. Electricity, internet, gas, mobile plans, insurance — you name it, the prices keep climbing. People want to save money, but actually tracking down the best deals is a headache. You end up calling around or digging through random websites, and the whole thing just takes forever.

With this project, we want to cut through that hassle. The app pulls live prices from different providers and puts everything in one spot. No more bouncing between tabs or making endless phone calls. Just clear, side-by-side quotes, so you can spot the best option right away and maybe even save a chunk of change.

We took some inspiration from those big price comparison websites, but we're going mobile-first this time. It just makes sense — everyone manages life on their phone now. Plus, the app isn't just handy for regular folks. There are business angles too, since it connects companies to customers and even other businesses. That gives it some real weight, both technically and commercially.

So, why this idea? Because it actually helps people, fits the times, and solves a problem almost everyone deals with.

---

## Tech Stack

| Component | Tool |
|-----------|------|
| Android App | Kotlin + Jetpack Compose + Material 3 |
| Backend API | Node.js + Express |
| Database | MySQL via XAMPP |
| Testing | Jetpack Compose UI Tests |
| Version Control | Git + GitHub |

---

## Prerequisites

Before running the project install these tools:

- [XAMPP 8.2](https://www.apachefriends.org) — for MySQL database
- [Node.js LTS](https://nodejs.org) — for the backend API
- [Android Studio](https://developer.android.com/studio) — for the Android app and emulator

---

## How To Run

### Step 1 — Set Up the Database

1. Open XAMPP Control Panel
2. Start **Apache** and **MySQL** — both should turn green
3. Open your browser and go to `http://localhost/phpmyadmin`
4. Click **New** on the left sidebar
5. Type `solution4u` as the database name and click **Create**
6. Click on `solution4u` in the left sidebar
7. Click the **SQL** tab
8. Open `backend/database.sql`, copy all contents and paste into the SQL box
9. Click **Go**

### Step 2 — Set Up the Backend

Open a terminal in the `backend` folder and run:

```powershell
npm install

Create a `.env` file in the backend folder with these contents:
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=
DB_NAME=solution4u
JWT_SECRET=solutions4u_secret_key_change_this_later
PORT=3000

Start the backend:

```powershell
node server.js
```

You should see:
Server running on port 3000
Connected to MySQL database

Keep this terminal open while testing.

### Step 3 — Set Up the Android App

Open a terminal in the `Project-Management` folder and run:

```powershell
echo sdk.dir=C:\\Users\\YOUR_USERNAME\\AppData\\Local\\Android\\Sdk > local.properties
```

Replace `YOUR_USERNAME` with your actual Windows username.

Set JAVA_HOME (run this each session):

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
```

### Step 4 — Run the App

Start the Pixel 6 emulator from Android Studio Device Manager, then run:

```powershell
.\gradlew.bat installDebug
adb reverse tcp:3000 tcp:3000
adb shell am start -n com.example.solutions4u/.MainActivity
```

### Step 5 — Run the Tests

```powershell
.\gradlew.bat app:connectedAndroidTest
```

All 73 tests should pass.

---

## Daily Startup Checklist

- [ ] Open XAMPP and start Apache and MySQL (both green)
- [ ] Navigate to backend folder and run `node server.js`
- [ ] Confirm `Server running on port 3000` and `Connected to MySQL database`
- [ ] Start Pixel 6 emulator in Android Studio
- [ ] Wait for emulator to fully boot to home screen
- [ ] Run `.\gradlew.bat installDebug`
- [ ] Run `adb reverse tcp:3000 tcp:3000`

---

## Troubleshooting

| Error | Fix |
|-------|-----|
| Database connection failed | Make sure XAMPP MySQL is running and database is imported |
| SDK location not found | Create `local.properties` file — see Step 3 |
| App fails to install | Wait for emulator to fully boot then try again |
| Emulator offline | Run `adb kill-server` then `adb start-server` |
| Emulator frozen | Device Manager → 3 dots → Wipe Data → Cold Boot Now |
| Running scripts disabled | Run `Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned` |

---

## Tests

73 automated instrumented UI tests written across 11 test files covering all user stories.

Create a `.env` file in the backend folder with these contents:
