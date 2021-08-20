package pe.gob.reniec.padronn.logic.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Properties;

/**
 * Clase Properties.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 21/06/13 10:58 AM
 */
@Component
public class PadronProperties {

	@Autowired
	@Qualifier("applicationProps")
	Properties properties;

	Validator validator;
	Logger log = Logger.getLogger(getClass());

	@NotNull
	public String ESREGISTRO_REG_OK;

	@NotNull
	public String ESREGISTRO_REG_ERROR;
	@NotNull
	public String ESREGISTRO_COT_PROCESO;
	@NotNull
	public String ESREGISTRO_COT_ACTUALIZADO;
	@NotNull
	public String ESREGISTRO_COT_INSERTADO;
	@NotNull
	public String ESREGISTRO_COT_ERROR;

	@NotNull
	public String ESENVIO_REG_PROCESO;
	@NotNull
	public String ESENVIO_REG_OK;
	@NotNull
	public String ESENVIO_REG_ERROR;
	@NotNull
	public String ESENVIO_REG_CORREGIDO;

    @NotNull
    public String EDAD_MIN_MADRE;

    @NotNull
	public String FICHA_MAYOR;

	@NotNull
	public String FICHA_MENOR;

	@NotNull
	public String RANGE_MONTHS_SEARCH;

	@NotNull
	public String RANGE_NAME;
	@NotNull
	public String MAXROWS;

	@NotNull
	public int MAXROWS_REPORTE;

	@NotNull
	public String MESSAGE_MAX_ROW;

	@NotNull
	public String[] MAPCOLS;
	@NotNull
	public String[] MAPCOLS_TRAD;
	@NotNull
	public String MAPCOLS_SEPARADOR_DEFINICION;
	@NotNull
	public String MAPCOLS_SEPARADOR_LINEA;

	//@NotNull
	//public String FORM_PATH;
	@NotNull
	public String FORM_NOMBRE;

    @NotNull
    public String REPORTE_XLS_NOMBRE;

	@NotNull
	public String PAGINA_FILAS_STR;

	@NotNull
	public int PAGINA_FILAS;

	@NotNull
	public String realPath;

    @NotNull
    public String[] DISTRITOS_EUROPAN;

    @NotNull
    public String REGISTRADOR_PERFIL;

    @NotNull
    public String MUNICIPALIDAD_ENTIDAD;

    @NotNull
	public String ESTABLECIMIENTO_ENTIDAD;

	@NotNull
	public String URL_WS_IMAGENES;

	@PostConstruct
	public void start() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();

		ESREGISTRO_REG_OK = properties.getProperty("APP.PRECOTEJO.ESREGISTRO.REG.OK");
		ESREGISTRO_REG_ERROR = properties.getProperty("APP.PRECOTEJO.ESREGISTRO.REG.ERROR");
		ESREGISTRO_COT_PROCESO = properties.getProperty("APP.PRECOTEJO.ESREGISTRO.COT.PROCESO");
		ESREGISTRO_COT_ACTUALIZADO = properties.getProperty("APP.PRECOTEJO.ESREGISTRO.COT.ACTUALIZADO");
		ESREGISTRO_COT_INSERTADO = properties.getProperty("APP.PRECOTEJO.ESREGISTRO.COT.INSERTADO");
		ESREGISTRO_COT_ERROR = properties.getProperty("APP.PRECOTEJO.ESREGISTRO.COT.ERROR");

		ESENVIO_REG_PROCESO = properties.getProperty("APP.PRECOTEJO.ESENVIO.REG.PROCESO");
		ESENVIO_REG_OK = properties.getProperty("APP.PRECOTEJO.ESENVIO.REG.OK");
		ESENVIO_REG_ERROR = properties.getProperty("APP.PRECOTEJO.ESENVIO.REG.ERROR");
		ESENVIO_REG_CORREGIDO = properties.getProperty("APP.PRECOTEJO.ESENVIO.REG.CORREGIDO");

		RANGE_NAME = properties.getProperty("APP.PRECOTEJO.RANGE.NAME");
		MAXROWS = properties.getProperty("APP.PRECOTEJO.MAXROWS");
		MAXROWS_REPORTE = Math.abs(Integer.parseInt(properties.getProperty("APP.REPORTES.MAXROWS")));
		MESSAGE_MAX_ROW = properties.getProperty("APP.REPORTES.MESSAGE.MAX.ROW");

		MAPCOLS = StringUtils.split(properties.getProperty("APP.PRECOTEJO.MAPCOLS"), ",");
		MAPCOLS_TRAD = StringUtils.split(properties.getProperty("APP.PRECOTEJO.MAPCOLS.TRAD"), ",");
		MAPCOLS_SEPARADOR_DEFINICION = properties.getProperty("APP.PRECOTEJO.MAPCOLS.SEPARADOR.DEFINICION");
		MAPCOLS_SEPARADOR_LINEA = properties.getProperty("APP.PRECOTEJO.MAPCOLS.SEPARADOR.LINEA");

