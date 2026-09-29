package pe.edu.pucp.RinconSatipeno.Modelo.empleados;

import java.time.LocalDate;

public class Empleado {
    int idEmpleado;
    String nombre; //Eliminar de aquí y ponerla en DatosCuentaEmpleado.
    EstadoEmpleado estado;
    Turno turno;
    LocalDate fechaDeContratacion;
    DatosCuentaEmpleado dataCuentaEmpleado;

    public Empleado(){
    }
    public Empleado(final Empleado empleado){
        if(empleado==null){
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        }
        this.idEmpleado = empleado.getIdEmpleado();
        this.nombre = empleado.getNombre();
        this.estado = empleado.getEstado();
        this.turno = empleado.getTurno();
        this.fechaDeContratacion = empleado.getFechaDeContratacion();
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        if(idEmpleado<0){
            throw new IllegalArgumentException("El ID del empleado no puede ser negativo");
        }
        this.idEmpleado = idEmpleado;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre==null){
            throw new IllegalArgumentException("Debe ingresar un nombre");
        }
        this.nombre = nombre;
    }

    public EstadoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpleado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado debe existir");
        }
        this.estado = estado;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public LocalDate getFechaDeContratacion() {
        return fechaDeContratacion;
    }

    public void setFechaDeContratacion(LocalDate fechaDeContratacion) {
        this.fechaDeContratacion = fechaDeContratacion;
    }
}


