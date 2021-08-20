package pe.gob.reniec.padronn.logic.web.controller;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 26/02/14
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
public class EndpointDocController {
    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public EndpointDocController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @RequestMapping(value="/endpointdoc.do", method=RequestMethod.GET)
    public String show(Model model) {
        model.addAttribute("handlerMethods", this.handlerMapping.getHandlerMethods());
        return "application/endpointdoc";
    }
}
