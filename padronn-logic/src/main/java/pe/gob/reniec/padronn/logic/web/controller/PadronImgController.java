package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.dao.PadronImgDao;
import pe.gob.reniec.padronn.logic.model.PadronImg;
import pe.gob.reniec.padronn.logic.util.ImageUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
* mostrar Padron imagen
* */
@Controller
@RequestMapping("padronimg")
public class PadronImgController extends AbstractBaseController {

	private static final Logger logger = Logger.getLogger(PadronImgController.class);

	@Autowired
	PadronImgDao padronImgDao;

	@Autowired
	ImageUtils imageUtils;

	@RequestMapping(value = "mostrarmenor.do", method = RequestMethod.GET)
	public String doMostrarMenor(@RequestParam String coPadronNominal, HttpServletResponse response) {
		// TODO validar sesión porque este método va a estar fuera de los interceptoress
		// TODO validar subida de archivos
		PadronImg padronImg = padronImgDao.obtenerPadronImg(coPadronNominal);
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			byte[] img = imageUtils.entramarFotoAlt(padronImg.getImgFotoMenor(), coPadronNominal, "RENIEC", "PADRON NOMINAL", simpleDateFormat.format(cal.getTime()));
			response.setContentType("image/jpg");
			//response.setHeader("Content-Disposition","attachment;filename="+nuDni+".jpg");
			response.setDateHeader("Expires", 0);
			response.setContentLength(img.length);
			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(img, 0, img.length);
				out.flush();
				out.close();
				return null;
			} catch(IOException ioe) {
				logger.error("Error: ", ioe);
				response.setStatus(500);
				return null;
			}
		} catch (Exception e) {
			logger.error("Error:", e);
			response.setContentType("image/jpg");
			response.setHeader("Content-Disposition", "attachment;filename=empty.jpg");
			response.setDateHeader("Expires", 0);
			response.setContentLength(0);
			return null;
		}
	}
}
