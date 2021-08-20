package pe.gob.reniec.padronn.logic.web.view;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import pe.gob.reniec.padronn.logic.model.PadronObservado;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.model.Usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jfloresh on 26/04/2016.
 */
public class BandejaObservadosView extends AbstractPOIExcelView {

    private static Logger LOG = Logger.getLogger(BandejaObservadosView.class);

    private CellStyle cs = null;
    private CellStyle csBold = null;
    private CellStyle csTop = null;
    private CellStyle csRight = null;
    private CellStyle csBottom = null;
    private CellStyle csLeft = null;
    private CellStyle csTopLeft = null;
    private CellStyle csTopRight = null;
    private CellStyle csBottomLeft = null;
    private CellStyle csBottomRight = null;

    @Override
    protected Workbook createWorkbook(HttpServletRequest request) {
        try {
            return new SXSSFWorkbook();
        } catch (Exception e) {
            LOG.error("Error:", e);
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
        String deTipoObservacion = (String) model.get("deTipoObservacion");

        Ubigeo ubigeo = (Ubigeo) model.get("ubigeo");
        Usuario usuario = (Usuario) model.get("usuario");

        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "bandeja_observados.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Sheet sheet = workbook.createSheet(deTipoObservacion);

        int[] withColumns = new int[]{1000, 8000, 8000, 3000, 3500, 8000,8000, 8000, 8000, 10000};
        //Set Column Widths
        for (int i=0; i<withColumns.length; i++) {
            sheet.setColumnWidth(i, withColumns[i]);
        }

        setCellStyles(workbook);

        //  Generacion de encabezado
        int currentRow = 0;
        row = sheet.createRow(currentRow);
        cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 9));
        cell.setCellValue("SISTEMA DE PADRON NOMINAL");
        cell.setCellStyle(csBold);
        currentRow++;
        row = sheet.createRow(currentRow);
        cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 9));
        cell.setCellValue("LISTADO DE REGISTROS OBSERVADOS, TIPO DE OBSERVACION: " + deTipoObservacion);
        cell.setCellStyle(csBold);
        currentRow++;
        Date date = new Date(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        row = sheet.createRow(currentRow);
        cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 9));
        cell.setCellValue("FECHA DE GENERACION DEL REPORTE: " + df.format(date));
        cell.setCellStyle(csBold);
        currentRow++;

        row = sheet.createRow(currentRow);
        String[] headerTable = new String[]{"ITEM", "CODIGO PADRON NOMINAL", "NUMERO DE DNI", "CUI", "NUMERO DE CNV", "PRIMER APELLIDO", "SEGUNDO APELLIDO", "NOMBRES", "FECHA NACIMIENTO", "OBSERVACION"};
        for(int i=0;i<headerTable.length;i++) {
            cell = row.createCell(i);
            cell.setCellValue(headerTable[i]);
            cell.setCellStyle(csBottom);
            cell.setCellStyle(csBold);
        }
        /*cell = row.createCell(0);
        cell.setCellValue("ITEM");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(1);
        cell.setCellValue("CODIGO PADRON NOMINAL");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(2);
        cell.setCellValue("NUMERO DE DNI");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(3);
        cell.setCellValue("CUI");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(4);
        cell.setCellValue("NUMERO DE CNV");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(5);
        cell.setCellValue("PRIMER APELLIDO");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(6);
        cell.setCellValue("SEGUNDO APELLIDO");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(7);
        cell.setCellValue("NOMBRES");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(8);
        cell.setCellValue("FECHA NACIMIENTO");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);

        cell = row.createCell(9);
        cell.setCellValue("OBSERVACION");
        cell.setCellStyle(csBottom);
        cell.setCellStyle(csBold);*/

        int column;
        currentRow++;
        List<PadronObservado> padronObservados = (List<PadronObservado>) model.get("padronObservados");
        if (padronObservados != null) {
            for (PadronObservado padronObservado : padronObservados) {
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
                cell.setCellValue(padronObservado.getNuItem());
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
                cell.setCellValue(padronObservado.getNuDniMenor());
                column++;

                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getNuCui());
                column++;


                cell = row.getCell(column);
                if (cell == null) {
                    cell = row.createCell(column);
                }
                cell.setCellValue(padronObservado.getNuCnv());
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
                cell.setCellValue(padronObservado.getDeTipoObservacion());
                column++;
            }
        }
    }

    private void setCellStyles(Workbook wb) {
        //font size 10
        Font f = wb.createFont();
        f.setFontHeightInPoints((short) 10);

        //Simple style
        cs = wb.createCellStyle();
        cs.setFont(f);

        //Bold Fond
        Font bold = wb.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);

        //Bold style
        csBold = wb.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        csBold.setAlignment((short)2);

        //Setup style for Top Border Line
        csTop = wb.createCellStyle();
        csTop.setBorderTop(CellStyle.BORDER_THIN);
        csTop.setTopBorderColor(IndexedColors.BLACK.getIndex());
        csTop.setFont(f);

        //Setup style for Right Border Line
        csRight = wb.createCellStyle();
        csRight.setBorderRight(CellStyle.BORDER_THIN);
        csRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
        csRight.setFont(f);

        //Setup style for Bottom Border Line
        csBottom = wb.createCellStyle();
        csBottom.setBorderBottom(CellStyle.BORDER_THIN);
        csBottom.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBottom.setFont(f);

        //Setup style for Left Border Line
        csLeft = wb.createCellStyle();
        csLeft.setBorderLeft(CellStyle.BORDER_THIN);
        csLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        csLeft.setFont(f);

        //Setup style for Top/Left corner cell Border Lines
        csTopLeft = wb.createCellStyle();
        csTopLeft.setBorderTop(CellStyle.BORDER_THIN);
        csTopLeft.setTopBorderColor(IndexedColors.BLACK.getIndex());
        csTopLeft.setBorderLeft(CellStyle.BORDER_THIN);
        csTopLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        csTopLeft.setFont(f);

        //Setup style for Top/Right corner cell Border Lines
        csTopRight = wb.createCellStyle();
        csTopRight.setBorderTop(CellStyle.BORDER_THIN);
        csTopRight.setTopBorderColor(IndexedColors.BLACK.getIndex());
        csTopRight.setBorderRight(CellStyle.BORDER_THIN);
        csTopRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
        csTopRight.setFont(f);

        //Setup style for Bottom/Left corner cell Border Lines
        csBottomLeft = wb.createCellStyle();
        csBottomLeft.setBorderBottom(CellStyle.BORDER_THIN);
        csBottomLeft.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBottomLeft.setBorderLeft(CellStyle.BORDER_THIN);
        csBottomLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        csBottomLeft.setFont(f);

        //Setup style for Bottom/Right corner cell Border Lines
        csBottomRight = wb.createCellStyle();
        csBottomRight.setBorderBottom(CellStyle.BORDER_THIN);
        csBottomRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBottomRight.setBorderRight(CellStyle.BORDER_THIN);
        csBottomRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
        csBottomRight.setFont(f);
    }
}