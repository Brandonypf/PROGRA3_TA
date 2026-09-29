package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio;

import pe.edu.pucp.RinconSatipeno.Modelo.empleados.DatosCuentaEmpleado;

public interface DatosCuentaEmpleadoLN extends lnGeneral<DatosCuentaEmpleado, Integer> {
    DatosCuentaEmpleado findByEmail(String email) throws BLException;
}