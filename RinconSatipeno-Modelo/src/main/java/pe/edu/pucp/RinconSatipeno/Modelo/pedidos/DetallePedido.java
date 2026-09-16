package pe.edu.pucp.RinconSatipeno.Modelo.pedidos;

import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;

public class DetallePedido {
    private int idDetalle;
    private int cantidadPlatos;
    private double subtotal;
    private Plato plato;

    public DetallePedido() {
    }

    public DetallePedido(final DetallePedido detallePedido) {
        if (detallePedido == null) {
            throw new IllegalArgumentException("detallePedido no puede ser nulo");
        }
        setIdDetalle(detallePedido.getIdDetalle());
        setCantidadPlatos(detallePedido.getCantidadPlatos());
        setSubtotal(detallePedido.getSubtotal());
        setPlato(detallePedido.getPlato());
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        if (idDetalle < 0) {
            throw new IllegalArgumentException("idDetalle no puede ser negativo");
        }
        this.idDetalle = idDetalle;
    }

    public int getCantidadPlatos() {
        return cantidadPlatos;
    }

    public void setCantidadPlatos(int cantidadPlatos) {
        if (cantidadPlatos <= 0) {
            throw new IllegalArgumentException("cantidadPlatos debe ser mayor a cero");
        }
        this.cantidadPlatos = cantidadPlatos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("subtotal no puede ser negativo");
        }
        this.subtotal = subtotal;
    }

    public Plato getPlato() {
        return new Plato(plato);
    }

    public void setPlato(Plato plato) {
        if (plato == null) {
            throw new IllegalArgumentException("plato no puede ser nulo");
        }
        this.plato = new Plato(plato);
    }
}
