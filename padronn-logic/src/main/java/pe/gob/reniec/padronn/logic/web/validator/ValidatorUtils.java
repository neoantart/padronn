package pe.gob.reniec.padronn.logic.web.validator;

import edu.vt.middleware.password.*;
import edu.vt.middleware.password.PasswordValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.service.KeysService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Clase ValidatorUtils.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 20/05/13 09:04 AM
 */
@Component
public class ValidatorUtils {

    List<String> tiProSocialKeys;
    List<String> coLenMadreKeys;
    List<String> coGraInstMadreKeys;
    List<String> tiVinculoMadreKeys;
    List<String> tiVinculoJefeKeys;
    List<String> tiSeguroMenorKeys;
    List<String> tiDocIdentidadKeys;
    List<String> coGeneroMenorKeys;

    SimpleDateFormat simpleDateFormat;

    Pattern validNameCharsPattern;
    Pattern invalidNameCharsPattern;
    Pattern invalidFirstNameCharsPattern;

    PasswordValidator passwordValidator;

    @Autowired
    KeysService keysService;

    @Autowired
    PadronProperties padronProperties;

    Logger log = Logger.getLogger(getClass());


    @PostConstruct
    public void start() {

        tiProSocialKeys = keysService.getTiProSocialKeys();
        coLenMadreKeys = keysService.getCoLenMadreKeys();
        coGraInstMadreKeys = keysService.getCoGraInstMadreKeys();
        tiVinculoMadreKeys = keysService.getTiVinculoMadreKeys();
        tiVinculoJefeKeys = keysService.getTiVinculoJefeKeys();
        tiSeguroMenorKeys = keysService.getTiSeguroMenorKeys();
        tiDocIdentidadKeys = keysService.getTiDocMenorKeys();
        coGeneroMenorKeys = keysService.getCoGeneroMenorKeys();

        //System.out.println(tiProSocialKeys);
        //System.out.println(coLenMadreKeys);
        //System.out.println(coGraInstMadreKeys);
        //System.out.println(tiVinculoMadreKeys);
        //System.out.println(tiVinculoJefeKeys);
        //System.out.println(tiSeguroMenorKeys);
        //System.out.println(tiDocIdentidadKeys);
        //System.out.println(coGeneroMenorKeys);

        // fechas
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // nombre
        validNameCharsPattern = Pattern.compile("^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ\\-\\' \\.0123456789]*$", Pattern.CASE_INSENSITIVE);
        invalidNameCharsPattern = Pattern.compile("AAA|BBB|CCC|DDD|EEE|FFF|GGG|III|LLL|MMM|NNN|OOO|PPP|QQ|RRR|SSS|TTT|UUU|ZZZ|ÄÄ|ËË|ÑÑ|ÖÖ|ÜÜ", Pattern.CASE_INSENSITIVE);
        invalidFirstNameCharsPattern = Pattern.compile("[\\-\\' \\.0123456789]");

        // contraseña
        final CharacterCharacteristicsRule characteristicsRule = new CharacterCharacteristicsRule();
        characteristicsRule.getRules().add(new DigitCharacterRule());
        characteristicsRule.getRules().add(new NonAlphanumericCharacterRule());
        characteristicsRule.getRules().add(new UppercaseCharacterRule());
        characteristicsRule.getRules().add(new LowercaseCharacterRule());
        characteristicsRule.setNumberOfCharacteristics(1); // que 3 reglas de arriba se cumplan como mínimo
        List<Rule> ruleList = new ArrayList<Rule>() {{
            //add(new LengthRule(6, 16));
            add(new AlphabeticalSequenceRule(5, true)); // abcde=>error abc=>ok
            add(new NumericalSequenceRule(5, true)); // 12345=>error 12=>ok
            add(new QwertySequenceRule(4, true)); // asdf=>error asd=>ok
            add(new RepeatCharacterRegexRule(4)); // aaaa=>error aa=>ok
            add(new AlphabeticalCharacterRule());
            //add(new UsernameRule(true, true)); // nombre backwards, ignorecase
            add(characteristicsRule);
            // se puede agregar reglas: blancos, diccionario, password history, banned password
        }};
        passwordValidator = new PasswordValidator(ruleList);

//    log.debug("ValidatorUtils iniciado...");
    }


