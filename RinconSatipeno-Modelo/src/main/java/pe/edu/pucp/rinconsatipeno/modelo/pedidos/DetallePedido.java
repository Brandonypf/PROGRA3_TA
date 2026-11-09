package pe.edu.pucp.rinconsatipeno.modelo.pedidos;

import pe.edu.pucp.rinconsatipeno.modelo.platos.Producto;

public class DetallePedido {
    private int idDetalle;
    private int cantidadPlatos;
    private double precioUnitario;
    private double subtotal;
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(final DetallePedido detallePedido) {
        if (detallePedido == null) {
            throw new IllegalArgumentException("detallePedido no puede ser nulo");
        }
        setIdDetalle(detallePedido.getIdDetalle());
        setCantidadPlatos(detallePedido.getCantidadPlatos());
        setPrecioUnitario(detallePedido.getPrecioUnitario());
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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("precioUnitario no puede ser negativo");
        }
        this.precioUnitario = precioUnitario;
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

    public Producto getPlato() {
        if (producto != null) {
            return new Producto(producto);
        }
        return null;
    }

    public void setPlato(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("plato no puede ser nulo");
        }
        this.producto = new Producto(producto);
    }
}