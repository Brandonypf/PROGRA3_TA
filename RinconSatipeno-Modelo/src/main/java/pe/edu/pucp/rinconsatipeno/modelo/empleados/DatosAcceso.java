package pe.edu.pucp.rinconsatipeno.modelo.empleados;

public class DatosAcceso {
    private int idCuentaAcceso;
    private String email;
    private String contrasenia;
    private EstadoEmpleado estado;

    public DatosAcceso() {
        this.estado = EstadoEmpleado.ACTIVO;
    }

    public DatosAcceso(int idCuentaAcceso, String email, String contrasenia, EstadoEmpleado estado) {
        this.idCuentaAcceso = idCuentaAcceso;
        this.setEmail(email);
        this.contrasenia = contrasenia;
        this.setEstado(estado);
    }

    public DatosAcceso(final DatosAcceso datoCuentaEmpleado) {
        if (datoCuentaEmpleado == null) {
            throw new IllegalArgumentException("DatosAcceso no puede ser nulo");
        }
        setIdCuentaAcceso(datoCuentaEmpleado.getIdCuentaAcceso());
        setEmail(datoCuentaEmpleado.getEmail());
        setContrasenia(datoCuentaEmpleado.getContrasenia());
        setEstado(datoCuentaEmpleado.getEstado());
    }

    public int getIdCuentaAcceso() {
        return idCuentaAcceso;
    }

    public void setIdCuentaAcceso(int idCuentaAcceso) {
        this.idCuentaAcceso = idCuentaAcceso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email==null){
            throw new IllegalArgumentException("El email debe existir");
        }
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser vacía");
        }
        this.contrasenia = contrasenia;
    }

    public EstadoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpleado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado debe existir");
        }
        this.estado = estado;
    }
}