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
import pe.gob.reniec.padronn.logic.model.Observacion;
import pe.gob.reniec.padronn.logic.model.PadronObservado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 20/09/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportePadronObservadosView extends AbstractPOIExcelView {

    private String pathExcel = "/WEB-INF/formato/Reporte-padron-observados";
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
            nombreArchivo = "Reporte-padron-observados.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Sheet sheet = workbook.getSheet("observaciones");
        if (sheet == null) {
            sheet = workbook.createSheet("observaciones");
        }
        int currentRow = 1;
        int filaCount = 1;
        int column;

        List<PadronObservado> padronTotalDistritos = (List<PadronObservado>) model.get("padronObservados");

        if (padronTotalDistritos != null) {
            for (PadronObservado padronObservado: padronTotalDistritos) {
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
                cell.setCellValue(filaCount);
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getCoPadronNominal());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue((padronObservado.getNuCnv()==null?"":padronObservado.getNuCnv()));
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getNuCui()==null?"":padronObservado.getNuCui());
                column++;
                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getNuDniMenor()==null?"":padronObservado.getNuDniMenor());
                column++;


                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getApPrimerMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getApSegundoMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getPrenombreMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getFeNacMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getFeCreaAudi());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getDeEntidad());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getDeTipoObservacion());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getFeCreaAudi());
                column++;
                filaCount += 1;
                if (currentRow % 100 == 0) {
                    ((SXSSFSheet)sheet).flushRows(100);
                    logger.info(currentRow*100 + " pasadas a disco");
                }
            }

        }
        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);*/
    }


/*    private boolean workbookAlreadyExists;
    public ReportePadronObservadosView() {
        super();
        setUrl("/WEB-INF/formato/Reporte-padron-observados");
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nombreArchivo = (String) model.get("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "Reporte-padron-observados.xls";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("observaciones");
        if (sheet == null) {
            sheet = workbook.createSheet("observaciones");
        }

        int currentRow = 1;
        int filaCount = 1;
        int column;

        List<Observacion> padronTotalDistritos = (List<Observacion>) model.get("padronObservados");

        for (Observacion observacion: padronTotalDistritos) {
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
            cell.setCellValue(filaCount);
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getCoPadronNominal());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getApPrimerMenor());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getApSegundoMenor());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getPrenombreMenor());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getFeNacMenor());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            *//*cell.da*//*
            cell.setCellValue(observacion.getFeCreaAudi());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getDeEntidad());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getDeObservacion());
            column++;

            cell = row.getCell(column);
            if (cell == null) {
                cell = row.createCell(column);
            }
            cell.setCellValue(observacion.getFeCreaAudi());
            column++;

            filaCount += 1;
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

    }*/
}
