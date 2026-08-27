package mx.edu.sgu.document.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.client.AcademicServiceClient;
import mx.edu.sgu.document.client.KardexDto;
import mx.edu.sgu.document.exception.BusinessRuleException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KardexPdfService {

    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 16;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final AcademicServiceClient academicServiceClient;

    public byte[] generate(UUID studentId, String authorizationHeader) {
        KardexDto kardex = academicServiceClient.fetchKardex(studentId, authorizationHeader);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font headerFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            PDPageContentStream content = new PDPageContentStream(document, page);
            float y = page.getMediaBox().getHeight() - MARGIN;

            y = writeLine(content, titleFont, 16, MARGIN, y, "Sistema de Gestión Universitaria — Kardex Académico");
            y -= 6;
            y = writeLine(content, bodyFont, 9, MARGIN, y,
                    "Generado: " + java.time.OffsetDateTime.now().format(TIMESTAMP_FORMAT));
            y = writeLine(content, bodyFont, 9, MARGIN, y, "Matrícula/ID de alumno: " + kardex.studentId());
            y -= 10;

            y = writeLine(content, headerFont, 11, MARGIN, y,
                    "Créditos aprobados: " + kardex.totalCreditsApproved() + " / " + kardex.totalCreditsInCareer());
            y -= 14;

            // Encabezado de la tabla
            y = writeLine(content, headerFont, 9, MARGIN, y,
                    pad("Periodo", 12) + pad("Código", 10) + pad("Materia", 30) + pad("Créd.", 6) + pad("Calif.", 7) + "Estatus");
            y -= 4;
            content.setLineWidth(0.5f);
            content.moveTo(MARGIN, y);
            content.lineTo(page.getMediaBox().getWidth() - MARGIN, y);
            content.stroke();
            y -= LINE_HEIGHT;

            List<KardexDto.KardexEntryDto> entries = kardex.entries() == null ? List.of() : kardex.entries();
            PDPageContentStream currentContent = content;
            PDPage currentPage = page;

            for (KardexDto.KardexEntryDto entry : entries) {
                if (y < MARGIN + LINE_HEIGHT) {
                    currentContent.close();
                    currentPage = new PDPage(PDRectangle.LETTER);
                    document.addPage(currentPage);
                    currentContent = new PDPageContentStream(document, currentPage);
                    y = currentPage.getMediaBox().getHeight() - MARGIN;
                }

                String grade = entry.finalGrade() == null ? "—" : entry.finalGrade().toPlainString();
                String row = pad(truncate(entry.periodName(), 11), 12)
                        + pad(truncate(entry.subjectCode(), 9), 10)
                        + pad(truncate(entry.subjectName(), 29), 30)
                        + pad(String.valueOf(entry.credits()), 6)
                        + pad(grade, 7)
                        + entry.status();

                y = writeLine(currentContent, bodyFont, 8.5f, MARGIN, y, row);
            }

            currentContent.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new BusinessRuleException("No se pudo generar el PDF del kardex: " + e.getMessage());
        }
    }

    private float writeLine(PDPageContentStream content, PDType1Font font, float size, float x, float y, String text) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(sanitize(text));
        content.endText();
        return y - LINE_HEIGHT;
    }

    /** PDFBox con fuentes estándar solo soporta WinAnsiEncoding: se remueven caracteres fuera de ese rango. */
    private String sanitize(String text) {
        if (text == null) return "";
        return text.replaceAll("[^\\x20-\\x7EñÑáéíóúÁÉÍÓÚ]", "?");
    }

    private String pad(String value, int width) {
        String v = value == null ? "" : value;
        if (v.length() >= width) return v.substring(0, width);
        return v + " ".repeat(width - v.length());
    }

    private String truncate(String value, int maxLength) {
        if (value == null) return "";
        return value.length() <= maxLength ? value : value.substring(0, maxLength - 1) + "…";
    }
}
