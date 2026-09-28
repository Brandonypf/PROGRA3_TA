package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio;

public class BLException extends Exception{
    public BLException(String mensaje){
        super(mensaje);
    }
    public BLException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }

}
