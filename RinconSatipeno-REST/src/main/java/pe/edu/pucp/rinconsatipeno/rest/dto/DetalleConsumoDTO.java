package pe.edu.pucp.rinconsatipeno.rest.dto;
import java.math.BigDecimal;
public class DetalleConsumoDTO {
    private String nombre;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal importe;
    public DetalleConsumoDTO() {}
    public DetalleConsumoDTO(String nombre, int cantidad, BigDecimal precioUnitario, BigDecimal importe) {
        this.nombre = nombre; this.cantidad = cantidad; this.precioUnitario = precioUnitario; this.importe = importe;
    }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
}
