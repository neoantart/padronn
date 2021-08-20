package pe.gob.reniec.padronn.logic.service.impl;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.PrecotejoUploadService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase PrecotejoUploadServiceImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:06 PM
 */
@Service
public class PrecotejoUploadServiceImpl
		extends AbstractBaseService
		implements PrecotejoUploadService {

	//@Autowired
	//Properties applicationProps;

	@Autowired
	PadronProperties padronProperties;

	@Override
	public List<PrecotejoRegistro> readFromStream(InputStream stream) {
//		log.debug("Iniciando parseo...");

		// TODO Limitar las entradas del CSV, no nos pueden enviar 1000 caracteres en un campo!

		//CSVReader csvReader = new CSVReader(new InputStreamReader(stream), ',', '\"', 1); //obviamos header
		CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("ISO-8859-1")), ',', '\"');
		//ColumnPositionMappingStrategy<PrecotejoRegistro> mappingStrategy = new ColumnPositionMappingStrategy<PrecotejoRegistro>(); //esta estrategia hace calzar el núemro de columan con la propiedad en ese índice
		HeaderColumnNameMappingStrategy<PrecotejoRegistro> mappingStrategy = new HeaderColumnNameMappingStrategy<PrecotejoRegistro>(); //el nombre de columna se toma como estrategia de mapeo
		mappingStrategy.setType(PrecotejoRegistro.class);
		/*
		String[] columnMapping;
		try {
			columnMapping = applicationProps.getProperty("APP.PRECOTEJO.MAPCOLS").split(",");
		} catch(Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(new Throwable("ERR.PRECOTEJO.CONFIG"));
		}
		mappingStrategy.setColumnMapping(columnMapping);
		*/

		//CsvToBean<PrecotejoRegistro> csv = new CsvToBean<PrecotejoRegistro>();
		List<PrecotejoRegistro> list;
		try {
			list = (new CsvToBean<PrecotejoRegistro>()).parse(mappingStrategy, csvReader);
//			log.info("precotejoRegistro :" + list.toString());

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			list = new ArrayList<PrecotejoRegistro>();

		} finally {
			try {
				csvReader.close();
				stream.close();
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}

		// TODO Lo siguiente se debe realizar antes de parsear el documento: eliminar filas vacías, limitar número de filas a APP.PRECOTEJO.MAXCOLS
		// eliminar beans vacíos
		ArrayList<PrecotejoRegistro> newList = new ArrayList<PrecotejoRegistro>();
		for (PrecotejoRegistro precotejoRegistro : list) {
			if (!precotejoRegistro.isEmpty()) {
				if (newList.size() <= MAX_ROWS) {
					newList.add(precotejoRegistro);
				} else {
					break;
				}
			}
		}

//		log.debug("Parseo finalizado... OK");
		return newList;
	}

	SimpleDateFormat sdf;
	DecimalFormat nf;
	String rangeName;
	PropertyUtilsBean propertyUtilsBean;
	int MAX_ROWS;
	String[] columnMapping;

	@PostConstruct
	public void start() {

		//try {
		//	columnMapping = applicationProps.getProperty("APP.PRECOTEJO.MAPCOLS").split(",");
		columnMapping = padronProperties.MAPCOLS;
		//
		//} catch (Exception e) {
		//	log.error(e.getMessage());
		//	e.printStackTrace();
		//	throw new RuntimeException("Configuración de mapeo de columnas XLS", new Throwable("ERR.PRECOTEJO.CONFIG"));
		//}

		//sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf = new SimpleDateFormat("dd/MM/yyyy");

		nf = new DecimalFormat("0.######");
		//nf.setParseIntegerOnly(false);
		//nf.setMaximumIntegerDigits(6);

		//rangeName = "DATA";
		rangeName = padronProperties.RANGE_NAME;

		propertyUtilsBean = new PropertyUtilsBean();

		MAX_ROWS = Integer.parseInt(padronProperties.MAXROWS);

	}

	@Override
	public List<PrecotejoRegistro> readFromExcelStream(InputStream stream) {
		log.debug("Iniciando parseo Excel...");

		// TODO Limitar las entradas del XLS, no nos pueden enviar 1000 caracteres en un campo!

		//NPOIFSFileSystem fs = new NPOIFSFileSystem(stream);
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(stream);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("No se puede leer el stream", new Throwable("ERR.IOE"));
			//return new ArrayList<PrecotejoRegistro>();
		}

		// La mejor forma de leer será a través de un NamedRange
		// http://poi.apache.org/spreadsheet/quick-guide.html#NamedRanges
		// también podría usarse una tabla; pero puede no ser compatible
		// http://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFTable.html
		// http://apache-poi.1045710.n5.nabble.com/Create-a-new-Table-into-an-XSSF-sheet-excel-2007-td4288371.html
		// http://apache-poi.1045710.n5.nabble.com/Extract-Excel-2007-tables-with-Jakarta-POI-3-7-td3395582.html
		// http://apache-poi.1045710.n5.nabble.com/Apache-POI-not-able-to-evaluate-table-names-in-the-Excel-Sheet-td5710583.html
		Name dataRangeName = workbook.getName(rangeName);
		if (dataRangeName == null || dataRangeName.isDeleted()) {
			log.error("No existe el nombre de rango '" + rangeName + "'");
			throw new RuntimeException("No existe/No es válido el nombre de rango '" + rangeName + "'", new Throwable("ERR.PRECOTEJO.CORRUPTFILE"));
		}

		// retrieve the cell at the named range and test its contents
		AreaReference dataRange = new AreaReference(dataRangeName.getRefersToFormula());
		Sheet sheet = workbook.getSheet(dataRangeName.getSheetName());
		Map<String, Integer> mapping = mapDataRange(sheet, dataRange, columnMapping);

		//log.debug("column mapping::"+Arrays.toString(columnMapping));

