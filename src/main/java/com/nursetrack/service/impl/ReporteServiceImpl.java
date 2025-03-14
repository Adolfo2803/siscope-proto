package com.nursetrack.service.impl;

import com.nursetrack.model.Ausencia;
import com.nursetrack.repository.AusenciaRepository;
import com.nursetrack.service.ReporteService;

// Importaciones para Excel (Apache POI)
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Importaciones para PDF (iText)
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final AusenciaRepository ausenciaRepository;

    public ReporteServiceImpl(AusenciaRepository ausenciaRepository) {
        this.ausenciaRepository = ausenciaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> generarReporteAusencias(LocalDate fechaInicio, LocalDate fechaFin) {
        return ausenciaRepository.findByRangoDeFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> generarReporteAusenciasPorServicio(Long servicioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return ausenciaRepository.findByServicioAndRangoDeFechas(servicioId, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> generarReporteAusenciasPorTurno(Long turnoId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Ausencia> ausencias = ausenciaRepository.findByRangoDeFechas(fechaInicio, fechaFin);
        return ausencias.stream()
                .filter(a -> a.getEnfermera().getTurno().getId().equals(turnoId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> generarReporteAusenciasPorConcepto(Long conceptoId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Ausencia> ausencias = ausenciaRepository.findByRangoDeFechas(fechaInicio, fechaFin);
        return ausencias.stream()
                .filter(a -> a.getConceptoAusencia().getId().equals(conceptoId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getEstadisticasPorConcepto(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Ausencia> ausencias = ausenciaRepository.findByRangoDeFechas(fechaInicio, fechaFin);
        return ausencias.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getConceptoAusencia().getNombre(),
                        Collectors.counting()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getEstadisticasPorServicio(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Ausencia> ausencias = ausenciaRepository.findByRangoDeFechas(fechaInicio, fechaFin);
        return ausencias.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getEnfermera().getServicio().getNombre(),
                        Collectors.counting()
                ));
    }

    @Override
    public byte[] exportarReporteExcel(List<Ausencia> ausencias) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Ausencias");

            // Crear estilos para encabezados
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"N° Empleado", "Nombre", "Apellido Paterno", "Apellido Materno",
                    "Servicio", "Turno", "Concepto", "Fecha Inicio", "Fecha Fin", "Observaciones"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Formato para fechas
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Llenar datos
            int rowNum = 1;
            for (Ausencia ausencia : ausencias) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ausencia.getEnfermera().getNumeroEmpleado());
                row.createCell(1).setCellValue(ausencia.getEnfermera().getNombre());
                row.createCell(2).setCellValue(ausencia.getEnfermera().getApellidoPaterno());
                row.createCell(3).setCellValue(ausencia.getEnfermera().getApellidoMaterno());
                row.createCell(4).setCellValue(ausencia.getEnfermera().getServicio().getNombre());
                row.createCell(5).setCellValue(ausencia.getEnfermera().getTurno().getNombre());
                row.createCell(6).setCellValue(ausencia.getConceptoAusencia().getNombre());
                row.createCell(7).setCellValue(ausencia.getFechaInicio().format(dateFormatter));
                row.createCell(8).setCellValue(ausencia.getFechaFin().format(dateFormatter));
                row.createCell(9).setCellValue(ausencia.getObservaciones() != null ? ausencia.getObservaciones() : "");
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir el libro a un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    @Override
    public byte[] exportarReportePdf(List<Ausencia> ausencias) {
        try {
            // Crear documento PDF
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Agregar título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Reporte de Ausencias de Personal de Enfermería", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Crear tabla
            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);

            // Definir fuente para encabezados
            Font headerPdfFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);

            // Encabezados
            Stream.of("N° Empleado", "Nombre", "Apellido Paterno", "Apellido Materno",
                            "Servicio", "Turno", "Concepto", "Fecha Inicio", "Fecha Fin", "Observaciones")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.BLUE);
                        header.setBorderWidth(1);
                        header.setPhrase(new Phrase(columnTitle, headerPdfFont));
                        table.addCell(header);
                    });

            // Formato para fechas
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Datos
            for (Ausencia ausencia : ausencias) {
                table.addCell(ausencia.getEnfermera().getNumeroEmpleado());
                table.addCell(ausencia.getEnfermera().getNombre());
                table.addCell(ausencia.getEnfermera().getApellidoPaterno());
                table.addCell(ausencia.getEnfermera().getApellidoMaterno());
                table.addCell(ausencia.getEnfermera().getServicio().getNombre());
                table.addCell(ausencia.getEnfermera().getTurno().getNombre());
                table.addCell(ausencia.getConceptoAusencia().getNombre());
                table.addCell(ausencia.getFechaInicio().format(dateFormatter));
                table.addCell(ausencia.getFechaFin().format(dateFormatter));
                table.addCell(ausencia.getObservaciones() != null ? ausencia.getObservaciones() : "");
            }

            document.add(table);
            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el archivo PDF", e);
        }
    }
}