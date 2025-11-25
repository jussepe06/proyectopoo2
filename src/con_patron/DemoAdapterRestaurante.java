package con_patron;

/**
 * Demostración del patrón ADAPTER en un sistema de ventas para restaurante.
 * Unificamos diferentes pasarelas de pago (con interfaces incompatibles) 
 * detrás de una interfaz objetivo (Pago).
 */
public class DemoAdapterRestaurante {

    // ========= Interfaz objetivo (Target) =========
    interface Pago {
        boolean cobrar(String cliente, double monto);
    }

    // ========= Subsistema existente (compatible) =========
    static class PagoInterno {
        public boolean procesar(String usuario, double monto) {
            System.out.println("[Interno] Cobro a " + usuario + " por S/ " + monto);
            return true;
        }
    }

    // ========= Proveedor externo #1 (API incompatible) =========
    static class PayPalAPI {
        // Firma distinta: email + amount en USD
        public String charge(String email, double amountUSD) {
            System.out.println("[PayPalAPI] Charging " + email + " USD $" + amountUSD);
            return "OK"; // podría ser "OK" o "ERROR"
        }
    }

    // ========= Proveedor externo #2 (API incompatible) =========
    static class YapeSDK {
        // Firma distinta: nro celular + monto en soles + token de comercio
        public int pagar(String celular, double montoSoles, String comercioToken) {
            System.out.println("[YapeSDK] Pago desde " + celular + " a comercio " + comercioToken + " S/ " + montoSoles);
            return 200; // 200=éxito, otros=código de error
        }
    }

    // ========= ADAPTERS =========
    // Adapter del sistema interno (ya compatible pero lo adaptamos para uniformidad)
    static class PagoInternoAdapter implements Pago {
        private final PagoInterno interno = new PagoInterno();

        @Override
        public boolean cobrar(String cliente, double monto) {
            return interno.procesar(cliente, monto);
        }
    }

    // Adapter de PayPal
    static class PayPalAdapter implements Pago {
        private final PayPalAPI api;
        private final double TIPO_CAMBIO_USD; // ejemplo simple

        public PayPalAdapter(PayPalAPI api, double tipoCambioUSD) {
            this.api = api;
            this.TIPO_CAMBIO_USD = tipoCambioUSD;
        }

        @Override
        public boolean cobrar(String cliente, double montoSoles) {
            // Convertir soles -> USD (ejemplo)
            double usd = Math.round((montoSoles / TIPO_CAMBIO_USD) * 100.0) / 100.0;
            String resp = api.charge(cliente + "@mail.com", usd);
            return "OK".equalsIgnoreCase(resp);
        }
    }

    // Adapter de Yape
    static class YapeAdapter implements Pago {
        private final YapeSDK sdk;
        private final String tokenComercio;

        public YapeAdapter(YapeSDK sdk, String tokenComercio) {
            this.sdk = sdk;
            this.tokenComercio = tokenComercio;
        }

        @Override
        public boolean cobrar(String cliente, double monto) {
            // Cliente se interpreta como celular en este proveedor
            int code = sdk.pagar(cliente, monto, tokenComercio);
            return code == 200;
        }
    }

    // ========= Cliente (usa solamente la interfaz Pago) =========
    static class Caja {
        private Pago medioPago;

        public void setMedioPago(Pago medioPago) {
            this.medioPago = medioPago;
        }

        public void cobrarPedido(String identificadorCliente, double total) {
            if (medioPago == null) {
                throw new IllegalStateException("No hay medio de pago configurado.");
            }
            System.out.println("[Caja] Total a cobrar: S/ " + total);
            boolean ok = medioPago.cobrar(identificadorCliente, total);
            System.out.println(ok ? "[Caja] Pago aprobado ✅" : "[Caja] Pago rechazado ❌");
        }
    }

    // ========= Demo =========
    public static void main(String[] args) {
        Caja caja = new Caja();

        // 1) Pago interno (usuario del sistema)
        caja.setMedioPago(new PagoInternoAdapter());
        caja.cobrarPedido("cajero_gerry", 54.00);

        System.out.println("-----");

        // 2) PayPal (cliente identificado por email) a través de ADAPTER
        PayPalAPI paypal = new PayPalAPI();
        Pago paypalAdapter = new PayPalAdapter(paypal, 3.80); // tipo de cambio de ejemplo
        caja.setMedioPago(paypalAdapter);
        caja.cobrarPedido("cliente.paypal", 54.00);

        System.out.println("-----");

        // 3) Yape (cliente identificado por teléfono) a través de ADAPTER
        YapeSDK yape = new YapeSDK();
        Pago yapeAdapter = new YapeAdapter(yape, "TOKEN_COMERCIO_ABC123");
        caja.setMedioPago(yapeAdapter);
        caja.cobrarPedido("999888777", 54.00);

        System.out.println("----- FIN DEMO ADAPTER -----");
    }
}

