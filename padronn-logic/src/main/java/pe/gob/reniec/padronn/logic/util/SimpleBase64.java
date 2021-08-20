package pe.gob.reniec.padronn.logic.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by jfloresh on 27/01/2015.
 */
public class SimpleBase64 {

    public static String encodeBase64(String string) {
        byte[] bytesEncoded =  Base64.encodeBase64(string.getBytes());
        return new String(bytesEncoded);
    }

    public static String decodeBase64(String string){
        return new String(Base64.decodeBase64(string.getBytes()));
    }

    public static void main(String[] args){

        /*String encode = Base64.encodeBase64String("CHIÑAMA".getBytes());
        System.out.println(encode);*/

        String cadenaHexAplicativo = "6c53452b6d6b41382f7834546a76534d474148527950553147536e2b4e4d446858616e6149333677705736526e475359394e6148756d6c69562f755a3671664f";

        String decode = SimpleBase64.decodeBase64(cadenaHexAplicativo);

        System.out.println(decode);
    }
}