		//FORM_PATH = properties.getProperty("APP.PRECOTEJO.FORM.PATH");
		FORM_NOMBRE = properties.getProperty("APP.PRECOTEJO.FORM.NOMBRE");
        REPORTE_XLS_NOMBRE = properties.getProperty("APP.REPORTE.XLS.NOMBRE");

		PAGINA_FILAS_STR = properties.getProperty("APP.PAGINA.FILAS");

        EDAD_MIN_MADRE = properties.getProperty("APP.EDAD.MIN.MADRE");
		FICHA_MAYOR = properties.getProperty("APP.FICHA.MAYOR");
		FICHA_MENOR = properties.getProperty("APP.FICHA.MENOR");
		RANGE_MONTHS_SEARCH = properties.getProperty("APP.RANGE.MONTHS.SEARCH");

		//WebApplicationContext applicationContext = new GenericWebApplicationContext();
		//if(!applicationContext.getResource(FORM_PATH).exists()) {
		//	throw new IllegalStateException("No existe formato '"+ FORM_PATH +"'.");
		//}

		PAGINA_FILAS = Math.abs(Integer.parseInt(PAGINA_FILAS_STR));

        DISTRITOS_EUROPAN = properties.getProperty("APP.REPORTE.DISTRITOS_EUROPAN").split(",");

        REGISTRADOR_PERFIL = properties.getProperty("APP.REGISTRADOR_PERFIL");
        MUNICIPALIDAD_ENTIDAD = properties.getProperty("APP.MUNICIPALIDAD_ENTIDAD");
		ESTABLECIMIENTO_ENTIDAD = properties.getProperty("APP.ESTABLECIMIENTO_ENTIDAD");

		URL_WS_IMAGENES = properties.getProperty("APP.URL_WS_IMAGENES");

//		log.debug("Intentando encontrar real path... ");
		try {
			realPath = new File(new File(new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")).getParent()).getParent()).getParent();
//			log.debug("Real path: "+realPath);
		} catch(Exception e) {
			e.printStackTrace();
		}

		if (validator.validate(this).size() > 0
				|| MAPCOLS.length != MAPCOLS_TRAD.length) {
			throw new IllegalStateException("No existe archivo properties o no contiene las definiciones apropiadas.");
		}

//		log.debug("PadronProperties iniciado...");
	}

	public Object getProperty(String name) {
		return properties.getProperty(name);
	}

	public String getReadableMappingColumn(String mapColName) {
		int index;
		if ((index = ArrayUtils.lastIndexOf(MAPCOLS, mapColName)) == -1)
			return mapColName;
		return MAPCOLS_TRAD[index];
	}

	public String getReadableMappinColumnUpperCase(String mapColName) {
		if(mapColName == null)
			return null;
		for (int i = 0; i < MAPCOLS.length; i++) {
			if(MAPCOLS[i].equalsIgnoreCase(mapColName)) {
				return MAPCOLS_TRAD[i];
			}
		}
		return mapColName;
	}

	public byte[] getPdfReportBytes(String jrFilePath, Map<String, Object> params)
			throws JRException {
		// TODO corregir los error por memory leaks producidos por AWT, y localrepository de JasperReports java - OutOfMemoryError: PermGen Space
		// ver http://stackoverflow.com/questions/12943030/outofmemoryerror-permgen-space-jasper-report-with-spring-running-on-tomcat
		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(realPath+"/"+jrFilePath);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
		JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1254");
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		return baos.toByteArray();
	}

	public String escribeSolicitud(HttpServletResponse response, byte[] reporte, String tipoArchivo, String tipoDescarga, String fileName) {
		if(reporte.length==0 || tipoArchivo==null || tipoArchivo.isEmpty() || tipoDescarga==null || tipoDescarga.isEmpty())
			throw new InvalidParameterException(this.getClass().getName()+".escribeSolicitud invalid params");

		// enviar el reporte a cliente
		if(tipoArchivo.equalsIgnoreCase("pdf")) {
			response.setContentType("application/pdf");
			response.setCharacterEncoding("ISO-8859-1");
			if(tipoDescarga.equalsIgnoreCase("v")) {
				// mostrar en línea
				response.setHeader("Content-Disposition", "inline;filename="+fileName+".pdf");
				response.setHeader("Content-Transfer-Encoding", "binary");
			} else {
				// por defecto descargar
				response.setHeader("Content-Disposition", "attachment;filename="+fileName+".pdf");
				response.setHeader("Content-Transfer-Encoding", "binary");
			}
			response.setDateHeader("Expires", 0);
			response.setContentLength(reporte.length);

			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(reporte, 0, reporte.length);
				out.flush();
				out.close();
				// escribió adecuadamente archivo PDF
				return null;

			} catch(IOException ioe) {
				log.error("Error I/O al intentar escribir la solicitud PDF en HttpServletResponse: " +fileName+ " : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			// error al escribir archivo PDF
			response.setStatus(500);
			return null;

		} else {
			throw new UnsupportedOperationException();
		}
	}
}
