package pe.edu.pucp.rinconsatipeno.rest.dto;

public class LoginDTO {
    private String correo;
    private String contrasenia;

    public LoginDTO() {
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}
