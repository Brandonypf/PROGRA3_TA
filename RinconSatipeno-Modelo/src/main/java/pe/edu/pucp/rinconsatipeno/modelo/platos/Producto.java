package pe.edu.pucp.rinconsatipeno.modelo.platos;

public class Producto {
    private int idPlato;
    private String nombre;
    private double precio;
    private String categoria;
    private String descripcion;
    private Receta receta;

    public Producto() {
    }

    public Producto(final Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("plato no puede ser nulo");
        }

        setIdPlato(producto.getIdPlato());
        setNombre(producto.getNombre());
        setPrecio(producto.getPrecio());
        setCategoria(producto.getCategoria());
        setDescripcion(producto.getDescripcion());

        // Copia segura de receta
        if (producto.receta != null) {
            this.receta = new Receta(producto.receta);
        } else {
            this.receta = null;
        }
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        if (idPlato < 0) {
            throw new IllegalArgumentException("idPlato no puede ser negativo");
        }
        this.idPlato = idPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("nombre no puede ser nulo o vacío");
        }
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) {
            throw new IllegalArgumentException("categoria no puede ser nula o vacía");
        }
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("descripcion no puede ser nula o vacía");
        }
        this.descripcion = descripcion;
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
}