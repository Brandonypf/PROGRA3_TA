package pe.edu.pucp.RinconSatipeno.Modelo.platos;

import pe.edu.pucp.RinconSatipeno.Modelo.inventario;

public class RecetaInsumo {
    private int idRecetaInsumo;
    private Receta receta;
    private Insumo insumo;
    private double cantidadRequerida;

    public RecetaInsumo() {
    }

    public RecetaInsumo(final RecetaInsumo recetaInsumo) {
        if (recetaInsumo == null) {
            throw new IllegalArgumentException("recetaInsumo no puede ser nulo");
        }
        setIdRecetaInsumo(recetaInsumo.getIdRecetaInsumo());
        setReceta(recetaInsumo.getReceta());
        setInsumo(recetaInsumo.getInsumo());
        setCantidadRequerida(recetaInsumo.getCantidadRequerida());
    }

    public int getIdRecetaInsumo() {
        return idRecetaInsumo;
    }

    public void setIdRecetaInsumo(int idRecetaInsumo) {
        if (idRecetaInsumo < 0) {
            throw new IllegalArgumentException("idRecetaInsumo no puede ser negativo");
        }
        this.idRecetaInsumo = idRecetaInsumo;
    }

    public Receta getReceta() {
        return new Receta(receta);
    }

    public void setReceta(Receta receta) {
        if (receta == null) {
            throw new IllegalArgumentException("receta no puede ser nula");
        }
        this.receta = new Receta(receta);
    }

    public Insumo getInsumo() {
        return new Insumo(insumo);
    }

    public void setInsumo(Insumo insumo) {
        if (insumo == null) {
            throw new IllegalArgumentException("insumo no puede ser nulo");
        }
        this.insumo = new Insumo(insumo);
    }

    public double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(double cantidadRequerida) {
        if (cantidadRequerida <= 0) {
            throw new IllegalArgumentException("cantidadRequerida debe ser mayor a cero");
        }
        this.cantidadRequerida = cantidadRequerida;
    }
}
