package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.reniec.padronn.logic.model.Precotejo;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.model.form.PrecotejoUploadForm;
import pe.gob.reniec.padronn.logic.service.PrecotejoService;
import pe.gob.reniec.padronn.logic.service.PrecotejoUploadService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.web.validator.PrecotejoUploadFormValidator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("precotejo")
public class PrecotejoUploadController
		extends AbstractBaseController {

	@Autowired
	PrecotejoUploadFormValidator precotejoUploadFormValidator;

	@Autowired
	PrecotejoService precotejoService;

	@Autowired
	PrecotejoUploadService precotejoUploadService;

	@Autowired
	Usuario usuario;

    @Autowired
    PadronProperties padronProperties;

  // ver: http://stackoverflow.com/questions/5211323/what-is-the-purpose-of-init-binder-in-spring-mvc
	@InitBinder("uploadForm")
	public void initBinder(WebDataBinder binder) {
		binder.setIgnoreInvalidFields(true);
		binder.setIgnoreUnknownFields(true);
		binder.setValidator(precotejoUploadFormValidator);
		binder.registerCustomEditor(
				MultipartFile.class,
				"files",
				new PropertyEditorSupport() {
					@Override
					public void setAsText(String text) {
						// ver: http://blog.jamesrossiter.co.uk/2013/03/26/use-a-spring-initbinder-to-resolve-type-mismatch-and-bind-exceptions-in-post-from-spring-framework-mvc-forms-to-controller-actions/
						// sin esta clase Spring fails miserably cuando no se envía nada
					}
				}
		);
	}

	@RequestMapping(value = "precotejoobs.do", method = RequestMethod.GET)
	public String doPrecotejoObs(@RequestParam String nuEnvio, HttpServletResponse response, Model model) {
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("UTF-8");

		String fileName = "PREOBS-"+nuEnvio+".csv";

        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];

        byte[]reporte = precotejoUploadService.writeToBytes(precotejoService.getAllFromPrecotejoObs(coEntidad, nuEnvio));
		response.setHeader("Content-Disposition","attachment;filename="+fileName);
		response.setDateHeader("Expires", 0);
		response.setContentLength(reporte.length);

		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(reporte, 0, reporte.length);
			out.flush();
			out.close();
			return null;

		} catch(IOException ioe) {
			log.error(ioe.getMessage());
			ioe.printStackTrace();
		}

		response.setStatus(500);
		return null;
	}

	@RequestMapping(value = "allobs.do", method = RequestMethod.GET)
	public String doAllObs(@RequestParam String nuEnvio, HttpServletResponse response, Model model) {
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("UTF-8");

		String fileName = "OBS_ENVIO_"+nuEnvio+".csv";

        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];

        byte[]reporte = precotejoUploadService.writeToBytes(precotejoService.getAllObs(coEntidad, nuEnvio));
		response.setHeader("Content-Disposition","attachment;filename="+fileName);
		response.setDateHeader("Expires", 0);
		response.setContentLength(reporte.length);

		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(reporte, 0, reporte.length);
			out.flush();
			out.close();
			return null;

		} catch(IOException ioe) {
			log.error(ioe.getMessage());
			ioe.printStackTrace();
		}

		response.setStatus(500);
		return null;
	}

	@RequestMapping(value = "cotejoobs.do", method = RequestMethod.GET)
	public String doCotejoObs(@RequestParam String nuEnvio, HttpServletResponse response, Model model) {
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("UTF-8");

		byte[]reporte;
		String fileName;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];

        List<PrecotejoRegistro> list = precotejoService.getAllFromCotejoObsDetails(coEntidad, nuEnvio);
		Precotejo precotejo;
		if(list == null) {
			precotejo = new Precotejo();
			reporte = new byte[0];
			fileName =  "error.csv";
		}  else {

			precotejo = precotejoService.getPrecotejoDetails(coEntidad, nuEnvio);
			reporte = precotejoUploadService.writeToCsvBytesFromPrecoteRegistroList(list);
			fileName = precotejo.getNoArchivoOriginal()+".obscot.csv";
		}

		response.setHeader("Content-Disposition","attachment;filename="+fileName);
		response.setDateHeader("Expires", 0);
		response.setContentLength(reporte.length);

		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(reporte, 0, reporte.length);
			out.flush();
			out.close();
			return null;

		} catch(IOException ioe) {
			log.error(ioe.getMessage());
			ioe.printStackTrace();
		}

		response.setStatus(500);
		return null;
	}

  // ver: http://stackoverflow.com/questions/3423262/what-is-modelattribute-in-spring-mvc
	@RequestMapping(value = "upload.do", method = RequestMethod.POST)
