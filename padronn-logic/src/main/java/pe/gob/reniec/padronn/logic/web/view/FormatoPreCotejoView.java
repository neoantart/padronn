package pe.gob.reniec.padronn.logic.web.view;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 07/06/13
 * Time: 06:33 PM
 */
//@Component
//@Scope("prototype") //session http://static.springsource.org/spring/docs/3.0.0.M3/reference/html/ch04s04.html
public class FormatoPreCotejoView
        extends AbstractExcelView {

	/*
    {
		//setUrl("http://lmamani.reniec.gob.pe:8081/padronn/resources-1.0/formato/Formato-con-export");
		//String url = getClass().getResource("/formato/Formato-con-export").toString();
		String url = null;
		try {
			//url = getClass().getResource("/formato/Formato-con-export").toString();
			//url = getServletContext().getResource("/formato/Formato-con-export").toString();
			//url = this.getWebApplicationContext().getResource("/formato/Formato-con-export").toString();
			url = this.getWebApplicationContext().getResource("/formato/Formato-con-export").toString();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		assert url != null;
		setUrl(url.replace("https", "http"));
	}
	*/

    private boolean workbookAlreadyExists;

	/*
	private URL getWorkbookUrl()
			throws IOException {
		*//*
		workbookAlreadyExists = false;

		URL workbookUrl = null;
		//workbookUrl = getClass().getResource(padronProperties.FORM_PATH +usuario.getCoUbigeoInei());
		//workbookUrl = getWebApplicationContext().getResource(padronProperties.FORM_PATH +usuario.getCoUbigeoInei()).getURL();
		//workbookUrl = getServletContext().getResource(padronProperties.FORM_PATH +usuario.getCoUbigeoInei());
		//WebApplicationContext applicationContext = new GenericWebApplicationContext();
		//workbookUrl = applicationContext.getResource(padronProperties.FORM_PATH +usuario.getCoUbigeoInei()).getURL();

		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("/WEB-INF/formato/XLS");
		System.out.println(resource);
		resource = loader.getResource("/WEB-INF/formato/XLS.xls");
		System.out.println(resource);

		//LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
		//Locale userLocale = RequestContextUtils.getLocale(request);
		//Resource inputFile = helper.findLocalizedResource(padronProperties.FORM_PATH +usuario.getCoUbigeoInei(), "xls", null);

		if(workbookUrl==null) {
			workbookUrl = getClass().getResource(padronProperties.FORM_PATH);
		} else {
			workbookAlreadyExists = true;
		}
		return workbookUrl;
		*//*
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource ;
		if(usuario==null) {
			resource = loader.getResource("/WEB-INF/formato/XLS.xls");
		} else {
			resource = loader.getResource("/WEB-INF/formato/XLS"+usuario.getCoUbigeoInei()+"");
		}
		System.out.println(resource);
		//resource = loader.getResource("/WEB-INF/formato/XLS.xls");
		//System.out.println(resource);
		return resource.getURL();
	}
	*/


    public FormatoPreCotejoView() {
        super();
        setUrl("/WEB-INF/formato/Formato-con-export");
		/*
		try {
			setUrl(getWorkbookUrl().toString());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
			throw new RuntimeException("No se encuentra formato xls");
		}
		*/
    }

    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String nombreArchivo = (String) model.get("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "Padron.xls";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

		/*
		if(workbookAlreadyExists) {
			return;
		}     */

        List<Ubigeo> ubigeoList = (List<Ubigeo>) model.get("ubigeoList");
        List<CentroPoblado> centroPobladoList = (List<CentroPoblado>) model.get("centroPobladoList");
        List<CentroEducativo> centroEducativoList = (List<CentroEducativo>) model.get("centroEducativoList");
        List<EstablecimientoSalud> establecimientoSaludList = (List<EstablecimientoSalud>) model.get("establecimientoSaludList");
        List<Dominio> gradoInstruccionList = (List<Dominio>) model.get("gradoInstruccionList");
        List<Dominio> tipoSeguroMenorList = (List<Dominio>) model.get("tipoSeguroMenorList");

        List<Dominio> tipoDocumentoMenorList = (List<Dominio>) model.get("tipoDocumentoMenorList");
        List<Dominio> lenguajeMadreList = (List<Dominio>) model.get("lenguajeMadreList");
        List<Dominio> generoList = (List<Dominio>) model.get("generoList");
        List<Dominio> tipoVinculoList = (List<Dominio>) model.get("tipoVinculoList");
        List<Dominio> programaSocialList = (List<Dominio>) model.get("programaSocialList");

        createUbigeosSheet(workbook, ubigeoList);
        createCentroPobladoSheet(workbook, centroPobladoList);
        createCentroEducativoSheet(workbook, centroEducativoList);
        createEstablecimientoSaludSheet(workbook, establecimientoSaludList);
        createGradoInstruccionSheet(workbook, gradoInstruccionList);
        createTipoSeguroMenorSheet(workbook, tipoSeguroMenorList);
        createTipoDocumentoMenorSheet(workbook, tipoDocumentoMenorList);
        createLenguajeMadreSheet(workbook, lenguajeMadreList);
        createGeneroSheet(workbook, generoList);
        createTipoVinculoSheet(workbook, tipoVinculoList);
        updateProSocialTbl(workbook, programaSocialList);
    }

    private void createUbigeosSheet(HSSFWorkbook workbook, List<Ubigeo> ubigeoList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        //String coUbigeoIneiTblNameFormula = null;
        //String coUbigeoIneiNameFormula = null;

        HSSFSheet sheet = workbook.getSheet("coUbigeoInei$");
        if (sheet == null) {
            sheet = workbook.createSheet("coUbigeoInei$");
        } else {
			/*
			// NO ELIMINAR NAMEDRANGES POI AL GUARDAR HACE UN MESS CON LOS NAMEDRANGES
			Name coUbigeoIneiName = workbook.getName("coUbigeoInei");
			coUbigeoIneiNameFormula = coUbigeoIneiName.getRefersToFormula();
			Name coUbigeoIneiTblName = workbook.getName("coUbigeoIneiTbl");
			coUbigeoIneiTblNameFormula = coUbigeoIneiTblName.getRefersToFormula();
			*/

            //removeWorksheet("coUbigeoInei$", workbook);
            //sheet = workbook.createSheet("coUbigeoInei$");
			/*
			for (Row rowi : sheet) {
				sheet.removeRow(rowi);
			}
			*/
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deUbigeoInei");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coUbigeoInei");

        int currentRow = 1;
        for (Ubigeo ubigeo : ubigeoList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(
                    WordUtils.capitalizeFully((ubigeo.getDeDepartamento() == null ? "" : ubigeo.getDeDepartamento()).trim()) + "-" +
                            WordUtils.capitalizeFully((ubigeo.getDeProvincia() == null ? "" : ubigeo.getDeProvincia()).trim()) + "-" +
                            WordUtils.capitalizeFully((ubigeo.getDeDistrito() == null ? "" : ubigeo.getDeDistrito()).trim())
            );

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(ubigeo.getCoUbigeoInei().trim());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

		/*
		// NO ELIMINAR NAMEDRANGES POI AL GUARDAR HACE UN MESS CON LOS NAMEDRANGES
		if(coUbigeoIneiNameFormula!=null) {
			Name coUbigeoIneiName = workbook.getName("coUbigeoInei");
			coUbigeoIneiName.setRefersToFormula(coUbigeoIneiNameFormula);
		}

		if(coUbigeoIneiTblNameFormula!=null) {
			Name coUbigeoIneiTblName = workbook.getName("coUbigeoIneiTbl");
			coUbigeoIneiTblName.setRefersToFormula(coUbigeoIneiTblNameFormula);
		}
		*/
    }

    private void createCentroPobladoSheet(HSSFWorkbook workbook, List<CentroPoblado> centroPobladoList) {
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;
        HSSFSheet sheet = workbook.getSheet("coCentroPoblado$");
        if (sheet == null) {
            sheet = workbook.createSheet("coCentroPoblado$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coCentroPoblado");

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("noCentroPoblado");

        cell = headerRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coUbigeoInei");

        cell = headerRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deUbigeoInei");

        int currentRow = 1;
        for (CentroPoblado centroPoblado : centroPobladoList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;


            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(centroPoblado.getCoCentroPoblado().trim());

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(
                    WordUtils.capitalizeFully((centroPoblado.getNoCentroPoblado() == null ? "" : centroPoblado.getNoCentroPoblado()).trim()) + " - " +
                            WordUtils.capitalizeFully((centroPoblado.getNoCategoria() == null ? "" : centroPoblado.getNoCategoria().trim())) + " - " +
                            WordUtils.capitalizeFully((centroPoblado.getDeDepartamento() == null ? "" : centroPoblado.getDeDepartamento()).trim()) + "/" +
                            WordUtils.capitalizeFully((centroPoblado.getDeProvincia() == null ? "" : centroPoblado.getDeProvincia()).trim()) + "/" +
                            WordUtils.capitalizeFully((centroPoblado.getDeDistrito() == null ? "" : centroPoblado.getDeDistrito()).trim())
            );

            cell = row.getCell(2);
            if (cell == null) {
                cell = row.createCell(2);
            }
            cell.setCellValue(centroPoblado.getCoUbigeoInei());

            cell = row.getCell(3);
            if (cell == null) {
                cell = row.createCell(3);
            }

            cell.setCellValue(
                    WordUtils.capitalizeFully((centroPoblado.getDeDepartamento() == null ? "" : centroPoblado.getDeDepartamento()).trim()) + "-" +
                            WordUtils.capitalizeFully((centroPoblado.getDeProvincia() == null ? "" : centroPoblado.getDeProvincia()).trim()) + "-" +
                            WordUtils.capitalizeFully((centroPoblado.getDeDistrito() == null ? "" : centroPoblado.getDeDistrito()).trim())
            );

        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void createCentroEducativoSheet(HSSFWorkbook workbook, List<CentroEducativo> centroEducativoList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("coCentroEducativo$");
        if (sheet == null) {
            sheet = workbook.createSheet("coCentroEducativo$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deCentroEducativo");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coCentroEducativo");

        cell = headerRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deTiCentroEducativo");

        cell = headerRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ubiCentroEducativo");

        cell = headerRow.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("idCentroEducativo");

        int currentRow = 1;
        for (CentroEducativo centroEducativo : centroEducativoList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(
                    WordUtils.capitalizeFully((centroEducativo.getDeDistrito() == null ? "" : centroEducativo.getDeDistrito()).trim()) + "-" +
                            WordUtils.capitalizeFully((centroEducativo.getNoCentroEducativo() == null ? "" : centroEducativo.getNoCentroEducativo()).trim()) + "-" +
                            WordUtils.capitalizeFully((centroEducativo.getDiCentroEducativo() == null ? "" : centroEducativo.getDiCentroEducativo()).trim())
            );

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue((centroEducativo.getCoModular() == null ? "" : centroEducativo.getCoModular()).trim());

            cell = row.getCell(2);
            if (cell == null) {
                cell = row.createCell(2);
            }
            cell.setCellValue(WordUtils.capitalizeFully((centroEducativo.getDeNivelEducativo() == null ? "" : centroEducativo.getDeNivelEducativo()).trim()));

            cell = row.getCell(3);
            if (cell == null) {
                cell = row.createCell(3);
            }
            cell.setCellValue(
                    WordUtils.capitalizeFully((centroEducativo.getDeDepartamento() == null ? "" : centroEducativo.getDeDepartamento()).trim()) + "-" +
                            WordUtils.capitalizeFully((centroEducativo.getDeProvincia() == null ? "" : centroEducativo.getDeProvincia()).trim()) + "-" +
                            WordUtils.capitalizeFully((centroEducativo.getDeDistrito() == null ? "" : centroEducativo.getDeDistrito()).trim()));

            cell = row.getCell(4);
            if (cell == null) {
                cell = row.createCell(4);
            }
            cell.setCellValue(centroEducativo.getCoCentroEducativo());

            //row.createCell(0).setCellValue(centroEducativo.getCoCentroEducativo());
            //row.createCell(1).setCellValue(centroEducativo.getNoCentroEducativo());
            //row.createCell(2).setCellValue(centroEducativo.getDiCentroEducativo());
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
    }

    private void createEstablecimientoSaludSheet(HSSFWorkbook workbook, List<EstablecimientoSalud> establecimientoSaludList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("coEstSalud$");
        if (sheet == null) {
            sheet = workbook.createSheet("coEstSalud$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deEstSalud");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coEstSalud");

        cell = headerRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deTiEstSalud");

        cell = headerRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ubiEstSalud");

        int currentRow = 1;
        for (EstablecimientoSalud establecimientoSalud : establecimientoSaludList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(
                    WordUtils.capitalizeFully((establecimientoSalud.getDeDistrito() == null ? "" : establecimientoSalud.getDeDistrito()).trim()) + "-" +
                            WordUtils.capitalizeFully((establecimientoSalud.getDeEstSalud() == null ? "" : establecimientoSalud.getDeEstSalud()).trim()) + "-" +
                            WordUtils.capitalizeFully((establecimientoSalud.getDeDireccion() == null ? "" : establecimientoSalud.getDeDireccion()).trim())
            );

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue((establecimientoSalud.getCoEstSalud() == null ? "" : establecimientoSalud.getCoEstSalud()).trim());

            cell = row.getCell(2);
            if (cell == null) {
                cell = row.createCell(2);
            }
            cell.setCellValue(WordUtils.capitalizeFully((establecimientoSalud.getTiEstSalud() == null ? "" : establecimientoSalud.getTiEstSalud()).trim()));

            cell = row.getCell(3);
            if (cell == null) {
                cell = row.createCell(3);
            }
            cell.setCellValue(
                    WordUtils.capitalizeFully((establecimientoSalud.getDeDepartamento() == null ? "" : establecimientoSalud.getDeDepartamento()).trim()) + "-" +
                            WordUtils.capitalizeFully((establecimientoSalud.getDeProvincia() == null ? "" : establecimientoSalud.getDeProvincia()).trim()) + "-" +
                            WordUtils.capitalizeFully((establecimientoSalud.getDeDistrito() == null ? "" : establecimientoSalud.getDeDistrito()).trim())
            );

            //row.createCell(0).setCellValue(establecimientoSalud.getCoEstSalud());
            //row.createCell(1).setCellValue(establecimientoSalud.getDeDepartamento());
            //row.createCell(2).setCellValue(establecimientoSalud.getDeProvincia());
            //row.createCell(3).setCellValue(establecimientoSalud.getDeDistrito());
            //row.createCell(4).setCellValue(establecimientoSalud.getDeEstSalud());
            //row.createCell(5).setCellValue(establecimientoSalud.getDeDireccion());
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        //sheet.autoSizeColumn(4);
        //sheet.autoSizeColumn(5);
    }

    private void createGradoInstruccionSheet(HSSFWorkbook workbook, List<Dominio> gradoInstruccionList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("coNivelEduca$");
        if (sheet == null) {
            sheet = workbook.createSheet("coNivelEduca$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deNivelEduca");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coNivelEduca");

        int currentRow = 1;
        for (Dominio gradoInstruccion : gradoInstruccionList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(WordUtils.capitalizeFully(gradoInstruccion.getDeDom().trim()));

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(WordUtils.capitalizeFully(gradoInstruccion.getCoDominio()));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void createTipoSeguroMenorSheet(HSSFWorkbook workbook, List<Dominio> tipoSeguroMenorList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("tiSeguroMenor$");
        if (sheet == null) {
            sheet = workbook.createSheet("tiSeguroMenor$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deTiSeguroMenor");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coTiSeguroMenor");

        int currentRow = 1;
        for (Dominio tipoSeguroMenor : tipoSeguroMenorList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(WordUtils.capitalizeFully(tipoSeguroMenor.getDeDom().trim()));

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(WordUtils.capitalizeFully(tipoSeguroMenor.getCoDominio()));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }


    private void createLenguajeMadreSheet(HSSFWorkbook workbook, List<Dominio> lenguajeMadreList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("coLenMadre$");
        if (sheet == null) {
            sheet = workbook.createSheet("coLenMadre$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deLenMadre");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coLenMadre");

        int currentRow = 1;
        for (Dominio lenguajeMadre : lenguajeMadreList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(WordUtils.capitalizeFully(lenguajeMadre.getDeDom().trim()));

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(WordUtils.capitalizeFully(lenguajeMadre.getCoDominio()));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }


    private void createTipoDocumentoMenorSheet(HSSFWorkbook workbook, List<Dominio> tipoDocumentoMenorList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("tiDocIden$");
        if (sheet == null) {
            sheet = workbook.createSheet("tiDocIden$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deTiDocIden");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coTiDocIden");

        int currentRow = 1;
        for (Dominio tipoDocumentoMenor : tipoDocumentoMenorList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(WordUtils.capitalizeFully(tipoDocumentoMenor.getDeDom().trim()));

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(WordUtils.capitalizeFully(tipoDocumentoMenor.getCoDominio()));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }


    private void createGeneroSheet(HSSFWorkbook workbook, List<Dominio> generoList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("coGenero$");
        if (sheet == null) {
            sheet = workbook.createSheet("coGenero$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deGenero");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coGenero");

        int currentRow = 1;
        for (Dominio genero : generoList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(WordUtils.capitalizeFully(genero.getDeDom().trim()));

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(WordUtils.capitalizeFully(genero.getCoDominio()));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }


    private void createTipoVinculoSheet(HSSFWorkbook workbook, List<Dominio> tipoVinculoList) {

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFont(font);

        HSSFRow row;
        HSSFCell cell;

        HSSFSheet sheet = workbook.getSheet("tiVinculoMenor$");
        if (sheet == null) {
            sheet = workbook.createSheet("tiVinculoMenor$");
        }

        HSSFRow headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("deTiVinculoMenor");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("coTiVinculoMenor");

        int currentRow = 1;
        for (Dominio tipoVinculo : tipoVinculoList) {
            row = sheet.getRow(currentRow);
            if (row == null) {
                row = sheet.createRow(currentRow);
            }
            currentRow += 1;

            cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(WordUtils.capitalizeFully(tipoVinculo.getDeDom().trim()));

            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellValue(WordUtils.capitalizeFully(tipoVinculo.getCoDominio()));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void updateProSocialTbl(HSSFWorkbook workbook, List<Dominio> programaSocialList) {

        Name programaSocialRangeName = workbook.getName("form_proSocialTbl");
        if (programaSocialRangeName == null || programaSocialRangeName.isDeleted() || programaSocialRangeName.isFunctionName())
            return;
        AreaReference aref = new AreaReference(programaSocialRangeName.getRefersToFormula());
        CellReference[] crefs = aref.getAllReferencedCells();
        for (int i = 0; i < crefs.length; i++) {
            Sheet sheet = workbook.getSheet(crefs[i].getSheetName());
            Row row = sheet.getRow(crefs[i].getRow());
            Cell cell = row.getCell(crefs[i].getCol());
            if (cell == null) {
                cell = row.createCell(crefs[i].getCol());
            }
            if (i < programaSocialList.size()) {
                Dominio dominio = programaSocialList.get(i);
                cell.setCellValue(dominio.getDeDom());
            } else {
                break;
            }
        }
        if (crefs.length < programaSocialList.size()) {
            logger.fatal("EL FORMATO NECESITA SER ACTUALIZADO (INSERTAR MÁS FILAS EN 'form_proSocialTbl')");
        }


        programaSocialRangeName = workbook.getName("form_proSocialIdTbl");
        if (programaSocialRangeName == null || programaSocialRangeName.isDeleted() || programaSocialRangeName.isFunctionName())
            return;
        aref = new AreaReference(programaSocialRangeName.getRefersToFormula());
        crefs = aref.getAllReferencedCells();
        for (int i = 0; i < crefs.length; i++) {
            Sheet sheet = workbook.getSheet(crefs[i].getSheetName());
            Row row = sheet.getRow(crefs[i].getRow());
            Cell cell = row.getCell(crefs[i].getCol());
            if (cell == null) {
                cell = row.createCell(crefs[i].getCol());
            }
            if (i < programaSocialList.size()) {
                Dominio dominio = programaSocialList.get(i);
                cell.setCellValue(dominio.getCoDominio());
            } else {
                break;
            }
        }
        if (crefs.length < programaSocialList.size()) {
            logger.fatal("EL FORMATO NECESITA SER ACTUALIZADO (INSERTAR MÁS FILAS EN 'form_proSocialIdTbl')");
        }

    }

    private void removeWorksheet(String wsname, HSSFWorkbook wb) {
        for (int i = wb.getNumberOfSheets() - 1; i >= 0; i--) {
            HSSFSheet tmpSheet = wb.getSheetAt(i);
            if (tmpSheet.getSheetName().equals(wsname)) {
                wb.removeSheetAt(i);
            }
        }
    }

}
