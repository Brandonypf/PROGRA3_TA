package pe.edu.pucp.rinconsatipeno.negocio.empleados;

import pe.edu.pucp.rinconsatipeno.negocio.GeneralBL;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;

public interface DatosAccesoBL extends GeneralBL<DatosAcceso, Integer> {
    DatosAcceso findByEmail(String email) throws BLException;
}