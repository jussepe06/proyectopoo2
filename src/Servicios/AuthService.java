package Servicios;

import modelo.Usuario;

public class AuthService {
    
    private static AuthService instancia;
    
    private AuthService(){   
    }
    
    public static AuthService getInstancia(){
        if (instancia == null) {
            instancia = new AuthService();
        }
        return instancia;
    }
    
    /**
     * *Valida las credenciales del usuario.
     * @param usuario
     * @param clave
     * @return 
     */
    
    
    
    public Usuario login(String usuario, String clave) {
        if (usuario == null || clave == null) return null;

        String u = usuario.trim().toLowerCase();
        String p = clave.trim();

        // Demo: credenciales en memoria (sin BD)
        if (("admin".equals(u) || "josue".equals(u)) && "1234".equals(p)) {
            return new Usuario(1, "Josué", "Admin", "admin");
        }
        if ("cajero".equals(u) && "123".equals(p)) {
            return new Usuario(2, "Josué", "Cajero", "cajero");
        }
        return null;
    }
}
