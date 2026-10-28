package pe.edu.pucp.rinconsatipeno.modelo.empleados;

import java.time.LocalDate;

public class Administrador extends Empleado {

    public Administrador() {
        super();
    }

    public Administrador(int idEmpleado, String nombre, String telefono, LocalDate fechaDeContratacion, DatosAcceso datosAcceso) {
        super(idEmpleado, nombre, telefono, fechaDeContratacion, datosAcceso);
    }

    public Administrador(final Administrador administrador) {
        super(administrador);
    }


}