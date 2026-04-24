package com.example.solutions4u.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.solutions4u.model.UtilityBill
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    // Generates a PDF report for the given period and bills
    // Returns the file path if successful, null if failed
    fun generateReport(
        context: Context,
        period: String,
        bills: List<UtilityBill>
    ): File? {
        return try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            val titlePaint = Paint().apply {
                color = Color.parseColor("#2E7D32")
                textSize = 28f
                isFakeBoldText = true
            }
            val headingPaint = Paint().apply {
                color = Color.BLACK
                textSize = 18f
                isFakeBoldText = true
            }
            val bodyPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 14f
            }
            val smallPaint = Paint().apply {
                color = Color.GRAY
                textSize = 12f
            }
            val linePaint = Paint().apply {
                color = Color.LTGRAY
                strokeWidth = 1f
            }
            val barPaint = Paint().apply {
                color = Color.parseColor("#2E7D32")
            }

            var y = 60f

            // Title
            canvas.drawText("Solutions 4 U", 40f, y, titlePaint)
            y += 30f
            canvas.drawText("Expenditure Report — $period", 40f, y, headingPaint)
            y += 20f

            // Date generated
            val dateStr = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())
            canvas.drawText("Generated: $dateStr", 40f, y, smallPaint)
            y += 30f

            // Divider
            canvas.drawLine(40f, y, 555f, y, linePaint)
            y += 20f

            // Summary
            val total = bills.sumOf { it.amount }
            canvas.drawText("Total for $period: €${"%.2f".format(total)}", 40f, y, headingPaint)
            y += 30f

            // Bar chart section
            canvas.drawText("Spending by Category", 40f, y, headingPaint)
            y += 20f

            val categoryTotals = bills
                .groupBy { it.category }
                .mapValues { (_, items) -> items.sumOf { it.amount } }
                .toList()
                .sortedByDescending { it.second }

            val maxAmount = categoryTotals.maxOfOrNull { it.second } ?: 1.0
            val barColors = listOf(
                Color.parseColor("#16A34A"),
                Color.parseColor("#EF4444"),
                Color.parseColor("#3B82F6"),
                Color.parseColor("#F59E0B"),
                Color.parseColor("#8B5CF6")
            )
            val maxBarWidth = 300f

            categoryTotals.forEachIndexed { index, (category, amount) ->
                val fraction = (amount / maxAmount).toFloat()
                val barWidth = maxBarWidth * fraction

                // Category label
                canvas.drawText(category, 40f, y + 12f, bodyPaint)

                // Bar
                val barPaintColored = Paint().apply {
                    color = barColors[index % barColors.size]
                }
                canvas.drawRect(200f, y, 200f + barWidth, y + 16f, barPaintColored)

                // Amount
                canvas.drawText("€${"%.2f".format(amount)}", 510f, y + 12f, bodyPaint)
                y += 30f
            }

            y += 10f
            canvas.drawLine(40f, y, 555f, y, linePaint)
            y += 20f

            // Bills table header
            canvas.drawText("Bill Details", 40f, y, headingPaint)
            y += 20f

            // Table header row
            val headerPaint = Paint().apply {
                color = Color.BLACK
                textSize = 13f
                isFakeBoldText = true
            }
            canvas.drawText("Category", 40f, y, headerPaint)
            canvas.drawText("Provider", 180f, y, headerPaint)
            canvas.drawText("Amount", 380f, y, headerPaint)
            canvas.drawText("Due Date", 460f, y, headerPaint)
            y += 6f
            canvas.drawLine(40f, y, 555f, y, linePaint)
            y += 16f

            // Table rows
            bills.forEach { bill ->
                canvas.drawText(bill.category, 40f, y, bodyPaint)
                canvas.drawText(bill.provider, 180f, y, bodyPaint)
                canvas.drawText("€${"%.2f".format(bill.amount)}", 380f, y, bodyPaint)
                canvas.drawText(bill.date, 460f, y, bodyPaint)
                y += 22f
            }

            document.finishPage(page)

            // Save to Downloads
            val fileName = "Solutions4U_${period}_${System.currentTimeMillis()}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            document.writeTo(FileOutputStream(file))
            document.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Lists all previously generated reports in Downloads
    fun getExistingReports(): List<File> {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return downloadsDir.listFiles { file ->
            file.name.startsWith("Solutions4U_") && file.name.endsWith(".pdf")
        }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
}