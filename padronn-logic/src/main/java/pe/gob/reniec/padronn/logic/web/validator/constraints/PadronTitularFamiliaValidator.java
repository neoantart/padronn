package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 03/07/13
 * Time: 04:36 PM
 */
public class PadronTitularFamiliaValidator implements ConstraintValidator<PadronTitularFamilia, PadronNominal> {

	@Override
	public void initialize(PadronTitularFamilia padronJefeFamilia) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
		String tiVinculoJefe=padronNominal.getTiVinculoJefe();
		String coDniJefeFam=padronNominal.getCoDniJefeFam();

		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;

		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();
		boolean isNotMenorEncontrado=coMenorEncontrado.equalsIgnoreCase("0")&&padronNominal.getCoMenorEncontrado()!=null&&!padronNominal.getCoMenorEncontrado().isEmpty();

		//String paterno=padronNominal.getApPrimerJefe();
		//String materno=padronNominal.getApSegundoJefe();

		String nombres=padronNominal.getPrenomJefe();
		boolean existeVinculo=tiVinculoJefe!=null&&!tiVinculoJefe.isEmpty();
		//boolean existeNombre=paterno!=null&&!paterno.isEmpty()&&materno!=null&&!materno.isEmpty()&&nombres!=null&&!nombres.isEmpty();
		boolean existeNombre = nombres!=null&&!nombres.isEmpty();
		//boolean existeDocumento = coDniJefeFam!=null && !coDniJefeFam.isEmpty();

		if (!(isMenorVisitado && isNotMenorEncontrado)){
			if(existeVinculo){
				return existeNombre /*&& existeDocumento*/;
			}else{
				return !(existeNombre /*&& existeDocumento*/);
			}
		}
		return true;
	}
}
