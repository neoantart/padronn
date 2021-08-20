package pe.gob.reniec.padronn.logic.web;

public class EntidadesException extends Exception{

    private int codigoError;

    public EntidadesException(int codigoError){
        super();
        this.codigoError=codigoError;
    }

    @Override
    public String getMessage(){
        String mensaje="";

        switch(codigoError){
            case 1:
                mensaje="Seleccione una entidad para logearse.";
                break;
            case 2:
                mensaje="Error desconocido";
                break;
        }

        return mensaje;
    }

}