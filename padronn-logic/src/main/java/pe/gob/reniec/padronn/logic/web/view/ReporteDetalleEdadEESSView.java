package pe.gob.reniec.padronn.logic.web.view;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronResumenEdadEESS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReporteDetalleEdadEESSView extends AbstractPOIExcelView {

    private static Logger LOG = Logger.getLogger(ReporteResumenEdadEESSView.class);
    private String pathExcel = "/WEB-INF/formato/Reporte-Detalle_EESS_edad";

    @Override
    protected Workbook createWorkbook(HttpServletRequest request) {
        LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
        Locale userLocale = RequestContextUtils.getLocale(request);
        Resource inputFile = helper.findLocalizedResource(pathExcel, ".xlsx", userLocale);
        try {
            return new SXSSFWorkbook(new XSSFWorkbook(inputFile.getInputStream()), -1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        XSSFFont font = (XSSFFont) workbook.createFont();

        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        Row row;
        Cell cell;

        String nombreArchivo = (String) model.get("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "detalle_padron_EESS.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        String deTiEstablecimiento = (String) model.get("deTiEstablecimiento");
        ReportePadronResumenEdadEESS reportePadronResumenEdadEESS = (ReportePadronResumenEdadEESS) model.get("reportePadronResumenEdadEESS");


        Sheet sheet = workbook.getSheet("DETALLE_EESS_EDAD");
        if (sheet == null) {
            sheet = workbook.createSheet("DETALLE_EESS_EDAD");
        }

        //HSSFRow headerRow = sheet.createRow(0);
        int currentRow = 4;

        //Asignación de valores al cuerpo
        int column;
        List<PadronNominal> padronNominalList = (List<PadronNominal>) model.get("padronNominalList");
        if (padronNominalList != null) {
            for (PadronNominal padronNominal: padronNominalList) {
                column = 0;
                row = sheet.getRow(currentRow);
                if (row == null) {
                    row = sheet.createRow(currentRow);
                }
                currentRow += 1;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getFila());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                /*cell.setCellValue(padronNominal.getCoRenaes());*/
                cell.setCellValue(padronNominal.getCoEstSalud());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeFrecAtencion());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(reportePadronResumenEdadEESS.getDeEstSalud());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(deTiEstablecimiento);
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getNuDniMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getApPrimerMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getApSegundoMenor());
                column++;


                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getPrenombreMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getEdad());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeMenorVisitado());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeFuenteDatos());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getFeUltimaTomaDatos());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getFeVisita());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeMenorEncontrado());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeUbigeoPad());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeDireccion());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDeRefDir());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getCoDniMadre());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getApPrimerMadre());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getApSegundoMadre());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getPrenomMadre());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getNuCelMadre());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronNominal.getDiCorreoMadre());
                column++;
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(13);
    }

}
