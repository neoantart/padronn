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

public class ReportePadronEdadGriasView extends AbstractPOIExcelView  {

    private String pathExcel = "/WEB-INF/formato/Reporte-padron-edad-grias";
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
            cell.setCellValue(padronNominal.getCoEstSaludAds());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeEstSaludAds());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoEstSalud());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeEstSalud());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            String coEstSaludNac = "";
            if(padronNominal.getCoEstSaludNac()!=null)
                coEstSaludNac = padronNominal.getCoEstSaludNac();
            cell.setCellValue(coEstSaludNac);
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            String deEstSaludNac = "";
            if(padronNominal.getDeEstSaludNac()!=null)
                deEstSaludNac = padronNominal.getDeEstSaludNac();
            cell.setCellValue(deEstSaludNac);
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            String nuCnv ="";
            if(padronNominal.getNuCnv()!=null)
                nuCnv =padronNominal.getNuCnv();
            cell.setCellValue(nuCnv);
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
            cell.setCellValue(padronNominal.getEdadEscrita());
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
            cell.setCellValue(padronNominal.getCoProgramasSociales());
            columnaCount++;

            // todo tipo de institucion educativa
            /*cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoProgramasSociales());
            columnaCount++;*/

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoInstEducativa());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeInstEducativa());
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

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoGraInstMadre());
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


            /*cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeGraInstMadre());
            columnaCount++;*/


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoLenMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getEsPadron());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeCreaRegistro());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeModiRegistro());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getUsCreaRegistro());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getUsModiRegistro());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeFuentePrecarga()!=null ? padronNominal.getDeFuentePrecarga():"");
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeTipoRegistro()!=null ? padronNominal.getDeTipoRegistro():"");
            columnaCount++;

            columnaCount = 0;
            filaCount += 1;

            if (currentRow % 100 == 0) {
                ((SXSSFSheet)sheet).flushRows(100);
                logger.info(currentRow*100 + " pasadas a disco");
            }

            /*
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
            cell.setCellValue(padronNominal.getEdadEscrita());
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
            cell.setCellValue(padronNominal.getNuDniMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getNuCui());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeDireccion());
            columnaCount++;

//                logger.debug("columnaCount:"+columnaCount);
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
            cell.setCellValue(padronNominal.getDeDepartamento());
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
            String coCentroPoblado = "";
            if( padronNominal.getCoCentroPoblado() != null )
                coCentroPoblado = padronNominal.getCoCentroPoblado();

            cell.setCellValue(coCentroPoblado);
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            String deCentroPoblado = "";
            if(padronNominal.getDeCentroPoblado()!= null)
                deCentroPoblado = padronNominal.getDeCentroPoblado();
            cell.setCellValue(deCentroPoblado);
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoEstSalud());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeEstSalud());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoInstEducativa());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeInstEducativa());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getTiSeguroMenor());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoProgramasSociales());
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

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoGraInstMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeGraInstMadre());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getCoLenMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getDeLenMadre());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeCreaRegistro());
            columnaCount++;


            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getFeModiRegistro());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getUsCreaRegistro());
            columnaCount++;

            cell = row.getCell(columnaCount);
            if (cell == null) {
                cell = row.createCell(columnaCount);
            }
            cell.setCellValue(padronNominal.getUsModiRegistro());

            columnaCount = 0;
            filaCount += 1;

            if (currentRow % 100 == 0) {
                ((SXSSFSheet)sheet).flushRows(100);
                logger.info(currentRow*100 + " pasadas a disco");
            }
*/

        }
        /*sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(6);*/
    }



        /*public ReportePadronEdadView() {
            super();
            setUrl("/WEB-INF/formato/Reporte-padron-edad");
        }

        protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
                throws Exception {

            String nombreArchivo = (String) model.get("nombreArchivo");
            if (nombreArchivo == null || nombreArchivo.isEmpty()) {
                nombreArchivo = "Reporte-padron-edad.xls";
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            headerStyle.setFont(font);

            HSSFRow row;
            HSSFCell cell;

            HSSFSheet sheet = workbook.getSheet("Padron_Nominal");
            if (sheet == null) {
                sheet = workbook.createSheet("Padron_Nominal");
            }

            //HSSFRow headerRow = sheet.createRow(0);
            int currentRow = 1;
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
                cell.setCellValue(padronNominal.getEdadEscrita());
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
                cell.setCellValue(padronNominal.getNuDniMenor());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getNuCui());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getDeDireccion());
                columnaCount++;

//                logger.debug("columnaCount:"+columnaCount);
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
                cell.setCellValue(padronNominal.getDeDepartamento());
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
                String coCentroPoblado = "";
                if( padronNominal.getCoCentroPoblado() != null )
                    coCentroPoblado = padronNominal.getCoCentroPoblado();

                cell.setCellValue(coCentroPoblado);
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                String deCentroPoblado = "";
                if(padronNominal.getDeCentroPoblado()!= null)
                    deCentroPoblado = padronNominal.getDeCentroPoblado();
                cell.setCellValue(deCentroPoblado);
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getCoEstSalud());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getDeEstSalud());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getCoInstEducativa());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getDeInstEducativa());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getTiSeguroMenor());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getCoProgramasSociales());
                columnaCount++;

                *//*List<String> programasSociales = new ArrayList<String>();
                //String programasSociales = "";
                for(Dominio dominio: padronNominal.getPadronProgramaList()){
                    programasSociales.add(dominio.getCoDominio());
                }
                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(programasSociales.toString().replace("[", "").replace("]", ""));
                columnaCount++;*//*

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

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getCoGraInstMadre());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getDeGraInstMadre());
                columnaCount++;


                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getCoLenMadre());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getDeLenMadre());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getFeCreaRegistro());
                columnaCount++;


                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getFeModiRegistro());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getUsCreaRegistro());
                columnaCount++;

                cell = row.getCell(columnaCount);
                if (cell == null) {
                    cell = row.createCell(columnaCount);
                }
                cell.setCellValue(padronNominal.getUsModiRegistro());

                columnaCount = 0;
                filaCount += 1;
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
        }
*/
}
