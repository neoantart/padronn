package pe.gob.reniec.padronn.logic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.service.ConsultaService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("consulta")
public class ConsultaController
		extends AbstractBaseController {

	@Autowired
	ConsultaService consultaService;

	@RequestMapping(value = "consultaform.do", method = RequestMethod.GET)
	public String doConsultaForm(Model model) {
		model.addAttribute("entidadList", consultaService.getEntidadList());
		return "consulta/consulta";
	}

	@RequestMapping(value = "consulta.do", method = RequestMethod.GET)
	public String doConsulta(@RequestParam String nuEnvio, HttpServletResponse response, Model model) {
		throw new UnsupportedOperationException("TODO");
	}

	@RequestMapping(value = "entidad.do", method = RequestMethod.GET)
	public String doEntidad(@RequestParam String nuEnvio, HttpServletResponse response, Model model) {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}

	@RequestMapping(value = "menor.do", method = RequestMethod.GET)
	public String doMenor(@RequestParam String nuEnvio, HttpServletResponse response, Model model) {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}
}
