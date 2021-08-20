package pe.gob.reniec.padronn.logic.util;

import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by JFLORESH on 28/04/2014.
 */
public class GetCurrentDateTime {
    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
