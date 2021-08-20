package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameCharactersValidator implements ConstraintValidator<NameCharacters, String> {

	/**
	 * Caracteres válidos de acuerdo a GETR_PARAMETRO.NO_PARAMETRO=T_NOMBRES
	 * ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ-' .0123456789;
	 */
	String validCharsPattern = "[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ0123456789\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-\' \\.]+";

	/**
	 * La cadena del nombre no puede empezar en
	 * -' .0123456789
	 */
	String noFirstChars = "-' .0123456789";

	/**
	 * La cadena de caracteres no puede tener dos espacios consecutivos
	 */
	String doubleSpace = "  ";

	/**
	 * La cadena de caracteres no puede contener las siguientes secuencias de acuerdo a la configuración en CTVW_GRUP_CARAC_NOPERMI
	 * AAA|BBB|CCC|DDD|EEE|FFF|GGG|HH|III|JJ|KK|LLL|MMM|NNN|OOO|PPP|QQ|RRR|SSS|TTT|UUU|VV|WW|XX|YY|ZZZ|ÄÄ|ËË|ÑÑ|ÖÖ|ÜÜ
	 */

	//String invalidCharGroupPattern = ".*(AAA|BBB|CCC|DDD|EEE|FFF|GGG|HH|III|JJ|KK|LLL|MMM|NNN|OOO|PPP|QQ|RRR|SSS|TTT|UUU|VV|WW|XX|YY|ZZZ|ÄÄ|ËË|ÑÑ|ÖÖ|ÜÜ).*";

	@Override
	public void initialize(NameCharacters nameCharacters) {
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

		if (s!=null&&s.isEmpty() ||
				(s!=null && s.matches(validCharsPattern) && !noFirstChars.contains(s.substring(0, 1)) && !s.contains(doubleSpace)/*&&!s.matches(invalidCharGroupPattern)*/ )
				) {
			return true;
		}
		return false;
	}
}