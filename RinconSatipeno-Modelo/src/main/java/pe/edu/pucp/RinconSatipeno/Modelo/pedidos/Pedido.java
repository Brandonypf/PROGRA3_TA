package pe.edu.pucp.RinconSatipeno.Modelo.pedidos;

import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Pedido {
    private int idPedido;
    private LocalDateTime fechaHora;
    private CuentaConsumo cuentaConsumo;
    private List<DetallePedido> detalles;

    public Pedido() {
        this.detalles = new ArrayList<>();
    }

    public Pedido(final Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("pedido no puede ser nulo");
        }
        setIdPedido(pedido.getIdPedido());
        setFechaHora(pedido.getFechaHora());
        setCuentaConsumo(pedido.getCuentaConsumo());
        setDetalles(pedido.getDetalles());
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        if (idPedido < 0) {
            throw new IllegalArgumentException("idPedido no puede ser negativo");
        }
        this.idPedido = idPedido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("fechaHora no puede ser nula");
        }
        this.fechaHora = fechaHora;
    }

    public CuentaConsumo getCuentaConsumo() {
        if (cuentaConsumo != null) {
            return new CuentaConsumo(cuentaConsumo);
        }
        return null;
    }

    public void setCuentaConsumo(CuentaConsumo cuentaConsumo) {
        if (cuentaConsumo == null) {
            throw new IllegalArgumentException("cuentaConsumo no puede ser nula");
        }
        this.cuentaConsumo = new CuentaConsumo(cuentaConsumo);
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void setDetalles(List<DetallePedido> detalles) {
        if (detalles == null) {
            throw new IllegalArgumentException("detalles no puede ser nulo");
        }
        this.detalles = new ArrayList<>(detalles);
    }
}