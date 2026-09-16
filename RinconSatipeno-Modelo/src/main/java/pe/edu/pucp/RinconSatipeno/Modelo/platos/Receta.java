package pe.edu.pucp.RinconSatipeno.Modelo.platos;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Receta{
    //Atributos
    private int idReceta;
    private List<RecetaInsumo> recetasInsumos;
    //Constructor sin parametros
    public Receta(){
        this.recetasInsumos=new ArrayList<>();
    }
    //Constructor copia
    public Receta(final Receta receta){
        if(receta==null){
            throw new IllegalArgumentException("receta no puede ser nula");
        }
        setId(receta.getId());
        setRecetasInsumos(receta.getRecetasInsumos());
    }
    //Getters and Setters
    public int getId(){
        return idReceta;
    }
    public void setId(int idReceta){
        if (idReceta<0){
            throw new IllegalArgumentException("idReceta no puede ser negativo");
        }
        this.idReceta=idReceta;
    }
    public List<RecetaInsumo> getRecetasInsumos(){
        return Collections.unmodifiableList(recetasInsumos);
    }
    public void setRecetasInsumos(List<RecetaInsumo> recetasInsumos){
        if (recetasInsumos == null) {
            throw new IllegalArgumentException("recetasInsumos no puede ser nulo");
        }
        this.recetasInsumos=new ArrayList<>(recetasInsumos);
    }
}
