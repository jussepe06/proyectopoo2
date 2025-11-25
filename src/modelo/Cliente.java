package modelo;

public class Cliente {
    
    private int id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String dni; // O RUC, o lo que necesites

    // Constructor vacío (importante para algunas cosas)
    public Cliente() {
    }

    // Constructor para crear clientes nuevos (sin ID aún)
    public Cliente(String nombres, String apellidos, String telefono, String dni) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.dni = dni;
    }
    
    // Constructor completo (para cuando lo leemos de la "BD")
    public Cliente(int id, String nombres, String apellidos, String telefono, String dni) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.dni = dni;
    }

    // --- Getters y Setters ---
    // (Puedes generarlos en NetBeans: clic derecho -> Insert Code... -> Getter and Setter...)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + '}';
    }
}
