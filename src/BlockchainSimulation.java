
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BlockchainSimulation {

    // esta clase representa una transaccion simple
    // solo guarda quien manda, quien recibe y cuanto
    static class Transaccion {

        private String from;
        private String to;
        private double amount;

        public Transaccion(String from, String to, double amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public String toString() {
            // esto solo sirve para mostrar bonito la transaccion
            return "Transaccion{"
                    + "from='" + from + '\''
                    + ", to='" + to + '\''
                    + ", amount=" + amount
                    + '}';
        }
    }

    // esta clase simula un bloque de la blockchain
    static class Bloque {

        private String hash;
        private String hashAnterior;
        private List<Transaccion> transacciones;
        private long timestamp;

        public Bloque(String hashAnterior) {
            // guardo el hash del bloque anterior para mantener la cadena
            this.hashAnterior = hashAnterior;

            // aqui se van a guardar las transacciones del bloque
            this.transacciones = new ArrayList<>();

            // guardo el tiempo en el que se creo el bloque
            this.timestamp = System.currentTimeMillis();

            // al crear el bloque se calcula su hash
            this.hash = calcularHash();
        }

        public void agregarTransaccion(Transaccion transaccion) {
            // agrega una transaccion al bloque actual
            transacciones.add(transaccion);

            // vuelvo a calcular el hash porque el contenido del bloque cambio
            this.hash = calcularHash();
        }

        public String calcularHash() {
            // esto no es un hash real de blockchain profesional
            // solo es una simulacion sencilla para entender la idea
            return Integer.toHexString((hashAnterior + timestamp + transacciones.toString()).hashCode());
        }

        public String getHash() {
            return hash;
        }

        public String getHashAnterior() {
            return hashAnterior;
        }

        public List<Transaccion> getTransacciones() {
            return transacciones;
        }
    }

    // esta clase representa toda la cadena de bloques
    static class CadenaDeBloques {

        private LinkedList<Bloque> cadena;

        public CadenaDeBloques() {
            // creo la lista enlazada donde se van a guardar los bloques
            this.cadena = new LinkedList<>();

            // creo el bloque genesis
            agregarBloque("0");
        }

        public void agregarBloque(String hashAnterior) {
            // creo un nuevo bloque usando el hash anterior
            Bloque nuevoBloque = new Bloque(hashAnterior);

            // lo agrego al final de la cadena
            cadena.add(nuevoBloque);
        }

        public void agregarTransaccion(Transaccion transaccion) {
            // reviso que la cadena no este vacia
            if (!cadena.isEmpty()) {
                // la transaccion siempre se mete al ultimo bloque
                cadena.getLast().agregarTransaccion(transaccion);
            }
        }

        public void mostrarCadena() {
            // recorro todos los bloques para imprimirlos
            for (Bloque bloque : cadena) {
                System.out.println("Bloque Hash: " + bloque.getHash());
                System.out.println("Hash Anterior: " + bloque.getHashAnterior());
                System.out.println("Transacciones: " + bloque.getTransacciones());
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // creo la blockchain
        CadenaDeBloques blockchain = new CadenaDeBloques();

        int opcion;

        do {
            System.out.println("==== MENU BLOCKCHAIN ====");
            System.out.println("1. Agregar transaccion");
            System.out.println("2. Agregar bloque");
            System.out.println("3. Mostrar blockchain");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpio buffer porque sino falla despues

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese remitente: ");
                    String from = scanner.nextLine();

                    System.out.print("Ingrese destinatario: ");
                    String to = scanner.nextLine();

                    System.out.print("Ingrese monto: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    // creo la transaccion con los datos que metio el usuario
                    Transaccion t = new Transaccion(from, to, amount);

                    // la agrego al ultimo bloque
                    blockchain.agregarTransaccion(t);

                    System.out.println("Transaccion agregada correctamente.");
                    break;

                case 2:
                    // obtengo el hash del ultimo bloque para enlazar el nuevo
                    String hashAnterior = blockchain.cadena.getLast().getHash();
                    blockchain.agregarBloque(hashAnterior);

                    System.out.println("Bloque agregado correctamente.");
                    break;

                case 3:
                    // muestro toda la cadena
                    blockchain.mostrarCadena();
                    break;

                case 4:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opcion no valida");
            }

        } while (opcion != 4);

        scanner.close();
    }
}