    public String getMessage(Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations) {
        ArrayList<String> messages = new ArrayList<String>();
        for (ConstraintViolation<PrecotejoRegistro> constraintViolation : constraintViolations) {
            messages.add(constraintViolation.getMessage());
        }
        return StringUtils.join(messages.toArray(), ',');
    }


    public String getJsonMessage(Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations) {
        //ArrayList<String> messages = new ArrayList<String>();
        HashMap<String, ArrayList<String>> propertyMessagesMap = new HashMap<String, ArrayList<String>>();
        for (ConstraintViolation<PrecotejoRegistro> constraintViolation : constraintViolations) {
            String propertyName = constraintViolation.getPropertyPath().toString();
            ArrayList<String> messages;
            if ((messages = propertyMessagesMap.get(propertyName)) == null) {
                messages = new ArrayList<String>();
                propertyMessagesMap.put(propertyName, messages);
            }
            messages.add(constraintViolation.getMessage());
        }

        // no es una "solución" adecuada
        ArrayList<String> jsonFields = new ArrayList<String>();
        for (String property : propertyMessagesMap.keySet()) {
            ArrayList<String> messages = propertyMessagesMap.get(property);
            messages.add("");

            StringBuilder sb = new StringBuilder();

            sb.append("\"");
            sb.append(padronProperties.getReadableMappingColumn(property));
            sb.append("\"");

            sb.append(":");

            sb.append("\"");
            sb.append(StringUtils.join(messages, ". "));
            sb.append("\"");

            jsonFields.add(sb.toString());
        }

        StringBuilder jsonSb = new StringBuilder();
        jsonSb.append("{");
        jsonSb.append(StringUtils.join(jsonFields, ", "));
        jsonSb.append("}");

        //return propertyMessagesMap.toString();
        return jsonSb.toString();
    }

    public String getHtmlMessage(Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations) {
        //ArrayList<String> messages = new ArrayList<String>();
        HashMap<String, ArrayList<String>> propertyMessagesMap = new HashMap<String, ArrayList<String>>();
        for (ConstraintViolation<PrecotejoRegistro> constraintViolation : constraintViolations) {
            String propertyName = constraintViolation.getPropertyPath().toString();
            ArrayList<String> messages;
            if ((messages = propertyMessagesMap.get(propertyName)) == null) {
                messages = new ArrayList<String>();
                propertyMessagesMap.put(propertyName, messages);
            }
            messages.add(constraintViolation.getMessage());
        }

        // no es una "solución" adecuada
        ArrayList<String> htmlLi = new ArrayList<String>();
        for (String property : propertyMessagesMap.keySet()) {
            ArrayList<String> messages = propertyMessagesMap.get(property);
            messages.add("");

            StringBuilder sb = new StringBuilder();

            sb.append("<li>");
            sb.append("<strong>");
            sb.append(padronProperties.getReadableMappingColumn(property));
            sb.append(": ");
            sb.append("</strong>");
            sb.append(StringUtils.join(messages, ". "));
            sb.append("</li>");

            htmlLi.add(sb.toString());
        }

        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<small>");
        htmlSb.append("<ul>");
        htmlSb.append(StringUtils.join(htmlLi, ""));
        htmlSb.append("</ul>");
        htmlSb.append("</small>");

        //return propertyMessagesMap.toString();
        return htmlSb.toString();
    }

