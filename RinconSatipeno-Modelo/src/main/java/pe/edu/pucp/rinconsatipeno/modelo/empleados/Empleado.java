package pe.edu.pucp.rinconsatipeno.modelo.empleados;

import java.time.LocalDate;

public abstract class Empleado {
    private int idEmpleado;
    private String nombre;
    private String telefono;
    private LocalDate fechaDeContratacion;
    private DatosAcceso datosAcceso;

    public Empleado() {
        this.datosAcceso = new DatosAcceso();
    }

    public Empleado(int idEmpleado, String nombre, String telefono, LocalDate fechaDeContratacion, DatosAcceso datosAcceso) {
        this.idEmpleado = idEmpleado;
        this.setNombre(nombre);
        this.setTelefono(telefono);
        this.fechaDeContratacion = fechaDeContratacion;
        this.datosAcceso = datosAcceso;
    }

    public Empleado(final Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        }
        setIdEmpleado(empleado.getIdEmpleado());
        setNombre(empleado.getNombre());
        setTelefono(empleado.getTelefono());
        setFechaDeContratacion(empleado.getFechaDeContratacion());
        if (empleado.getDatosAcceso() != null) {
            this.datosAcceso = new DatosAcceso(empleado.getDatosAcceso());
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

    public DatosAcceso getDatosAcceso() {
        return datosAcceso;
    }

    public void setDatosAcceso(DatosAcceso datosAcceso) {
        this.datosAcceso = datosAcceso;
    }
}