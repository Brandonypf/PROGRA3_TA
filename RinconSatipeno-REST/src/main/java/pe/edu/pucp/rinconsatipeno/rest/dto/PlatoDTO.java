package pe.edu.pucp.rinconsatipeno.rest.dto;

import java.math.BigDecimal;

public class PlatoDTO {
    private int id;
    private String nombre;
    private BigDecimal precio;

    public PlatoDTO() {}
    public PlatoDTO(int id, String nombre, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}
