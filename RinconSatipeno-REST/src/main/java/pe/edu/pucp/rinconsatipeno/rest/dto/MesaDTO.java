package pe.edu.pucp.rinconsatipeno.rest.dto;
public class MesaDTO {
    private int id;
    private int numero;
    private int capacidad;
    private String zona;
    private String estado;
    private Integer idMozo;
    private String mozo;
    public MesaDTO() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getIdMozo() { return idMozo; }
    public void setIdMozo(Integer idMozo) { this.idMozo = idMozo; }
    public String getMozo() { return mozo; }
    public void setMozo(String mozo) { this.mozo = mozo; }
}
