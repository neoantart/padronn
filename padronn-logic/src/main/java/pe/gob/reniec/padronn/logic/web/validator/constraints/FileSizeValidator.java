package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.web.filter.Log4jNestedDiagnosticContextFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 06:19 PM
 */
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

	FileSize fileSize;

	@Override
	public void initialize(FileSize fileSize) {
		this.fileSize=fileSize;
	}

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
		return multipartFile==null||(multipartFile.getSize()>=fileSize.min()&&multipartFile.getSize()<=fileSize.max());
	}
}
