package pe.gob.reniec.commons.baseApplication.model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dyn
 * Date: 31/01/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ApplicationUser {

	public boolean isValidForSession();

	public boolean isRememberSession();

	public void invalidateSession();

	public void authenticate(HttpServletRequest httpServletRequest) throws Exception;

	public String getUserName();

	public String getFullName();

	public String getInvalidSessionDispatcher();

	public void setValues(ApplicationUser applicationUser);

    public List<String> getAuthorities();

    public boolean isSelectedEntity();

    public boolean isReloadedPage();

}
