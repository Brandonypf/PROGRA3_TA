package pe.edu.pucp.RinconSatipeno.Modelo.empleados;

public class DatosCuentaEmpleado {

    //Clase añadida debido a lo siguiente:

    /*
    5) datos como contraseña, email, teléfono separados en una tabla de datosCuentaEmpleado.
    El mozo y administrador no pueden interactuar bajo la misma tabla de cuenta_insumo.
    En Java deberá ser abstracto. Recomendablemente utilizar el tercer patrón de diseño de tablas.
     */

    String email;
    String telefono;
    String contrasenia;

    public DatosCuentaEmpleado(final DatosCuentaEmpleado datoCuentaEmpleado){
        if(datoCuentaEmpleado==null){
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        }

        this.email = datoCuentaEmpleado.getEmail();
        this.telefono = datoCuentaEmpleado.getTelefono();
        this.contrasenia = datoCuentaEmpleado.getContrasenia();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if(telefono==null){
            throw new IllegalArgumentException("El número telefónico debe tener 9 dígitos y existir");
        }
        this.telefono = telefono;
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
        this.contrasenia = contrasenia;
    }
}
