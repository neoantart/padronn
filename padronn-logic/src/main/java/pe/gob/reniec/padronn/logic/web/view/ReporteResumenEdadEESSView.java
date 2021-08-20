package pe.gob.reniec.padronn.logic.web.view;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import pe.gob.reniec.padronn.logic.model.PadronEdadEESS;
import pe.gob.reniec.padronn.logic.model.PadronMovimiento;
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


public class ReporteResumenEdadEESSView extends AbstractPOIExcelView {

    private static Logger LOG = Logger.getLogger(ReporteResumenEdadEESSView.class);

    private String pathExcel = "/WEB-INF/formato/Reporte-Resumen_EESS_edad";


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
            nombreArchivo = "resumen_padron_EESS.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        String deTiEstablecimiento = (String) model.get("deTiEstablecimiento");
        ReportePadronResumenEdadEESS reportePadronResumenEdadEESS = (ReportePadronResumenEdadEESS) model.get("reportePadronResumenEdadEESS");

        Sheet sheet = workbook.getSheet("RESUMEN_EESS_EDAD");
        if (sheet == null) {
            sheet = workbook.createSheet("RESUMEN_EESS_EDAD");
        }

        //HSSFRow headerRow = sheet.createRow(0);
        int currentRow = 4;
        int column;
        List<PadronEdadEESS> padronEdadEESSList = (List<PadronEdadEESS>) model.get("padronEdadEESSList");
//        List<PadronObservado> padronObservados = (List<PadronObservado>) model.get("padronObservados");
        if (padronEdadEESSList != null) {
            for (PadronEdadEESS padronEdadEESS : padronEdadEESSList) {
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
                cell.setCellValue(padronEdadEESS.getFila());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getCoRenaes());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 1){cell.setCellValue(padronEdadEESS.getDeEstSalud());}
                if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 2){cell.setCellValue(padronEdadEESS.getDeEstSaludNac());}
                if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 3){cell.setCellValue(padronEdadEESS.getDeEstSaludAds());}
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
                cell.setCellValue(padronEdadEESS.getDeRenaesDireccion());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getAnno0());
                column++;


                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getAnno1());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getAnno2());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getAnno3());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getAnno4());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getAnno5());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronEdadEESS.getTotal());
                column++;
            }
        }
        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(3);*/
    }
}
