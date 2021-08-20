package pe.gob.reniec.padronn.logic.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
//import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import pe.gob.reniec.jr.ftp.archivos.main.FtpArchivo;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ActaForm;
import pe.gob.reniec.padronn.logic.service.ActaArchivoService;
import pe.gob.reniec.padronn.logic.service.ActaService;
import pe.gob.reniec.padronn.logic.service.EstablecimientoSaludService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("upload")
public class ActaUploadController extends AbstractBaseController {

    private static Logger logger = Logger.getLogger("controller");

    @Autowired
    Usuario usuario;

    @Autowired
    ActaArchivoService actaArchivoService;

    @Autowired
    ActaService actaService;

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    EstablecimientoSaludService establecimientoSaludService;

    @RequestMapping("/actas/form.do")
    public String form(@ModelAttribute("actaForm") ActaForm actaForm, Model model) {
//        logger.debug("establecimientos salud" + establecimientoSaludService.listarPorCodigoUbigeoInei(usuario.getCoUbigeoInei()));
        model.addAttribute("establecimientosSalud", establecimientoSaludService.listarPorCodigoUbigeoInei(usuario.getCoUbigeoInei()));
        return "actas/acta-form";
    }

    @RequestMapping(value = "/actas/file.do", method = RequestMethod.POST)
    public String doFile(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletResponse response) throws IOException {
        //log.debug("guardando archivo en la base de datos");
        ActaArchivo actaArchivo = new ActaArchivo();
        actaArchivo.setArchivo(file.getBytes());
        actaArchivo.setNoActaArchivo(file.getOriginalFilename());
        actaArchivo.setSizeActaArchivo(Long.toString(file.getSize()));

        actaArchivo.setExtArchivo(FilenameUtils.getExtension(file.getOriginalFilename()));
//        logger.info("content/type: " + file.getContentType());
        actaArchivo.setContentType(file.getContentType());
        actaArchivo.setUsCreaAudi(usuario.getLogin());
        actaArchivo.setFeModiAudi(usuario.getLogin());

        actaArchivo.setCoActaArchivo(actaArchivoService.insert(actaArchivo));

        actaArchivo.setArchivo(null);
        List<ActaArchivo> actaArchivoList = new ArrayList<ActaArchivo>();
        actaArchivoList.add(actaArchivo);
        ObjectMapper mapper = new ObjectMapper();
//        logger.info(mapper.defaultPrettyPrintingWriter().writeValueAsString(actaArchivoList));
        try {
            response.addHeader("X-Requested-With", "XMLHttpRequest");
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            //out.print(mapper.defaultPrettyPrintingWriter().writeValueAsString(actaArchivoList));
            out.print(mapper.writeValueAsString(actaArchivoList));
            out.flush();
            return null;
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            ioe.printStackTrace();
            response.setStatus(500);
            return null;
        }
    }

    @RequestMapping(value = "/actas/insert.do", method = RequestMethod.POST)
    public String doGuardarActa(@Valid @ModelAttribute(value = "actaForm") ActaForm actaForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMensaje", "Nose puede guardar el acta!");
            model.addAttribute("establecimientosSalud", establecimientoSaludService.listarPorCodigoUbigeoInei(usuario.getCoUbigeoInei()));
            return "actas/acta-form";
        }

