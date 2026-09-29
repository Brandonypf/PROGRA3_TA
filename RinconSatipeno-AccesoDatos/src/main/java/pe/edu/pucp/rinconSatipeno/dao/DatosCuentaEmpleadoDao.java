package pe.edu.pucp.rinconSatipeno.dao;

import java.sql.SQLException;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.DatosCuentaEmpleado;

public interface DatosCuentaEmpleadoDao extends DaoGeneral<DatosCuentaEmpleado, Integer> {
    DatosCuentaEmpleado findByEmail(String email) throws SQLException;
}