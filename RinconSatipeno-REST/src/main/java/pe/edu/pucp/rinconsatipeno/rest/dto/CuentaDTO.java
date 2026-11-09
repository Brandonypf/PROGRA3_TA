package pe.edu.pucp.rinconsatipeno.rest.dto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class CuentaDTO {
    private int idCuenta;
    private int mesa;
    private String zona;
    private String mozo;
    private String estado;
    private String referencia;
    private String token;
    private String medioPago;
    private String tipoComprobante;
    private String ruc;
    private String razonSocial;
    private String fecha;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal igv = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private List<DetalleConsumoDTO> detalles = new ArrayList<>();
    public CuentaDTO() {}
    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }
    public int getMesa() { return mesa; }
    public void setMesa(int mesa) { this.mesa = mesa; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public String getMozo() { return mozo; }
    public void setMozo(String mozo) { this.mozo = mozo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getReferencia() { return referencia; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getIgv() { return igv; }
    public void setIgv(BigDecimal igv) { this.igv = igv; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<DetalleConsumoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleConsumoDTO> detalles) { this.detalles = detalles; }
}
