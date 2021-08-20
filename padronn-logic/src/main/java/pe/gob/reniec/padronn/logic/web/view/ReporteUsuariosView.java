package pe.gob.reniec.padronn.logic.web.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import pe.gob.reniec.padronn.logic.model.UsuarioExterno;
import pe.gob.reniec.padronn.logic.util.GetCurrentDateTime;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 31/01/14
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */

public class ReporteUsuariosView extends AbstractExcelView {

    Logger logger = Logger.getLogger(getClass());

    public ReporteUsuariosView() {
        super();
        setUrl("/WEB-INF/formato/Reporte-usuarios");
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        logger.info("intentando generar...");
        String nombreArchivo = (String) model.get("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "Reporte-usuarios.xls";
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);
        /*HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);*/

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("usuarios");
        if (sheet == null) {
            sheet = workbook.createSheet("usuarios");
        }

        int currentRow = 0;
        int filaCount = 1;
        int columnaCount = 0;

        List<UsuarioExterno> usuarioExternoList = (List<UsuarioExterno>) model.get("usuarios");

        row = sheet.getRow(currentRow);
        if (row == null) {
            row = sheet.createRow(currentRow);
        }
        currentRow += 2;
        cell = row.getCell(columnaCount);
        if (cell == null) {
            cell = row.createCell(columnaCount);
        }
        cell.setCellValue("REPORTE DE USUARIOS, FECHA DE GENERACION: " + GetCurrentDateTime.getCurrentDateTime());

        for (UsuarioExterno usuarioExterno : usuarioExternoList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            /*cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(filaCount);
            columnaCount++;*/


            String[] valores = {
                    Integer.toString(filaCount), usuarioExterno.getCoUsuario(), usuarioExterno.getApPrimer(),
                    usuarioExterno.getApSegundo(), usuarioExterno.getPrenombres(), usuarioExterno.getDeEmail(),
                    usuarioExterno.getDeGrupo(), usuarioExterno.getNuTelefono(), /*usuarioExterno.getDniAlcalde(),*/
                    usuarioExterno.getDeTipoEntidad(), usuarioExterno.getDeEntidadLarga(), usuarioExterno.getCoUbigeoInei(),
                    usuarioExterno.getDeDepartamento(),usuarioExterno.getDeProvincia(), usuarioExterno.getDeDistrito(),
                    usuarioExterno.getFeLastLogin(), usuarioExterno.getHoraAcceso(), usuarioExterno.getEsUsuario(),
            };
            for (String valor : valores) {
                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(valor);
                columnaCount++;
            }
            columnaCount = 0;
            filaCount += 1;
        }
        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);*/
    }
}
