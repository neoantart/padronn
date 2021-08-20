package pe.gob.reniec.padronn.logic.web.view;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import pe.gob.reniec.padronn.logic.model.PadronMovimiento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 19/09/13
 * Time: 05:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportePadronMovimientoView extends AbstractPOIExcelView {


    private String pathExcel = "/WEB-INF/formato/Reporte-padron-movimientos";
    Logger logger = Logger.getLogger(getClass());

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
            nombreArchivo = "Reporte-padron-movimientos.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Sheet sheet = workbook.getSheet("movimientos");
        if (sheet == null) {
            sheet = workbook.createSheet("movimientos");
        }
        int currentRow = 2;
        int filaCount = 1;
        int columnCount = 0;

        List<PadronMovimiento> padronMovimientoList = (List<PadronMovimiento>) model.get("padronMovimientoList");

        if (padronMovimientoList != null) {
            for (PadronMovimiento padronMovimiento : padronMovimientoList) {
                row = sheet.getRow(currentRow);
                if (row == null) {
                    row = sheet.createRow(currentRow);
                }
                currentRow += 1;

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(filaCount);

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getCoPadronNominal());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                cell.setCellValue((padronMovimiento.getNuCnv()==null?"":padronMovimiento.getNuCnv()));
                columnCount++;

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                cell.setCellValue(padronMovimiento.getNuCui()==null?"":padronMovimiento.getNuCui());
                columnCount++;
                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                cell.setCellValue(padronMovimiento.getNuDniMenor()==null?"":padronMovimiento.getNuDniMenor());
                columnCount++;


                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;

                cell.setCellValue(padronMovimiento.getApPrimerMenor());


                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getApSegundoMenor());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getPrenombreMenor());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getFeNacMenor());


                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getCoUbigeoIneiAnt());


                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getCoCentroPobladoAnt());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeDepartamentoAnt());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeProvinciaAnt());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeDistritoAnt());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeCentroPobladoAnt());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getFeCreaAudi());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getCoUbigeoIneiAct());



                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getCoCentroPobladoAct());


                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeDepartamentoAct());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeProvinciaAct());


                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeDistritoAct());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getDeCentroPobladoAct());

                cell = row.getCell(columnCount);
                if (cell == null) {
                    cell = row.createCell(columnCount);
                }
                columnCount++;
                cell.setCellValue(padronMovimiento.getFeCreaAudi());

                columnCount = 0;
                filaCount += 1;

                if (currentRow % 100 == 0) {
                    ((SXSSFSheet)sheet).flushRows(100);
                    logger.info(currentRow*100 + " pasadas a disco");
                }
            }
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
}