    public String getTextMessage(Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations) {
        //ArrayList<String> messages = new ArrayList<String>();
        HashMap<String, ArrayList<String>> propertyMessagesMap = new HashMap<String, ArrayList<String>>();
        for (ConstraintViolation<PrecotejoRegistro> constraintViolation : constraintViolations) {
            String propertyName = constraintViolation.getPropertyPath().toString();
            ArrayList<String> messages;
            if ((messages = propertyMessagesMap.get(propertyName)) == null) {
                messages = new ArrayList<String>();
                propertyMessagesMap.put(propertyName, messages);
            }
            messages.add(constraintViolation.getMessage());
        }

        // no es una "solución" adecuada
        ArrayList<String> textItem = new ArrayList<String>();
        for (String property : propertyMessagesMap.keySet()) {
            ArrayList<String> messages = propertyMessagesMap.get(property);
            messages.add("");

            StringBuilder sb = new StringBuilder();

            sb.append(padronProperties.getReadableMappingColumn(property));
            sb.append(": ");
            sb.append(StringUtils.join(messages, ". "));

            textItem.add(sb.toString());
        }

        StringBuilder textSb = new StringBuilder();
        textSb.append(StringUtils.join(textItem, "<br/>"));

        //return propertyMessagesMap.toString();
        return textSb.toString();
    }


    /**
     * Mensaje separado por / y | : Ej. campo/descripcion|campo/descripcion. No podemos usar ni html, ni json porque
     * no garantizamos que el mensaje se escriba completo (500chars).
     * El campo se inserta con su nombre de bd, traducirlo con padronProperties.getReadableMappingColumn()
     *
     * @param constraintViolations
     * @return
     */
    public String getSpecialMessage(Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations) {

        HashMap<String, ArrayList<String>> propertyMessagesMap = new HashMap<String, ArrayList<String>>();
        for (ConstraintViolation<PrecotejoRegistro> constraintViolation : constraintViolations) {
            String propertyName = constraintViolation.getPropertyPath().toString();
            ArrayList<String> messages;
            if ((messages = propertyMessagesMap.get(propertyName)) == null) {
                messages = new ArrayList<String>();
                propertyMessagesMap.put(propertyName, messages);
            }
            messages.add(constraintViolation.getMessage());
        }

        // no es una "solución" adecuada para separar adecuadamente las líneas
        ArrayList<String> textItem = new ArrayList<String>();
        for (String property : propertyMessagesMap.keySet()) {
            ArrayList<String> messages = propertyMessagesMap.get(property);
            messages.add("");

            StringBuilder sb = new StringBuilder();
            //sb.append(padronProperties.getReadableMappingColumn(property));
            sb.append(property);
            sb.append(padronProperties.MAPCOLS_SEPARADOR_DEFINICION);
            sb.append(StringUtils.join(messages, ". "));
            textItem.add(sb.toString());
        }

        StringBuilder message = new StringBuilder();
        message.append(StringUtils.join(textItem, padronProperties.MAPCOLS_SEPARADOR_LINEA));
        return message.toString();
    }


    public List<String> getTiProSocialKeys() {
        return tiProSocialKeys;
    }

    public List<String> getCoLenMadreKeys() {
        return coLenMadreKeys;
    }

    public List<String> getCoGraInstMadreKeys() {
        return coGraInstMadreKeys;
    }

    public List<String> getTiVinculoMadreKeys() {
        return tiVinculoMadreKeys;
    }

    public List<String> getTiVinculoJefeKeys() {
        return tiVinculoJefeKeys;
    }

    public List<String> getTiSeguroMenorKeys() {
        return tiSeguroMenorKeys;
    }

    public List<String> getTiDocIdentidadKeys() {
        return tiDocIdentidadKeys;
    }

    public List<String> getCoGeneroMenorKeys() {
        return coGeneroMenorKeys;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public Pattern getValidNameCharsPattern() {
        return validNameCharsPattern;
    }

    public Pattern getInvalidNameCharsPattern() {
        return invalidNameCharsPattern;
    }

    public Pattern getInvalidFirstNameCharsPattern() {
        return invalidFirstNameCharsPattern;
    }

}
