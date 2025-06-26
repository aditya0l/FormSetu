package com.example.formsetu.util

import android.content.Context
import android.os.Environment
import com.example.formsetu.data.model.FormField
import com.example.formsetu.data.model.FormSchema
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object PDFExporter {

    fun exportFormToPdf(
        context: Context,
        schema: FormSchema,
        responses: Map<String, String>,
        language: String = "hi"
    ): File {
        // Define output file
        val outputDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "FormSetu")
        outputDir.mkdirs()

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(outputDir, "Form_${timestamp}.pdf")

        // Setup PDF writer
        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        // Title
        val titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
        val title = schema.title[language] ?: "Form"
        document.add(Paragraph(title).setFont(titleFont).setFontSize(20f).setTextAlignment(
            TextAlignment.CENTER).setMarginBottom(20f))

        // Fields
        val bodyFont = PdfFontFactory.createFont(StandardFonts.HELVETICA)
        for (field in schema.fields) {
            val label = field.label[language] ?: field.key
            val value = responses[field.key] ?: ""

            val fieldParagraph = Paragraph()
                .add(Text("$label: ").setFont(bodyFont).setBold())
                .add(Text(value).setFont(bodyFont))
                .setFontSize(12f)
                .setMarginBottom(10f)

            document.add(fieldParagraph)
        }

        document.close()
        return file
    }
}
