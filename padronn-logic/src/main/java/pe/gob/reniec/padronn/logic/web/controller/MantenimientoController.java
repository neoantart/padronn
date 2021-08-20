package pe.gob.reniec.padronn.logic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.model.Usuario;


@Controller
@RequestMapping
public class MantenimientoController {

	@Autowired
	Usuario usuario;

	@RequestMapping("/mantenimiento/maestros.do")
	public String maestros(Model model){
		model.addAttribute("esMinsa",usuario.getEsMinsa());
		return "mantenimiento/maestros";
	}

	@RequestMapping("/mantenimiento/configuracion.do")
	public String configuracion(){
		return "mantenimiento/configuracion";
	}

    @RequestMapping("ayuda.do")
    public String ayuda(){
        return "mantenimiento/ayuda";
    }

}
