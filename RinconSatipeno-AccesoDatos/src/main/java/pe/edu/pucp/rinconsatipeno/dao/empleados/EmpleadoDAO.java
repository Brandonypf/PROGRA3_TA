package pe.edu.pucp.rinconsatipeno.dao.empleados;

import pe.edu.pucp.rinconsatipeno.dao.GeneralDAO;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.Empleado;

public interface EmpleadoDAO extends GeneralDAO<Empleado, Integer>{
    Empleado findyBynombre(String nombreEmpleado);
}
