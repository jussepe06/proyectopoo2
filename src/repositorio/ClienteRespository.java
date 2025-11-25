package repositorio;

import java.sql.Connection; // <-- ¡Nuevo!
import java.sql.DriverManager; // <-- ¡Nuevo!
import java.sql.PreparedStatement; // <-- ¡Nuevo!
import java.sql.ResultSet; // <-- ¡Nuevo!
import java.sql.SQLException; // <-- ¡Nuevo!
import java.sql.Statement; // <-- ¡Nuevo!
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 * PATRÓN REPOSITORY + SINGLETON (Ahora con Base de Datos SQLite)
 *
 * El "Cerebro" se conecta a un archivo de base de datos SQLite.
 * Si el archivo no existe, lo crea.
 * Si la tabla "clientes" no existe, la crea.
 */
public class ClienteRespository {

    private static ClienteRespository instancia;
    
    // Esta es la "dirección" de nuestro diario mágico.
    // "jdbc:sqlite:restaurante.db" significa:
    // "Usa el traductor de SQLite para un archivo llamado restaurante.db"
    // Este archivo aparecerá en la carpeta raíz de tu proyecto.
    private String url = "jdbc:sqlite:restaurante.db";

    // Constructor privado (Singleton)
    private ClienteRespository() {
        // ¡Ya no creamos una ArrayList!
        // En su lugar, nos aseguramos de que la tabla exista.
        crearTablaSiNoExiste();
    }

    // Método de acceso público (Singleton)
    public static ClienteRespository getInstancia() {
        if (instancia == null) {
            instancia = new ClienteRespository();
        }
        return instancia;
    }
    
    // --- Métodos de Conexión y Creación de Tabla ---

    /**
     * Se conecta a la base de datos SQLite.
     * @return un objeto Connection
     */
    private Connection conectar() {
        Connection conn = null;
        try {
            // Esta es la "magia" del driver que añadiste
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    /**
     * Crea la tabla de Clientes si no existe
     */
    private void crearTablaSiNoExiste() {
        // Esta es una sentencia SQL (Lenguaje de Base de Datos)
        String sql = """
                     CREATE TABLE IF NOT EXISTS clientes (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      nombres TEXT NOT NULL,
                      apellidos TEXT NOT NULL,
                      dni TEXT NOT NULL UNIQUE,
                      telefono TEXT
                     );
                     """;
        
        // "try-with-resources" se asegura de cerrar la conexión
        try (Connection conn = this.conectar();
             Statement stmt = conn.createStatement()) {
            // Ejecuta la sentencia SQL
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // --- Métodos del CRUD (¡Ahora hablan SQL!) ---

    // CREATE (C)
    public void agregar(Cliente cliente) {
        String sql = "INSERT INTO clientes(nombres, apellidos, dni, telefono) VALUES(?,?,?,?)";

        try (Connection conn = this.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // "PreparedStatement" es más seguro.
            // Reemplaza los '?' con los datos del cliente
            pstmt.setString(1, cliente.getNombres());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getDni());
            pstmt.setString(4, cliente.getTelefono());
            
            pstmt.executeUpdate(); // Ejecuta la inserción
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // READ (R) - Todos
    public List<Cliente> obtenerTodos() {
        String sql = "SELECT * FROM clientes";
        List<Cliente> clientes = new ArrayList<>(); // <-- La lista se crea aquí
        
        try (Connection conn = this.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // "ResultSet" es la respuesta

            // Itera sobre cada fila de la respuesta
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("dni")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clientes; // Devuelve la lista que acaba de llenar
    }
    
    // (Nota: No implementé obtenerPorId, pero es igual a obtenerTodos con un "WHERE")

    // UPDATE (U)
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombres = ?, apellidos = ?, dni = ?, telefono = ? WHERE id = ?";

        try (Connection conn = this.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getNombres());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getDni());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setInt(5, cliente.getId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // DELETE (D)
    public void eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = this.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}