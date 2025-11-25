package modelo;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;

    // Constructor vacío
    public Producto() {
    }

    // Constructor para crear productos nuevos (sin ID aún)
    public Producto(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Constructor completo (para leer de la BD)
    public Producto(int id, String nombre, String descripcion, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // --- Getters y Setters ---
    // (Puedes generarlos en NetBeans: clic derecho -> Insert Code...)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return this.nombre + " (S/ " + this.precio + ")";
    }
    
}
