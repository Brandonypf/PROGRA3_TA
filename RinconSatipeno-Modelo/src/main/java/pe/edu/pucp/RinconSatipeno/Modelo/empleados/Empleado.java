package pe.edu.pucp.RinconSatipeno.Modelo.empleados;

import java.time.LocalDate;

public abstract class Empleado {
    private int idEmpleado;
    private String nombre;
    private String telefono;
    private LocalDate fechaDeContratacion;
    private DatosCuentaEmpleado datosCuentaEmpleado;

    public Empleado() {
        this.datosCuentaEmpleado = new DatosCuentaEmpleado();
    }

    public Empleado(int idEmpleado, String nombre, String telefono, LocalDate fechaDeContratacion, DatosCuentaEmpleado datosCuentaEmpleado) {
        this.idEmpleado = idEmpleado;
        this.setNombre(nombre);
        this.setTelefono(telefono);
        this.fechaDeContratacion = fechaDeContratacion;
        this.datosCuentaEmpleado = datosCuentaEmpleado;
    }

    public Empleado(final Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        }
        setIdEmpleado(empleado.getIdEmpleado());
        setNombre(empleado.getNombre());
        setTelefono(empleado.getTelefono());
        setFechaDeContratacion(empleado.getFechaDeContratacion());
        if (empleado.getDatosCuentaEmpleado() != null) {
            this.datosCuentaEmpleado = new DatosCuentaEmpleado(empleado.getDatosCuentaEmpleado());
        }
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        if (idEmpleado < 0) {
            throw new IllegalArgumentException("El ID del empleado no puede ser negativo");
        }
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un nombre válido");
        }
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().length() < 7) {
            throw new IllegalArgumentException("El número telefónico debe ser válido");
        }
        this.telefono = telefono;
    }

    public LocalDate getFechaDeContratacion() {
        return fechaDeContratacion;
    }

    public void setFechaDeContratacion(LocalDate fechaDeContratacion) {
        this.fechaDeContratacion = fechaDeContratacion;
    }

    public DatosCuentaEmpleado getDatosCuentaEmpleado() {
        return datosCuentaEmpleado;
    }

    public void setDatosCuentaEmpleado(DatosCuentaEmpleado datosCuentaEmpleado) {
        this.datosCuentaEmpleado = datosCuentaEmpleado;
    }
}