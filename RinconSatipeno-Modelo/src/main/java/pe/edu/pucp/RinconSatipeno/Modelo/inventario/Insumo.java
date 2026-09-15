package pe.edu.pucp.RinconSatipeno.Modelo.inventario;

public class Insumo {
    //Atributos
    private int idInsumo;
    private String nombre;
    private String unidadMedida;
    private double stockActual;
    private double stockMinimo;


    public Insumo(){
    }

    public Insumo(final Insumo insumo){
        if(insumo==null){
            throw new IllegalArgumentException("insumo no puede ser nulo");
        }

    }
    //Getters y Setters
    public int getId(){
        return idInsumo;
    }
    public void setId(int idInsumo){
        if(idInsumo<0){
            throw new IllegalArgumentException("El idInsumo no puede ser negativo");
        }
        this.idInsumo=idInsumo;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        if(nombre==null || nombre.isEmpty()){
            throw new IllegalArgumentException("nombre no puede ser nulo o vacio");
        }
        this.nombre=nombre;
    }
    public String getUnidadMedida(){
        return unidadMedida;
    }
    public void setUnidadMedida(String unidadMedida){
        if(unidadMedida==null || unidadMedida.isEmpty()){
            throw new IllegalArgumentException("nombre no puede ser nulo o vacio");
        }
        this.unidadMedida=unidadMedida;
    }
    public double getStockActual(){
        return stockActual;
    }
    public void setStockActual(double stockActual){
        if(stockActual<0){
            throw new IllegalArgumentException("El stockActual no puede ser negativo");
        }
        this.stockActual=stockActual;
    }
    public double getStockMinimo(){
        return stockMinimo;
    }
    public void setStockMinimo(double stockMinimo){
        if(stockMinimo<0){
            throw new IllegalArgumentException("El stockMinimo no puede ser negativo");
        }
        this.stockMinimo=stockMinimo;
    }
}
