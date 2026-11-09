package pe.edu.pucp.rinconsatipeno.negocio;

public class BLException extends Exception{
    public BLException(String mensaje){
        super(mensaje);
    }
    public BLException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }

}
