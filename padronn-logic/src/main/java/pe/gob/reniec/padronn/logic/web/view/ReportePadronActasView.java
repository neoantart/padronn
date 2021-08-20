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
import org.springframework.web.servlet.view.document.AbstractExcelView;
import pe.gob.reniec.padronn.logic.model.Acta;
import pe.gob.reniec.padronn.logic.model.PadronMovimiento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * User: jronal at gmail dot com
 * Date: 17/10/13
 * Time: 04:53 PM
 */

public class ReportePadronActasView  extends AbstractPOIExcelView {

    private String pathExcel = "/WEB-INF/formato/Actas_Municipios_Cumplieron";
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

        Sheet sheet = workbook.getSheet("Actas_Municipios");
        if (sheet == null) {
            sheet = workbook.createSheet("Actas_Municipios");
        }

        int currentRow = 2;
        int filaCount = 1;
        int columnaCount = 0;


        List<Acta> actaList = (List<Acta>) model.get("listActas");
        for (Acta acta: actaList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(filaCount);
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getDeEntidad());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getDeDepartamento());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getDeProvincia());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getDeDistrito());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getFeIni());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getFeFin());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getFeCreaAudi());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getUsCreaAudi());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getApPrimer());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getApSegundo());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getPrenombres());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(acta.getNuArchivos());

            columnaCount=0;

            filaCount += 1;

            if (currentRow % 100 == 0) {
                ((SXSSFSheet)sheet).flushRows(100);
                logger.info(currentRow*100 + " pasadas a disco");
            }
        }

        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);*/
    }
}
