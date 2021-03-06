package pe.gob.reniec.padronn.logic.web.view;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 11/03/14
 * Gracias a: https://jira.spring.io/browse/SPR-6898
 * Time: 04:42 PM
 * To change this template use File | Settings | File Templates.
 */

// extraido  para soporte XSSFWorkbook  https://jira.spring.io/browse/SPR-6898

public abstract class AbstractPOIExcelView extends AbstractView {

    /** The content type for an Excel response */
    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";

    /**
     * Default Constructor. Sets the content type of the view for excel files.
     */
    public AbstractPOIExcelView() {
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    /**
     * Renders the Excel view, given the specified model.
     */
    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {

        SXSSFWorkbook workbook = (SXSSFWorkbook) createWorkbook(request);

        if (workbook instanceof SXSSFWorkbook) {
            setContentType(CONTENT_TYPE_XLSX);
        } else {
            setContentType(CONTENT_TYPE_XLS);
        }

        buildExcelDocument(model, workbook, request, response);

        // Set the content type.
        response.setContentType(getContentType());

        // Flush byte array to servlet output stream.
        ServletOutputStream out = response.getOutputStream();
        out.flush();

        workbook.write(out);

        out.flush();
        workbook.dispose();

        // Don't close the stream - we didn't open it, so let the container
        // handle it.
        // http://stackoverflow.com/questions/1829784/should-i-close-the-servlet-outputstream
    }

    /**
     * Subclasses must implement this method to create an Excel Workbook.
     * HSSFWorkbook and XSSFWorkbook are both possible formats.
     */
    protected abstract Workbook createWorkbook(HttpServletRequest request);

    /**
     * Subclasses must implement this method to create an Excel HSSFWorkbook
     * document, given the model.
     *
     * @param model
     *            the model Map
     * @param workbook
     *            the Excel workbook to complete
     * @param request
     *            in case we need locale etc. Shouldn't look at attributes.
     * @param response
     *            in case we need to set cookies. Shouldn't write to it.
     */
    protected abstract void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception;

}



