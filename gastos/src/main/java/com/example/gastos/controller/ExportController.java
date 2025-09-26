package com.example.gastos.controller;

import com.example.gastos.model.Gasto;
import com.example.gastos.repository.GastoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Stream;

@Controller
public class ExportController {

    @Autowired
    private GastoRepository gastoRepository;

    // PDF
    @GetMapping("/historial/exportar")
    public void exportarPDF(HttpServletResponse response,
                            Integer mes, Integer anio, String tipo) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=gastos.pdf");

        List<Gasto> gastos = gastoRepository.findByMesAnioTipo(mes, anio, tipo);

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Historial de Gastos", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // espacio

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 2, 2});

        // Encabezados
        Stream.of("Fecha", "Monto (S/.)", "Tipo").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(headerTitle));
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        });

        // Filas
        for (Gasto g : gastos) {
            table.addCell(g.getFecha().toString());
            table.addCell(String.format("%.2f", g.getMonto()));
            table.addCell(g.getTipo());
        }

        document.add(table);
        document.close();
    }

    // Excel
    @GetMapping("/historial/exportarExcel")
    public void exportarExcel(HttpServletResponse response,
                              Integer mes, Integer anio, String tipo) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=gastos.xlsx");

        List<Gasto> gastos = gastoRepository.findByMesAnioTipo(mes, anio, tipo);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Gastos");

        // Encabezado
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Fecha");
        header.createCell(1).setCellValue("Monto (S/.)");
        header.createCell(2).setCellValue("Tipo");

        // Filas
        int rowIdx = 1;
        for (Gasto g : gastos) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(g.getFecha().toString());
            row.createCell(1).setCellValue(g.getMonto());
            row.createCell(2).setCellValue(g.getTipo());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