//public String doUpload(@Valid @ModelAttribute("uploadForm") PrecotejoUploadForm uploadForm, @RequestParam(required = false) Integer minimalCheck, BindingResult bindingResult, HttpServletResponse response, Model model) {	
	public String doUpload(@Valid @ModelAttribute("uploadForm") PrecotejoUploadForm uploadForm, BindingResult bindingResult, HttpServletResponse response, Model model) {
		//minimalCheck = (minimalCheck==null||minimalCheck!=1) ? 0 : 1;
		Integer minimalCheck = 1;
		// TODO modificar todo upload, simplificar usar JSR303, y model attributes de mejor forma
//		log.debug("Iniciando recepción de archivos... (" + uploadForm.getFiles().size() + ") minimalCheck=" + minimalCheck);

		if(bindingResult.hasErrors() || uploadForm.getFiles()==null) {
//			log.debug("No se ha recibido información válida: " + Arrays.toString(bindingResult.getAllErrors().toArray()));
		}

		if(uploadForm.getFiles().size()>0) {
//			log.debug("Archivos para registrar: " + uploadForm.getFiles().size() + " archivos");
			// insertamos archivos
			final Precotejo precotejo = new Precotejo();
            String[] split = usuario.getCoEntidad().split("_");
            String coEntidad = split[0];

            precotejo.setCoEntidad(Integer.parseInt(coEntidad));
			precotejo.setUsCreaAudi(usuario.getLogin());
			precotejo.setUsModiAudi(usuario.getLogin());
			//
			List<HashMap<String, String>> successFiles = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> errorFiles = new ArrayList<HashMap<String, String>>();
			for(final MultipartFile file:uploadForm.getFiles()) {
				precotejo.setNoArchivoOriginal(file.getOriginalFilename());
				String fileExt = FilenameUtils.getExtension(file.getOriginalFilename());
				List<PrecotejoRegistro> precotejoRegistroList = null;

				try {
					if("csv".equalsIgnoreCase(fileExt)) {
						precotejoRegistroList = precotejoUploadService.readFromStream(file.getInputStream());
					} else {
						try {
							precotejoRegistroList = precotejoUploadService.readFromExcelStream(file.getInputStream());

						} catch(RuntimeException re) {
							re.printStackTrace();
							log.error(re.getMessage());
							//ya se ha impreso el stacktrace
							//
							errorFiles.add(new HashMap<String, String>() {{
								put("noFile", file.getOriginalFilename()+" Error: Archivo con errores-Descargue el archivo otra vez");
							}});
						}
					}

					if(precotejoRegistroList != null) {
						log.debug("Los siguientes registros serán validados e insertados: " +precotejoRegistroList);
						precotejoService.insert(precotejo, precotejoRegistroList, (minimalCheck==1));
						successFiles.add(new HashMap<String, String>() {{
							put("noFile", file.getOriginalFilename());
							put("nuEnvio", precotejo.getNuEnvio().toString());
						}});
					}

				} catch (IOException ioe) {
					log.error(ioe.getMessage());
					ioe.printStackTrace();
					//
					errorFiles.add(new HashMap<String, String>() {{
						put("noFile", file.getOriginalFilename());
					}});

				} catch (DataAccessException dae) {
					log.error(dae.getMessage());
					dae.printStackTrace();
					//
					errorFiles.add(new HashMap<String, String>() {{
						put("noFile", file.getOriginalFilename());
					}});
				}
			}
			model.addAttribute("errorFiles", errorFiles);
			model.addAttribute("successFiles", successFiles);
			model.addAttribute("esEnvio", precotejo.getEsEnvio());
		}

    int nuPagina = 1;
    String[] split = usuario.getCoEntidad().split("_");
    String coEntidad = split[0];

    int filas = precotejoService.countAll(coEntidad, null, null, null, null);

    model.addAttribute("nuPaginas", Math.floor(filas/padronProperties.PAGINA_FILAS)+(filas%padronProperties.PAGINA_FILAS==0?0:1));
    model.addAttribute("nuPagina", nuPagina);
    model.addAttribute("precotejoList",
        precotejoService.getAll(
            coEntidad, null, null, null, null, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS
        )
    );
    return "precotejo/precotejoform";
	}
}