//		log.debug("Mapping (" + mapping.size() + " items) = " + mapping);

		// código obtenido de AreaReference.getAllReferencedCells()
		int minRow = Math.min(dataRange.getFirstCell().getRow(), dataRange.getLastCell().getRow());
		int maxRow = Math.max(dataRange.getFirstCell().getRow(), dataRange.getLastCell().getRow());
		List<PrecotejoRegistro> precotejoRegistroList = new ArrayList<PrecotejoRegistro>();
		boolean maxRowsReached = false;
		for (int rowi = minRow + 1; rowi <= maxRow && !maxRowsReached; rowi++) {
			if (rowi >= maxRow) {
				// obviemos la fila de la nota
				break;
			}

			Row row = sheet.getRow(rowi);
			PrecotejoRegistro precotejoRegistro = new PrecotejoRegistro();
			// "no"->11, "apPrimer"->12, "apSegundo"->13 ...
			//log.debug("Insertando fila = "+rowi);
			for (String colName : mapping.keySet()) {
				if (colName != null) {
					int colIndex = mapping.get(colName);
					String cellText = getCellText(row.getCell(colIndex));
					try {
						//http://commons.apache.org/proper/commons-beanutils/apidocs/org/apache/commons/beanutils/package-summary.html#package_description
						//http://commons.apache.org/proper/commons-beanutils//api/org/apache/commons/beanutils/PropertyUtilsBean.html
						//log.debug(" > " + colName + "=" + cellText);
						propertyUtilsBean.setSimpleProperty(precotejoRegistro, colName, cellText);
					} catch (IllegalAccessException e) {
						// las excepciones se ignoran, todas corresponden al estilo de programación, NO DEBERÍAN HABER EXCEPCIONES!
						// así que los beans vacíos (registros) se reportan como error de registro
						log.error(e.getMessage());
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						log.error(e.getMessage());
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}

			//log.debug(precotejoRegistro);
			//log.debug("coPadronPrograma:::"+precotejoRegistro.getTiProSocial());

			if (precotejoRegistroList.size() < MAX_ROWS) {
				if (precotejoRegistro.getNo() != null && !precotejoRegistro.getNo().isEmpty()) {
					precotejoRegistroList.add(precotejoRegistro);
				}
			} else {
				maxRowsReached = true;
				break;
			}
		}

		log.debug("Parseo finalizado... OK (max rows reached? " + maxRowsReached + ")");
		return precotejoRegistroList;
	}

	private Map<String, Integer> mapDataRange(Sheet sheet, AreaReference dataRange, String[] columnMapping) {

		// código obtenido de AreaReference.getAllReferencedCells()
		int minRow = Math.min(dataRange.getFirstCell().getRow(), dataRange.getLastCell().getRow());
		int maxRow = Math.max(dataRange.getFirstCell().getRow(), dataRange.getLastCell().getRow());
		int minCol = Math.min(dataRange.getFirstCell().getCol(), dataRange.getLastCell().getCol());
		int maxCol = Math.max(dataRange.getFirstCell().getCol(), dataRange.getLastCell().getCol());

		if (maxCol - minCol <= 0) {
			throw new RuntimeException("No es válido rango de datos", new Throwable("ERR.PRECOTEJO.CORRUPTFILE"));
		}

		Row row = sheet.getRow(minRow);
		if (row == null) {
			throw new RuntimeException("No se encontró una fila válida", new Throwable("ERR.PRECOTEJO.CORRUPTFILE"));
		}

		Map<String, Integer> mapping = new HashMap<String, Integer>();
		for (int coli = minCol; coli <= maxCol; coli++) {
			String cellText = getCellText(row.getCell(coli));
			if (ArrayUtils.contains(columnMapping, cellText)) {
				mapping.put(cellText, coli);
			}
		}

		// al menos una columna debe mapearse
		if (mapping.size() == 0) {
			throw new RuntimeException("No se ha encontrado ninguna columna de datos", new Throwable("ERR.PRECOTEJO.CORRUPTFILE"));
		}

		return mapping;
	}

	// get contents cell http://poi.apache.org/spreadsheet/quick-guide.html#CellContents
	private String getCellText(Cell cell) {

		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();

			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
                    //log.debug("campo:: " + cell.getStringCellValue());
					return sdf.format(cell.getDateCellValue());
				} else {
                    //log.debug("campo::: " + cell.getStringCellValue());
					//return nf.format(cell.getNumericCellValue());
					// http://stackoverflow.com/questions/1072561/how-can-i-read-numeric-strings-in-excel-cells-as-string-not-numbers-with-apach
					cell.setCellType(Cell.CELL_TYPE_STRING);
					return cell.getStringCellValue();
				}

			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());

			case Cell.CELL_TYPE_FORMULA:
				// obtengamos el resultado cacheado:  http://stackoverflow.com/questions/7608511/java-poi-how-to-read-excel-cell-value-and-not-the-formula-computing-it
				switch (cell.getCachedFormulaResultType()) {
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							return sdf.format(cell.getDateCellValue());
						} else {
							//return nf.format(cell.getNumericCellValue());
							// http://stackoverflow.com/questions/1072561/how-can-i-read-numeric-strings-in-excel-cells-as-string-not-numbers-with-apach
							cell.setCellType(Cell.CELL_TYPE_STRING);
							return cell.getStringCellValue();
						}
					case Cell.CELL_TYPE_STRING:
						return cell.getRichStringCellValue().getString();
				}
				//return cell.getCellFormula();

			case Cell.CELL_TYPE_BLANK:
				return "";
		}

		return null;
	}

	@Override
	public byte[] writeToBytes(List<HashMap<String, Object>> precotejoRegistroList) {
		if(precotejoRegistroList == null)
			return new byte[0];

//		log.debug("Iniciando volcado de registros... " + precotejoRegistroList.size() + " item(s)");

		OutputStream stream = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(stream));

		boolean writeHeader = true;
		for (HashMap<String, Object> precotejoRegistro : precotejoRegistroList) {
			if (writeHeader) {
				//log.debug("CABECERA:"+Arrays.toString((String[])precotejoRegistro.keySet().toArray(new String[0])));
				String[] list = new String[precotejoRegistro.keySet().size()];
				int i = 0;
				for (String key : precotejoRegistro.keySet()) {
					list[i++] = (key==null?"":String.valueOf(padronProperties.getReadableMappinColumnUpperCase(key)));
				}
				writer.writeNext(list);
				writeHeader = false;
			}
			//log.debug(" REG:"+Arrays.toString((String[])precotejoRegistro.values().toArray(new String[0])));
			String[] list = new String[precotejoRegistro.values().size()];
			int i = 0;
			precotejoRegistro.put("DEOBSERVACION", getDeObservacionSimple((String) precotejoRegistro.get("DEOBSERVACION")));
			for (Object object : precotejoRegistro.values()) {
				list[i++] = (object==null?"":String.valueOf(object));
			}
			writer.writeNext(list);
		}

		try {
			//obligamos escribir el contenido en stream
			writer.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
//		log.debug("Volcado finalizado... OK");
		return ((ByteArrayOutputStream) stream).toByteArray();
	}

	@Override
	public byte[] writeToCsvBytesFromPrecoteRegistroList(List<PrecotejoRegistro> precotejoRegistroList) {
		// TODO encaso_no_se_reciba_archivo_o_vacio_o_nulo?
		/*if(precotejoRegistroList == null) {
			log.error("Iniciando volcado de registros... NULL!") ;
			return new byte[0];
		}*/

//		log.debug("Iniciando volcado de registros... " + precotejoRegistroList.size() + " item(s)");

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(stream));

		// columnMapping contiene deObservacion
		writer.writeNext(columnMapping);
		for (PrecotejoRegistro precotejoRegistro : precotejoRegistroList) {
			String[] lineCsv = new String[columnMapping.length];
			for (int i = 0; i < columnMapping.length; i++) {
				try {
					lineCsv[i] = "";
					// String.valueOf es usado por Arrays.toString de Java!
					Object val = propertyUtilsBean.getSimpleProperty(precotejoRegistro, columnMapping[i]);
					if (val != null) {
						lineCsv[i] = String.valueOf(val);
					}
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			//log.debug(" Linea:" + ArrayUtils.toString(lineCsv));
//			log.debug(" Linea:" + Arrays.toString(lineCsv));
			writer.writeNext(lineCsv);
		}

		try {
			//obligamos escribir el contenido en stream
			writer.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}

//		log.debug("Volcado finalizado... OK");
		return stream.toByteArray();
	}


	@Override
	public OutputStream writeToStream(List<HashMap<String, Object>> precotejoRegistroList) {
//		log.debug("Iniciando volcado de registros... " + precotejoRegistroList.size() + " item(s)");

		OutputStream stream = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(stream));

		boolean writeHeader = true;
		for (HashMap<String, Object> precotejoRegistro : precotejoRegistroList) {
			if (writeHeader) {
				//log.debug("CABECERA:"+Arrays.toString((String[])precotejoRegistro.keySet().toArray(new String[0])));
				writer.writeNext((String[]) precotejoRegistro.keySet().toArray(new String[0]));
				writeHeader = false;
			}
			//log.debug(" REG:"+Arrays.toString((String[])precotejoRegistro.values().toArray(new String[0])));
			writer.writeNext((String[]) precotejoRegistro.values().toArray(new String[0]));
		}

		try {
			//obligamos escribir el contenido en stream
			writer.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
//		log.debug("Volcado finalizado... OK");
		return stream;
	}

	public String[] getColumnMapping() {
		return columnMapping;
	}

	public int getMAX_ROWS() {
		return MAX_ROWS;
	}

	public PropertyUtilsBean getPropertyUtilsBean() {
		return propertyUtilsBean;
	}

	public String getDeObservacionSimple(String deObservacion) {
		if(deObservacion != null) {
			StringBuilder deObservacionHtmlSb = new StringBuilder();
			//deObservacionHtmlSb.append("<ul>");
			for (StringTokenizer stringTokenizer = new StringTokenizer(deObservacion, padronProperties.MAPCOLS_SEPARADOR_LINEA); stringTokenizer.hasMoreTokens(); ) {
				String linea = stringTokenizer.nextToken();
				for (StringTokenizer tokenizer = new StringTokenizer(linea, padronProperties.MAPCOLS_SEPARADOR_DEFINICION); tokenizer.hasMoreTokens(); ) {
					String lineai = tokenizer.nextToken();
					//deObservacionHtmlSb.append("<li>");

					//deObservacionHtmlSb.append("<strong>");
					deObservacionHtmlSb.append(padronProperties.getReadableMappingColumn(lineai));
					//deObservacionHtmlSb.append("</strong>");

					if(tokenizer.hasMoreTokens()) {
						lineai = tokenizer.nextToken();
						deObservacionHtmlSb.append(": ");
						deObservacionHtmlSb.append(lineai);
					}

					//deObservacionHtmlSb.append("</li>");
				}
			}
			//deObservacionHtmlSb.append("</ul>");
			return deObservacionHtmlSb.toString();
		}
		return null;
	}
}
