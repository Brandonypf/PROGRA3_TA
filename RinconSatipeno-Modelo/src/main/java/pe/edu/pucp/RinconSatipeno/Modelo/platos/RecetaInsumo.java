package pe.edu.pucp.RinconSatipeno.Modelo.platos;

import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;

public class RecetaInsumo {

    private Receta receta;
    private Insumo insumo;
    private double cantidadRequerida;

    public RecetaInsumo() {

    }

    public RecetaInsumo(final RecetaInsumo recetaInsumo) {
        if (recetaInsumo == null) {
            throw new IllegalArgumentException(
                    "recetaInsumo no puede ser nulo"
            );
        }

        this.receta = recetaInsumo.receta;
        this.insumo = recetaInsumo.insumo;
        this.cantidadRequerida = recetaInsumo.cantidadRequerida;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        if (receta == null) {
            throw new IllegalArgumentException(
                    "receta no puede ser nula"
            );
        }

        this.receta = receta;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        if (insumo == null) {
            throw new IllegalArgumentException(
                    "insumo no puede ser nulo"
            );
        }

        this.insumo = insumo;
    }

    public double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(double cantidadRequerida) {
        if (cantidadRequerida <= 0) {
            throw new IllegalArgumentException(
                    "cantidadRequerida debe ser mayor a cero"
            );
        }

        this.cantidadRequerida = cantidadRequerida;
    }
}