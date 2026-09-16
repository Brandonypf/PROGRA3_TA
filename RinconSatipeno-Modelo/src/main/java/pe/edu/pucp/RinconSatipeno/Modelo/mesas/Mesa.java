package pe.edu.pucp.RinconSatipeno.Modelo.mesas;

public class Mesa {
    private int idMesa;
    private int numero;
    private int capacidad;
    private EstadoMesa estado;

    public Mesa(){
    }
    public Mesa(final Mesa mesa){
        if (mesa==null){
            throw new IllegalArgumentException("mesa no puede ser nula");
        }
        setId(mesa.getId());
        setNumero(mesa.getNumero());
        setCapacidad(mesa.getCapacidad());
        setEstado(mesa.getEstado());
    }
    public int getId(){
        return idMesa;
    }
    public void setId(int idMesa){
        if(idMesa<0){
            throw new IllegalArgumentException("idMesa no puede ser negativo");
        }
        this.idMesa=idMesa;
    }
    public int getNumero(){
        return numero;
    }
    public void setNumero(int numero){
        if(numero<=0){
            throw new IllegalArgumentException("numero debe ser mayor a cero");
        }
        this.numero=numero;
    }
    public int getCapacidad(){
        return capacidad;
    }
    public void setCapacidad(int capacidad){
        if(capacidad<=0){
            throw new IllegalArgumentException("capacidad debe ser mayor a cero");
        }
        this.capacidad=capacidad;
    }
    public EstadoMesa getEstado(){
        return estado;
    }
    public void setEstado(EstadoMesa estado){
        if (estado==null){
            throw new IllegalArgumentException("estado no puede ser nulo");
        }
        this.estado=estado;
    }
}
