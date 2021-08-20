package pe.gob.reniec.padronn.logic.model.form;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.reniec.padronn.logic.model.AbstractBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase PrecotejoUploadForm.
 *
 * @author lmamani
 * @version 1.0.0
 * @since 16/05/13 04:15 PM
 */
public class PrecotejoUploadForm
		/*extends AbstractBean*/ {

	private List<MultipartFile> files;/* = new ArrayList<MultipartFile>() {{
		add(null);
	}};*/

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

}
