package pe.edu.pucp.rinconsatipeno.modelo.empleados;

import pe.edu.pucp.rinconsatipeno.modelo.mesas.Zona;

import java.time.LocalDate;

public class Mozo extends Empleado {
    private Turno turno;
    private Zona zona;

    public Mozo() {
        super();
    }

    public Mozo(int idEmpleado, String nombre, String telefono, LocalDate fechaDeContratacion, DatosAcceso datosAcceso, Turno turno, Zona zona) {
        super(idEmpleado, nombre, telefono, fechaDeContratacion, datosAcceso);
        this.turno = turno;
        this.zona = zona;
    }

    public Mozo(final Mozo mozo) {
        super(mozo);
        if (mozo != null) {
            this.turno = mozo.getTurno();
            this.zona = mozo.getZona();
        }
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void tomarPedido() {
        // Lógica para tomar pedidos.
    }

    public void cambiarEstadoMesa() {
        // Lógica para cambiar estado de mesa.
    }

    public void entregarPedido() {
        // Lógica para entregar pedido.
    }
}