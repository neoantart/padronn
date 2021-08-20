package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//added
public abstract class AbstractBaseController {
	protected static Logger log = Logger.getLogger(AbstractBaseController.class);

    public Map<String, Object> getErrores(BindingResult result){
        Map<String, Object> errors = new HashMap<String, Object>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for(FieldError fieldError: fieldErrors){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }

}
