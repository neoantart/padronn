package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.apache.log4j.Logger;
import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 11:20 AM
 */
public class PadronApoderadoValidator
		implements ConstraintValidator<PadronApoderado, PadronNominal> {

	@Override
	public void initialize(PadronApoderado padronApoderado) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
		/*
		String tiVinculoJefe=padronNominal.getTiVinculoJefe();
		//Logger logger= Logger.getLogger(this.getClass());
		//logger.info("tiVinculoJefe: "+tiVinculoJefe+" "+padronNominal.getCoDniMadre()+"=="+padronNominal.getCoDniJefeFam());
		if(tiVinculoJefe!=null&&!tiVinculoJefe.isEmpty()&&tiVinculoJefe.equals("1")){
			String coDniMadre=padronNominal.getCoDniMadre();
			return coDniMadre!=null&&coDniMadre.equals(padronNominal.getCoDniJefeFam());
		}else{
			return true;
		}
		*/
		boolean existeMadre = padronNominal.getPrenomMadre()!=null && !padronNominal.getPrenomMadre().isEmpty();
		boolean existeTitular = padronNominal.getPrenomJefe()!=null && !padronNominal.getPrenomJefe().isEmpty();

		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;

		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();
		boolean isNotMenorEncontrado=coMenorEncontrado.equalsIgnoreCase("0")&&padronNominal.getCoMenorEncontrado()!=null&&!padronNominal.getCoMenorEncontrado().isEmpty();

		if (!(isMenorVisitado && isNotMenorEncontrado)){
			if(!existeMadre && !existeTitular) {
				return false;
			}
		}
		return true;
	}
}
