package pe.edu.pucp.rinconsatipeno.rest.dto;
public class ComprobanteDTO {
    private String url;
    private String token;
    private CuentaDTO cuenta;
    public ComprobanteDTO() {}
    public ComprobanteDTO(String token, String url, CuentaDTO cuenta) { this.token = token; this.url = url; this.cuenta = cuenta; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public CuentaDTO getCuenta() { return cuenta; }
    public void setCuenta(CuentaDTO cuenta) { this.cuenta = cuenta; }
}
