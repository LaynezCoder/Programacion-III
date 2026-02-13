// lista doble enlazada bien basica, hecha con nodos

class Nodo {

    String dato;
    Nodo siguiente;
    Nodo anterior;

    public Nodo(String dato) {
        this.dato = dato;       // guardo el texto
        this.siguiente = null;  // aun no apunta a nada
        this.anterior = null;   // ni para atras
    }
}

class ListaDobleEnlazada {

    private Nodo cabeza;  // primer nodo
    private Nodo cola;    // ultimo nodo
    private int tamano;   // cuantos llevo

    public ListaDobleEnlazada() {
        cabeza = null; // arranca vacia
        cola = null;
        tamano = 0;
    }

    public void add(String elemento) {
        Nodo nuevoNodo = new Nodo(elemento); // creo el nodo con el dato

        if (cabeza == null) { // si esta vacia
            cabeza = nuevoNodo; // el primero es cabeza
            cola = nuevoNodo;   // y tambien es cola
        } else {
            cola.siguiente = nuevoNodo; // lo pego al final
            nuevoNodo.anterior = cola;  // lo conecto para atras
            cola = nuevoNodo;           // actualizo la cola
        }

        tamano++; // sumo uno
        System.out.println(elemento + " ha sido agregado a la lista.");
    }

    public boolean delete(String elemento) {
        Nodo actual = cabeza; // empiezo desde el inicio

        while (actual != null) {
            if (actual.dato.equals(elemento)) {

                if (actual.anterior != null) {
                    actual.anterior.siguiente = actual.siguiente; // salto el nodo
                } else {
                    cabeza = actual.siguiente; // era el primero
                }

                if (actual.siguiente != null) {
                    actual.siguiente.anterior = actual.anterior; // acomodo para atras
                } else {
                    cola = actual.anterior; // era el ultimo
                }

                tamano--; // bajo uno
                System.out.println(elemento + " ha sido eliminado de la lista.");
                return true;
            }

            actual = actual.siguiente; // sigo caminando
        }

        System.out.println(elemento + " no se encontro en la lista.");
        return false;
    }

    public void imprimir() {
        Nodo actual = cabeza; // arranco desde cabeza
        System.out.println("Elementos en la lista:");

        while (actual != null) {
            System.out.println(actual.dato); // imprimo el dato
            actual = actual.siguiente;       // avanzo
        }
    }
}

public class ListaDobleEjemplo {

    public static void main(String[] args) {

        ListaDobleEnlazada lista = new ListaDobleEnlazada();

        lista.add("Juan");
        lista.add("Maria");
        lista.add("Pedro");

        lista.imprimir();

        lista.delete("Maria");

        lista.imprimir();
    }
}
