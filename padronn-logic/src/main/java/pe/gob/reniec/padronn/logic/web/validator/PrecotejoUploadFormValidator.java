package pe.gob.reniec.padronn.logic.web.validator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.reniec.padronn.logic.model.form.PrecotejoUploadForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Clase PrecotejoUploadFormValidator.
 * 1705131601-lmiguelmh resueltos problemas de validación cuando no se envía "nada"
 *
 * @author lmamani
 * @version 1.0.1
 * @since 16/05/13 03:56 PM
 */
@Component
public class PrecotejoUploadFormValidator
        extends AbstractBaseValidator
        implements Validator {

    private static final String[] ACCEPTED_CONTENT_TYPES = new String[]{            /*
			"application/pdf",
			"application/doc",
			"application/msword",
			"application/rtf",
			"text/richtext" ,
			"text/rtf" ,
			"text/plain" ,
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document" ,
			"application/vnd.sun.xml.writer" ,
			"application/x-soffice",
			*/
            //"text/plain",
            //"application/zip",
            "application/vnd.ms-excel"
    };
    /**
     * Archivo: Formato.xlsx Ext:xlsx Tipo: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
     * Archivo: Formato.xlsb Ext:xlsb Tipo: application/vnd.ms-excel.sheet.binary.macroEnabled.12
     * Archivo: Formato1.xls Ext:xls Tipo: application/vnd.ms-excel
     */


    private static final String[] ACCEPTED_EXTENSIONS = new String[]{
			/*
			"doc",
			"pdf",
			"docx",
			"rtf",
			"txt",*/
            //"zip",
            "xls",
            "csv"
    };

    @Override
    public boolean supports(Class<?> clazz) {
        return PrecotejoUploadForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PrecotejoUploadForm form = (PrecotejoUploadForm) object;

        List<MultipartFile> files = form.getFiles();
        //log.debug("En validate..." +files);
        ArrayList<MultipartFile> deleteFiles = new ArrayList<MultipartFile>();
        int i = -1;
        for (MultipartFile file : files) {
            i++;
            if (file == null) {
                // programado para eliminación (ConcurrentModificationException)
                deleteFiles.add(file);

            } else {
                String fileName = file.getOriginalFilename();
                String fileType = file.getContentType();
                String fileExt = FilenameUtils.getExtension(fileName); //http://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java

                if (fileName == null || fileName.isEmpty()) {
                    // programado para eliminación (ConcurrentModificationException)
                    deleteFiles.add(file);

                } else {
//          log.debug("Archivo: '" +fileName+ "' Ext: '" +fileExt+ "' Tipo: '" +fileType+"'");

                    // TODO INVESTIGAR : No se asegura el nombre original del archivo?
                    if (!isValidContentType(fileType) || !isValidExtension(fileExt)) {
                        errors.rejectValue("files[" + (i) + "]", "error.upload.contenttype", fileName);
                        deleteFiles.add(file);
                    }
                }
            }
        }
        // eliminamos
        files.removeAll(deleteFiles);
    }

    private boolean isValidContentType(String contentType) {
        return ArrayUtils.contains(ACCEPTED_CONTENT_TYPES, contentType);
    }

    private boolean isValidExtension(String extension) {
        return ArrayUtils.contains(ACCEPTED_EXTENSIONS, extension);
    }
}
