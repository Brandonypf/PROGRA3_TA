package pe.edu.pucp.rinconsatipeno.dao.empleados;

import pe.edu.pucp.rinconsatipeno.dao.RegistroDAO;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.Empleado;

public interface EmpleadoDAO extends RegistroDAO<Empleado, Integer>{
    Empleado findyBynombre(String nombreEmpleado);
}
