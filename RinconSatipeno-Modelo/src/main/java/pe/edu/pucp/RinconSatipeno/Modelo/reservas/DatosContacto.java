package pe.edu.pucp.RinconSatipeno.Modelo.reservas;

public class DatosContacto {
    private int idContacto;
    private String nombre;
    private String telefono;
    private String correo;

    public DatosContacto() {
    }

    public DatosContacto(final DatosContacto datos) {
        if (datos == null) {
            throw new IllegalArgumentException("datosContacto no puede ser nulo");
        }
        setIdContacto(datos.getIdContacto());
        setNombre(datos.getNombre());
        setTelefono(datos.getTelefono());
        setCorreo(datos.getCorreo());
    }

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        if (idContacto < 0) {
            throw new IllegalArgumentException("idContacto no puede ser negativo");
        }
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("nombre no puede ser nulo o vacío");
        }
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("telefono no puede ser nulo o vacío");
        }
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty() || !correo.contains("@")) {
            throw new IllegalArgumentException("correo no puede ser nulo, vacío o inválido");
        }
        this.correo = correo;
    }
}