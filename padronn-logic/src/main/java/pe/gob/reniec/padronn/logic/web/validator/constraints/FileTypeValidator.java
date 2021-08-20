package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 05:51 PM
 */
public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

	FileType fileType;

	@Override
	public void initialize(FileType fileType) {
		this.fileType=fileType;
	}

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile != null && multipartFile.isEmpty())
            return true;
        else
            return fileType.types().length==0||multipartFile==null||multipartFile.getContentType()==null||Arrays.asList(fileType.types()).contains(multipartFile.getContentType());
    }

}