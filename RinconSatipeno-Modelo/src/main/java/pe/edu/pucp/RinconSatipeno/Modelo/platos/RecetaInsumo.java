package pe.edu.pucp.RinconSatipeno.Modelo.platos;

import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;

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

        // Copia segura de receta
        if (recetaInsumo.receta != null) {
            this.receta = new Receta(recetaInsumo.receta);
        } else {
            this.receta = null;
        }

        // Copia segura de insumo
        if (recetaInsumo.insumo != null) {
            this.insumo = new Insumo(recetaInsumo.insumo);
        } else {
            this.insumo = null;
        }

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
        if (receta != null) {
            return new Receta(receta);
        } else {
            return null;
        }
    }

    public void setReceta(Receta receta) {
        if (receta == null) {
            this.receta = null;
        } else {
            this.receta = new Receta(receta);
        }
    }

    public Insumo getInsumo() {
        if (insumo != null) {
            return new Insumo(insumo);
        } else {
            return null;
        }
    }

    public void setInsumo(Insumo insumo) {
        if (insumo == null) {
            throw new IllegalArgumentException("insumo no puede ser nulo");
        } else {
            this.insumo = new Insumo(insumo);
        }
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