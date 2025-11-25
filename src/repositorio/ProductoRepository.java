package repositorio;

// Importaciones de SQL (¡importante!)
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto; // <-- ¡Ahora importa Producto!

public class ProductoRepository {
    
    private static ProductoRepository instancia;
    
    // La misma dirección de BD que usó ClienteRepository
    private String url = "jdbc:sqlite:restaurante.db";

    // Constructor privado (Singleton)
    private ProductoRepository() {
        // Al crearse, se asegura que la tabla "productos" exista
        crearTablaSiNoExiste();
    }

    // Método de acceso público (Singleton)
    public static ProductoRepository getInstancia() {
        if (instancia == null) {
            instancia = new ProductoRepository();
        }
        return instancia;
    }
    
    // --- Métodos de Conexión y Creación de Tabla ---

    // Este método es idéntico al de ClienteRepository
    private Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    /**
     * Crea la tabla de Productos si no existe
     */
    private void crearTablaSiNoExiste() {
        // Sentencia SQL para crear la nueva tabla "productos"
        String sql = """
                     CREATE TABLE IF NOT EXISTS productos (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      nombre TEXT NOT NULL,
                      descripcion TEXT,
                      precio REAL NOT NULL
                     );
                     """;
        
        try (Connection conn = this.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- Métodos del CRUD (¡Ahora hablan SQL de Productos!) ---

    // CREATE (C)
    public void agregar(Producto producto) {
        String sql = "INSERT INTO productos(nombre, descripcion, precio) VALUES(?,?,?)";

        try (Connection conn = this.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // READ (R) - Todos
    public List<Producto> obtenerTodos() {
        String sql = "SELECT * FROM productos";
        List<Producto> lista = new ArrayList<>();
        
        try (Connection conn = this.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { 

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    // UPDATE (U)
    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ? WHERE id = ?";

        try (Connection conn = this.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // DELETE (D)
    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = this.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
