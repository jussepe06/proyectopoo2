package sin_patron; // puedes cambiar el nombre del paquete si quieres

public class SoftwareSinPatronesRestaurante {

    // ====== Subsistemas (clases simples) ======

    static class Autenticacion {
        boolean login(String usuario, String clave) {
            System.out.println("[Auth] Intentando login de: " + usuario);
            return "cajero".equals(usuario) && "1234".equals(clave);
        }
    }

    static class Catalogo {
        void cargarMenu() {
            System.out.println("[Catálogo] Menú cargado: Burger(20), Papas(8), Gaseosa(6)");
        }
        double precioDe(String item) {
            return switch (item.toLowerCase()) {
                case "burger" -> 20.0;
                case "papas"  -> 8.0;
                case "gaseosa"-> 6.0;
                default       -> 0.0;
            };
        }
    }

    static class Carrito {
        double total = 0.0;
        void agregar(String item, int cantidad, double precioUnit) {
            System.out.println("[Carrito] + " + cantidad + " x " + item + " @ " + precioUnit);
            total += cantidad * precioUnit;
        }
        double total() { return total; }
    }

    static class Inventario {
        boolean hayStock(String item, int cantidad) {
            System.out.println("[Inventario] Verificando stock de " + item + " x" + cantidad);
            return true; // simplificado
        }
        void descontar(String item, int cantidad) {
            System.out.println("[Inventario] Descontando " + cantidad + " de " + item);
        }
    }

    static class PagoGateway {
        boolean cobrar(String metodo, double monto) {
            System.out.println("[Pago] Cobro con " + metodo + " por S/ " + monto);
            return true; // simplificado
        }
    }

    static class CocinaPrinter {
        void imprimirTicketCocina(String detalle) {
            System.out.println("[Cocina] Ticket: " + detalle);
        }
    }

    static class Comprobante {
        void emitirBoleta(String cliente, double total) {
            System.out.println("[Boleta] Cliente: " + cliente + " | Total: S/ " + total);
        }
    }

    static class Notificador {
        void enviarWhatsApp(String telefono, String msg) {
            System.out.println("[Notificación] WhatsApp a " + telefono + ": " + msg);
        }
    }

    // ====== Cliente SIN patrón (todo a mano) ======
    public static void main(String[] args) {
        // El cliente es responsable de orquestarlo TODO (alto acoplamiento)
        Autenticacion auth = new Autenticacion();
        Catalogo catalogo = new Catalogo();
        Carrito carrito = new Carrito();
        Inventario inventario = new Inventario();
        PagoGateway pago = new PagoGateway();
        CocinaPrinter cocina = new CocinaPrinter();
        Comprobante comprobante = new Comprobante();
        Notificador notificador = new Notificador();

        // 1) Login manual
        if (!auth.login("cajero", "1234")) {
            System.out.println("Credenciales inválidas. Abortando.");
            return;
        }

        // 2) Cargar menú
        catalogo.cargarMenu();

        // 3) Armar pedido (el cliente coordina cada paso)
        String cliente = "Gerry";
        String telefono = "+51 999-888-777";

        String item1 = "Burger"; int q1 = 2;
        String item2 = "Papas";  int q2 = 1;
        String item3 = "Gaseosa";int q3 = 2;

        // 3.1) Verificar stock (uno por uno)
        if (!inventario.hayStock(item1, q1) || !inventario.hayStock(item2, q2) || !inventario.hayStock(item3, q3)) {
            System.out.println("Sin stock para alguno de los ítems. Abortando.");
            return;
        }

        // 3.2) Agregar al carrito y calcular totales
        carrito.agregar(item1, q1, catalogo.precioDe(item1));
        carrito.agregar(item2, q2, catalogo.precioDe(item2));
        carrito.agregar(item3, q3, catalogo.precioDe(item3));

        double subtotal = carrito.total();
        double igv = Math.round(subtotal * 0.18 * 100.0) / 100.0;
        double total = Math.round((subtotal + igv) * 100.0) / 100.0;
        System.out.println("[Totales] Subtotal: " + subtotal + " | IGV: " + igv + " | Total: " + total);

        // 3.3) Cobrar
        if (!pago.cobrar("Tarjeta", total)) {
            System.out.println("Pago rechazado. Abortando.");
            return;
        }

        // 3.4) Descontar inventario (el cliente vuelve a acordarse de hacerlo)
        inventario.descontar(item1, q1);
        inventario.descontar(item2, q2);
        inventario.descontar(item3, q3);

        // 3.5) Enviar a cocina (el cliente arma el detalle)
        String detalle = q1 + "x " + item1 + ", " + q2 + "x " + item2 + ", " + q3 + "x " + item3;
        cocina.imprimirTicketCocina(detalle);

        // 3.6) Emitir comprobante y notificar al cliente
        comprobante.emitirBoleta(cliente, total);
        notificador.enviarWhatsApp(telefono, "Tu pedido fue recibido. Total: S/ " + total + ". ¡Gracias!");

        System.out.println("Pedido completado (versión SIN patrón).");
    }
}
