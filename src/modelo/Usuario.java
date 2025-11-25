package modelo;

public class Usuario {
    private int id;
    private String nombres;
    private String rol;
    private String username;

    public Usuario(int id, String nombres, String rol, String username) {
        this.id = id;
        this.nombres = nombres;
        this.rol = rol;
        this.username = username;
    }
    public String getNombres() { return nombres; }
    public String getRol() { return rol; }
    public String getUsername() { return username; }
    public int getId() { return id; }
    
    @Override
public String toString() {
    return nombres + " (" + rol + ")";
}

}

