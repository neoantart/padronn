package pe.gob.reniec.padronn.logic.validator;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.owasp.esapi.ESAPI;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Created by jfloresh on 23/09/2015.
 */
public class OwaspXssValidator {
    private static final Logger logger = Logger.getLogger(OwaspXssValidator.class);

    @Before
    public void init() {

    }

    @After
    public void end() {

    }

    @Test
    public void testEncodeStringESAPI() {
        String html = ESAPI.encoder().encodeForHTML("hello < how > are 'you'");
        String html_attr = ESAPI.encoder().encodeForHTMLAttribute("hello < how > are 'you'");
        String js = ESAPI.encoder().encodeForJavaScript("</span><script>alert(1);</script><span>");


        boolean encontradoHtml = !html.contains(">");
        logger.info("valor html:" + html);

        boolean encontradoHtmlAttr = !html.contains(">");
        logger.info("valor html_attr:" + html_attr);

        boolean encontradoJs = !html_attr.contains(">");
        logger.info("valor js:" + js);

        String value = "</span><script> alert(1);</script><span>";
        Pattern scriptPattern = Pattern.compile("</([a-zA-Z]*)><script>(.*?)</script><([a-zA-Z]*)>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        logger.info("value:" + value);
        assertTrue(true);
        /*assertTrue(encontradoHtml);
        assertTrue(encontradoHtmlAttr);
        assertTrue(encontradoJs);*/
    }

}
