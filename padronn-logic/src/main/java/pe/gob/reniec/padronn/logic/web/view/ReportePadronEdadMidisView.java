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
import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jfloresh on 27/05/2014.
 */
public class ReportePadronEdadMidisView extends AbstractPOIExcelView  {

    private String pathExcel = "/WEB-INF/formato/Reporte-padron-edad-midis";
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

        Sheet sheet = workbook.getSheet("Padron_Nominal");
        if (sheet == null) {
            sheet = workbook.createSheet("Padron_Nominal");
        }

        //HSSFRow headerRow = sheet.createRow(0);
        int currentRow = 5;
        int filaCount = 1;
        int columnaCount = 0;

        List<PadronNominal> padronList = (List<PadronNominal>) model.get("padronList");

        for (PadronNominal padronNominal: padronList) {

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
            cell.setCellValue(padronNominal.getDeMenorVisitado());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeFuenteDatos());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeUltimaTomaDatos());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeVisita());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeMenorEncontrado());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeDepartamento() + "                         ");
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeProvincia());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeDistrito());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoUbigeoInei());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            String coCentroPoblado = "";
            if( padronNominal.getCoCentroPoblado() != null )
                coCentroPoblado = padronNominal.getCoCentroPoblado();
            cell.setCellValue(coCentroPoblado);
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeCentroPoblado());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeAreaCcpp());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeVia());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeDireccion());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeRefDir());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getTiDocIdentidad());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoDocumentoIdentidad());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getApPrimerMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getApSegundoMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getPrenombreMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoGeneroMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeNacMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getNuEdad());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getNuEdadMeses());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            /*cell.setCellValue(padronNominal.getTiSeguroMenor());*/
            cell.setCellValue(padronNominal.getCoTipoSeguros());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getNinguno());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getPvl());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getJuntos());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getQaliwarma());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCunamasscd());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCunamassaf());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getTiVinculoJefe());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoDniJefeFam());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getApPrimerJefe());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getApSegundoJefe());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getPrenomJefe());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getTiVinculoMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoDniMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getApPrimerMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getApSegundoMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getPrenomMadre());
            columnaCount++;
/**/
            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getNuCelMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDiCorreoMadre());
            columnaCount++;
  /**/
            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeTipoRegistro()!=null ? padronNominal.getDeTipoRegistro():"");
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getEsPadron()!=null ? padronNominal.getEsPadron():"");
            columnaCount++;

            /*cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoGraInstMadre());
            columnaCount++;*/

            columnaCount = 0;
            filaCount += 1;

            if (currentRow % 100 == 0) {
                ((SXSSFSheet)sheet).flushRows(100);
                logger.info(currentRow*100 + " pasadas a disco");
            }
        }
        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(6);*/
    }
}