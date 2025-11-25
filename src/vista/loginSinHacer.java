package vista; // Asegúrate que este sea tu paquete

import javax.swing.*; 
import java.awt.event.KeyEvent; 
import Servicios.AuthService; 
import modelo.Usuario; 

// Le decimos a Java que esta clase ES una Ventana (JFrame)
public class loginSinHacer extends javax.swing.JFrame {

    // Llama al "único guardia" (Singleton) en lugar de crear uno nuevo
    private final AuthService auth = AuthService.getInstancia();

    // --- VARIABLES (Declaradas UNA SOLA VEZ) ---
    private JButton btnCancelar;
    private JButton btnIngresar;
    private JLabel lblEstado;
    private JLabel jLabel1; // Usuario
    private JLabel jLabel2; // Contraseña
    private JTextField txtUsuario;
    private JPasswordField txtClave; // Usar JPasswordField es mejor


    public loginSinHacer() {
        // Esta línea llama al método de abajo que construye la ventana
        initComponents(); 
        
        // --- CÓDIGO DE CONFIGURACIÓN ---
        setLocationRelativeTo(null); // Centrar
        lblEstado.setText("Ingrese sus credenciales");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Mi Proyecto Final");
        setResizable(false);

        // ENTER ejecuta el botón Ingresar
        getRootPane().setDefaultButton(btnIngresar);

        // ESC ejecuta Cancelar
        getRootPane().registerKeyboardAction(
                e -> btnCancelar.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Listeners de botones (con expresiones lambda)
        btnIngresar.addActionListener(evt -> btnIngresarActionPerformed(evt));
        btnCancelar.addActionListener(evt -> btnCancelarActionPerformed(evt));
    }

    /**
     * Este método contiene TODO el código de la interfaz visual
     * que construiste "a mano" con GroupLayout.
     */
    private void initComponents() {

        // --- INICIALIZACIÓN DE COMPONENTES ---
        lblEstado = new JLabel("Ingrese sus credenciales", SwingConstants.CENTER);
        jLabel1 = new JLabel("Usuario:");
        jLabel2 = new JLabel("Contraseña:");
        txtUsuario = new JTextField(15); 
        txtClave = new JPasswordField(15); 
        btnIngresar = new JButton("Ingresar");
        btnCancelar = new JButton("Cancelar");

        // --- Layout simple (GroupLayout) ---
        // (Este es tu código, copiado de tus capturas, pero ordenado)
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // --- GRUPO HORIZONTAL ---
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(lblEstado, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtUsuario)
                        .addComponent(txtClave)))
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Centra los botones
                    .addComponent(btnIngresar)
                    .addComponent(btnCancelar)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) // Centra los botones
        );

        // --- GRUPO VERTICAL ---
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(lblEstado)
                .addGap(10) // Espacio
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsuario))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtClave))
                .addGap(15) // Espacio
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIngresar)
                    .addComponent(btnCancelar))
        );

        pack(); // Ajusta el tamaño de la ventana a los componentes
    }

    // --- EVENTO: INGRESAR (Solo UNA VEZ) ---
    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtClave.getPassword()); 

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar usuario y contraseña",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario u = auth.login(user, pass);

        if (u != null) {
            // ¡Éxito!
            JOptionPane.showMessageDialog(this,
                    "Bienvenido, " + u.getNombres() + " (" + u.getRol() + ")");
            
            this.dispose(); // Cierra esta ventana de login

            // --- ¡AQUÍ ABRE TU VENTANA PRINCIPAL! ---
            // (Asegúrate de crear la clase FrmPrincipal en el Paso 3)
            FrmPrincipal ventanaPrincipal = new FrmPrincipal();
            ventanaPrincipal.setVisible(true);

        } else {
            // Fracaso
            JOptionPane.showMessageDialog(this,
                    "Credenciales inválidas",
                    "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            // Limpiamos los campos para que intente de nuevo
            txtUsuario.setText("");
            txtClave.setText("");
            txtUsuario.requestFocus(); // Pone el cursor en el usuario
        }
    }

    // --- EVENTO: CANCELAR (Solo UNA VEZ) ---
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0); // Cierra toda la aplicación
    }

    // --- MÉTODO MAIN (El arranque, Solo UNA VEZ) ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new loginSinHacer().setVisible(true);
        });
    }
}