        if (actaForm.getFiles().size() > 0) {
            Acta acta = new Acta();
            acta.setFeIni(actaForm.getFeIniActa());
            acta.setFeFin(actaForm.getFeFinActa());

            acta.setDeActa(actaForm.getDeActa());

            acta.setUsCreaAudi(usuario.getLogin());
            acta.setCoEstSalud(actaForm.getCoEstSalud());

//            logger.info("coArchivos: " + actaForm.getFiles());
            acta.setCoActaArchivos(actaForm.getFiles());
            String[] split = usuario.getCoEntidad().split("_");
            String coEntidad = split[0];
            acta.setCoEntidad(coEntidad);
            String coActa = null;
            try {
                coActa = actaService.addActa(acta);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                model.addAttribute("errorMensaje", "No se puede guardar el acta!");
                model.addAttribute("establecimientosSalud", establecimientoSaludService.listarPorCodigoUbigeoInei(usuario.getCoUbigeoInei()));
                return "actas/acta-form";
            }

            model.addAttribute("successMensaje", "Acta insertada correctamente!");
            Acta actaDetalle = actaService.getActa(coActa);
            model.addAttribute("acta", actaDetalle);
            return "actas/acta-detalle";
        } else {
            model.addAttribute("successMensaje", "Nose puede guardar el acta!");
            result.rejectValue("coActaArchivos", "errors.actaForm.coActaArchivos", "Debe adjuntar minimo un archivo!");
            model.addAttribute("establecimientosSalud", establecimientoSaludService.listarPorCodigoUbigeoInei(usuario.getCoUbigeoInei()));
            return "actas/acta-form";
        }
    }

    @RequestMapping(value = "/actas/detalle.do", method = RequestMethod.GET)
    public String detalleActa(
            @RequestParam(value = "coActa", required = true) String coActa,
            @RequestParam(value = "volver", required = false) String volver,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            Model model) {
        Acta actaDetalle = actaService.getActa(coActa);
        model.addAttribute("acta", actaDetalle);
        model.addAttribute("volver", volver);
        model.addAttribute("nuPagina", nuPagina);
        //model.addAttribute("sizeFile", actaArchivoService.fomatFileSize())
        return "actas/acta-detalle";
    }

    @RequestMapping(value = "/actas/list.do", method = RequestMethod.GET)
    public String listarActas(@RequestParam(required = false) Integer nuPagina, Model model) {
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
        int filas = actaService.getNumActas(coEntidad);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        List<Acta> actas = actaService.getAll(coEntidad, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("actas", actas);
        return "actas/actas-list";
    }


    @RequestMapping(value = "/actas/download.do", method = RequestMethod.GET)
    public String downloadFile(@RequestParam(required = true, value = "coActaArchivo") String coActaArchivo, HttpServletResponse response) {
        ActaArchivo actaArchivo = actaArchivoService.getActaArchivo(coActaArchivo);
        if (actaArchivo != null) {
            response.setDateHeader("Expire", 0);
            byte[] archivo = actaArchivoService.getActaArchivoBytes(coActaArchivo);
            response.setContentLength(archivo.length);

            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + actaArchivo.getNoActaArchivo());
                ServletOutputStream out = response.getOutputStream();
                response.setContentType(actaArchivo.getContentType());
                out.write(archivo, 0, archivo.length);
                InputStream is = new ByteArrayInputStream(actaArchivoService.getActaArchivoBytes(coActaArchivo));
                IOUtils.copy(is, out);
                out.flush();
                out.close();
                return null;
            } catch (IOException ioe) {
                log.error(ioe.getMessage());
                ioe.printStackTrace();
                response.setStatus(500);
                return null;
            }

        } else {
            response.setContentType("image/jpg");
            response.setHeader("Content-Disposition", "attachment;filename=empty.jpg");
            response.setDateHeader("Expires", 0);
            response.setContentLength(0);
            return null;
        }
    }

    @RequestMapping("/actas/dar_baja.do")
    public String darBaja(
            @RequestParam(value = "coActa", required = true) String coActa,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            Model model) {
        model.addAttribute("coActa", coActa);
        model.addAttribute("nuPagina", nuPagina);
        return "actas/confirma-dar-baja";
    }

    @RequestMapping("/actas/confirma_dar_baja.do")
    public String confirmaDarBaja(
            @RequestParam(value = "esActa", required = false) String esActa,
            @RequestParam(value = "coActa", required = true) String coActa,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            @RequestParam(value = "nuPagina", required = true) String deObservacion,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (esActa != null && esActa.equals("0")) {
            actaService.darBajaActa(coActa, deObservacion);
            redirectAttributes.addFlashAttribute("mensajeSuccess", "Se dio de baja al acta [" + coActa + "]");
            return "redirect:../actas/list.do";
        } else {
            return "redirect:../actas/detalle.do?coActa=" + coActa + "&nuPagina=" + nuPagina;
        }
    }

    @RequestMapping("/actas/cancela_dar_baja.do")
    public String cancelaDarBaja(
            @RequestParam(value = "coActa", required = true) String coActa,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            Model model) {
        if (nuPagina != null) return "redirect:../actas/detalle.do?coActa=" + coActa + "&nuPagina=" + nuPagina + "&volver=1";
        else return "redirect:../actas/detalle.do?coActa=" + coActa + "&volver=1";
    }
}
