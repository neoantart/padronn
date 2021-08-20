package pe.gob.reniec.commons.baseApplication.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.commons.baseApplication.model.ApplicationUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("applicationBase")
public abstract class ApplicationBase {

	protected Logger logger = Logger.getLogger(ApplicationBase.class);

	@Autowired
	protected ApplicationUser applicationUser;

	@RequestMapping(value="/")
	public ModelAndView mainWithoutSession() {
		return getMainWithoutSession();
	}

	@RequestMapping(value="/mainwithsession")
	final public ModelAndView mainWithSession(){
		return getMainWithSession();
	}

	@RequestMapping(value = "mainwithsessionentidad")
	final public ModelAndView mainwithsessionentidad(){
		return getHomeWithSession();
	}

	@RequestMapping(value = "/noSession")
	public ModelAndView invalidSession(HttpServletResponse response) {
		response.setStatus(401);
		return noSessionPage();
	}

	@RequestMapping(value="/login.do")//, method= RequestMethod.POST)
	public ModelAndView loginProcess(HttpServletRequest httpServletRequest) {
		ApplicationUser loggedUser=this.getLoggedUser(httpServletRequest);
		try {
			loggedUser.authenticate(httpServletRequest);
			this.applicationUser.setValues(loggedUser);
			return getHomeWithSession();
		} catch (Exception e) {
			ModelAndView mv = getHomeWithoutSession();
			mv.addObject("errorMessage", e.getMessage());
			return mv;
		}
	}

	@RequestMapping(value = "/logout.do")
	public ModelAndView logout(HttpServletRequest httpServletRequest) {
        try{
            this.applicationUser.invalidateSession();
            return getHomeWithoutSession();
        }
        catch(Exception e){
            return noSessionPage();
        }
	}

	protected abstract ModelAndView getMainWithoutSession();
	protected abstract ModelAndView getMainWithSession();
	protected abstract ModelAndView getHomeWithSession();
	protected abstract ModelAndView getHomeWithoutSession();
	protected abstract ModelAndView noSessionPage();
	protected abstract ApplicationUser getLoggedUser(HttpServletRequest httpServletRequest);
	public abstract long getDiasPassword(String fechaInit);
}
