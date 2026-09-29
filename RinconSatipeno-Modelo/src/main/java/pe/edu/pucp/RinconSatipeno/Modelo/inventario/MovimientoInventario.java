package pe.edu.pucp.RinconSatipeno.Modelo.inventario;

import java.time.LocalDate;


public class MovimientoInventario {
    //Atributos
    private int idMovimiento;
    private TipoMovimiento tipo;
    private double cantidad;
    private LocalDate fecha;
    private Insumo insumo;

    public MovimientoInventario(){
    }

    public MovimientoInventario(final MovimientoInventario movimientoInventario){
       if(movimientoInventario==null){
           throw new IllegalArgumentException("movimientoInventario no puede ser nulo");
       }
       setId(movimientoInventario.getId());
       setTipo(movimientoInventario.getTipo());
       setCantidad(movimientoInventario.getCantidad());
       setFecha(movimientoInventario.getFecha());
       setInsumo(movimientoInventario.getInsumo());

    }
    //Getters y Setters
    public int getId(){
        return idMovimiento;
    }
    public void setId(int idMovimiento){
        if(idMovimiento<0){
            throw new IllegalArgumentException("El idMovimiento no puede ser negativo");
        }
        this.idMovimiento=idMovimiento;
    }
    public TipoMovimiento getTipo(){
        return tipo;
    }
    public void setTipo(TipoMovimiento tipo){
        this.tipo=tipo;
    }
    public double getCantidad(){
        return cantidad;
    }
    public void setCantidad(double cantidad){
        this.cantidad=cantidad;
    }
    public LocalDate getFecha(){
        return fecha;
    }
    public void setFecha(LocalDate fecha){
        if(fecha ==null){
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        this.fecha=fecha;
    }
    public Insumo getInsumo(){
        return insumo;
    }
    public void setInsumo(Insumo insumo){
        this.insumo=insumo;
    }
}
