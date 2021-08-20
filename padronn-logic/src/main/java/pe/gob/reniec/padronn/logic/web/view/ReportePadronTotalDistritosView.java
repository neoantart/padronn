package pe.gob.reniec.padronn.logic.web.view;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import pe.gob.reniec.padronn.logic.model.Ubigeo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.apache.taglibs.standard.tag.common.core.Util.getStyle;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 20/09/13
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */

public class ReportePadronTotalDistritosView extends AbstractExcelView {

    private boolean workbookAlreadyExists;

    public ReportePadronTotalDistritosView() {
        super();
        setUrl("/WEB-INF/formato/Reporte-padron-total-distritos");
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nombreArchivo = (String) model.get("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "Reporte-padron-total-distritos.xls";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("total_distritos");
        if (sheet == null) {
            sheet = workbook.createSheet("total_distritos");
        }

        //HSSFRow headerRow = sheet.createRow(0);
        int currentRow = 1;
        int filaCount = 1;

        List<Ubigeo> padronTotalDistritos = (List<Ubigeo>) model.get("padronTotalDistritos");

        int total=0;
        int totalSinDni=0;
        int totalConDni=0;
        int totalCui = 0;
        for (Ubigeo ubigeo: padronTotalDistritos) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(filaCount);

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(ubigeo.getCoUbigeoInei());

            cell = row.getCell(2);
            if (cell == null) {
                cell = row.createCell(2);
            }
            cell.setCellValue(ubigeo.getDeDepartamento());

            cell = row.getCell(3);
            if (cell == null) {
                cell = row.createCell(3);
            }
            cell.setCellValue(ubigeo.getDeProvincia());

            cell = row.getCell(4);
            if (cell == null) {
                cell = row.createCell(4);
            }
            cell.setCellValue(ubigeo.getDeDistrito());

            cell = row.getCell(5);
            if (cell == null) {
                cell = row.createCell(5);
              }
            cell.setCellValue(Integer.parseInt(ubigeo.getNuCui()==null?"0":ubigeo.getNuCui()));

            cell = row.getCell(6);
            if (cell == null) {
                cell = row.createCell(6);
            }
            cell.setCellValue(Integer.parseInt(ubigeo.getNuSinDni()==null?"0":ubigeo.getNuSinDni()));

            cell = row.getCell(7);
            if (cell == null) {
                cell = row.createCell(7);

            }
            cell.setCellValue(Integer.parseInt(ubigeo.getNuConDni()==null?"0":ubigeo.getNuConDni()));

            cell = row.getCell(8);
            if (cell == null) {
                cell = row.createCell(8);
            }
            cell.setCellValue(Integer.parseInt(ubigeo.getNuTotal()==null?"0":ubigeo.getNuTotal()));

            filaCount += 1;

            totalSinDni += Integer.parseInt(ubigeo.getNuSinDni() == null ? "0" : ubigeo.getNuSinDni());
            totalConDni += Integer.parseInt(ubigeo.getNuConDni() == null ? "0" : ubigeo.getNuConDni());
            totalCui += Integer.parseInt(ubigeo.getNuCui() == null ? "0" : ubigeo.getNuCui());
            total += Integer.parseInt(ubigeo.getNuTotal() == null ? "0" : ubigeo.getNuTotal());
        }

        row = sheet.getRow(currentRow);
        if (row == null) {
            row = sheet.createRow(currentRow);
        }

        cell = row.getCell(4);
        if (cell == null) {
            cell = row.createCell(4);
        }
        cell.setCellValue("Total");

        row = sheet.getRow(currentRow);
        cell = row.getCell(5);
        if (cell == null) {
            cell = row.createCell(5);
        }
        cell.setCellValue(totalCui);

        row = sheet.getRow(currentRow);
        cell = row.getCell(6);
        if (cell == null) {
            cell = row.createCell(6);
        }
        cell.setCellValue(totalSinDni);

        row = sheet.getRow(currentRow);
        cell = row.getCell(7);
        if (cell == null) {
            cell = row.createCell(7);
        }
        cell.setCellValue(totalConDni);

        row = sheet.getRow(currentRow);
        cell = row.getCell(8);
        if (cell == null) {
            cell = row.createCell(8);
        }
        cell.setCellValue(total);


        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);*/

    }
}
