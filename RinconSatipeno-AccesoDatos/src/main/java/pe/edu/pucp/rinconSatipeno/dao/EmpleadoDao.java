package pe.edu.pucp.rinconSatipeno.dao;

import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Empleado;

public interface EmpleadoDao extends DaoGeneral<Empleado, Integer>{
    Empleado findyBynombre(String nombreEmpleado);
}
