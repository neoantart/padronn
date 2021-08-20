package pe.gob.reniec.padronn.logic.web.view;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
import pe.gob.reniec.padronn.logic.model.Ubigeo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jfloresh on 27/05/2014.
 */
public class ReportePadronTiSeguroMidisView extends AbstractPOIExcelView  {

    private String pathExcel = "/WEB-INF/formato/Reporte-padron-ti-seguro-midis";
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
            nombreArchivo = "Reporte-padron.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Sheet sheet = workbook.getSheet("reporte");
        if (sheet == null) {
            sheet = workbook.createSheet("reporte");
        }

        //HSSFRow headerRow = sheet.createRow(0);
        int currentRow = 1;
        int filaCount = 1;
        int columnaCount = 0;

        List<Ubigeo> list = (List<Ubigeo>) model.get("list");

        for (Ubigeo ubigeo: list) {

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
            cell.setCellValue(ubigeo.getCoUbigeoInei());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getDeDepartamento() + "                         ");
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getDeProvincia());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getDeDistrito());
            columnaCount++;



            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getNuSis());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getNuEssalud());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getNuPrivado());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(ubigeo.getNuNinguno());
            columnaCount++;


            columnaCount = 0;
            filaCount += 1;

            if (currentRow % 100 == 0) {
                ((SXSSFSheet)sheet).flushRows(100);
            }
        }
        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);*/
    }
}