// COLA (tipo queue), el primero que entra es el primero que sale
// aqui lo hago circular con el % para que no se pierda espacio

class Cola {

    private String[] elementos; // aqui guardo la cola
    private int frente;         // el que se atiende primero
    private int fin;            // donde voy metiendo
    private int tamano;         // cuantos hay

    public Cola(int capacidad) {
        elementos = new String[capacidad];
        frente = 0;
        fin = -1;
        tamano = 0;
    }

    public void enqueue(String elemento) {
        // si aun cabe
        if (tamano < elementos.length) {
            fin = (fin + 1) % elementos.length; // avanzo en circulo
            elementos[fin] = elemento;          // guardo
            tamano++;                           // subo contador
            System.out.println(elemento + " ha sido agregado a la cola.");
        } else {
            System.out.println("La cola esta llena.");
        }
    }

    public String dequeue() {
        // si hay algo
        if (tamano > 0) {
            String elemento = elementos[frente]; // saco el primero
            frente = (frente + 1) % elementos.length; // muevo el frente
            tamano--; // bajo contador
            System.out.println(elemento + " ha sido atendido.");
            return elemento;
        }

        System.out.println("La cola esta vacia.");
        return null;
    }

    public boolean search(String elemento) {
        // recorro segun el tamaño, no segun todo el arreglo
        for (int i = 0; i < tamano; i++) {
            int pos = (frente + i) % elementos.length; // posicion real
            if (elementos[pos].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    public void imprimir() {
        System.out.println("Elementos en la cola:");
        for (int i = 0; i < tamano; i++) {
            int pos = (frente + i) % elementos.length;
            System.out.println(elementos[pos]);
        }
    }
}

public class ColaEjemplo {

    public static void main(String[] args) {

        Cola cola = new Cola(5);

        cola.enqueue("Ana");
        cola.enqueue("Luis");
        cola.enqueue("Carlos");

        cola.imprimir();

        cola.dequeue();

        cola.imprimir();

        System.out.println("Buscar 'Luis': " + cola.search("Luis"));
    }
}
