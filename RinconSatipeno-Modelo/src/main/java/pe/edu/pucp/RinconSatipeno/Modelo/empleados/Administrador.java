package pe.edu.pucp.RinconSatipeno.Modelo.empleados;

import java.time.LocalDate;

public class Administrador extends Empleado {

    public Administrador() {
        super();
    }

    public Administrador(int idEmpleado, String nombre, String telefono, LocalDate fechaDeContratacion, DatosCuentaEmpleado datosCuentaEmpleado) {
        super(idEmpleado, nombre, telefono, fechaDeContratacion, datosCuentaEmpleado);
    }

    public Administrador(final Administrador administrador) {
        super(administrador);
    }

    public void gestionarUsuarios() {
        // Lógica para gestionar usuarios.
    }

    public void generarReporteVentas() {
        // Lógica para la generación de reportes.
    }
}