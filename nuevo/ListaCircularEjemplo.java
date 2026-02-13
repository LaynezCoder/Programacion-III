// lista circular simple, osea que el ultimo apunta al primero

class NodoCircular {

    String dato;
    NodoCircular siguiente;

    public NodoCircular(String dato) {
        this.dato = dato;      // aqui guardo el valor
        this.siguiente = null; // luego lo conecto
    }
}

class ListaCircular {

    private NodoCircular cabeza; // por aqui empiezo

    public ListaCircular() {
        cabeza = null; // al inicio no hay nada
    }

    public void add(String elemento) {
        NodoCircular nuevoNodo = new NodoCircular(elemento); // creo el nodo

        if (cabeza == null) {
            cabeza = nuevoNodo;           // si esta vacia, este es el primero
            nuevoNodo.siguiente = cabeza; // se apunta a si mismo (ciclo)
        } else {
            NodoCircular actual = cabeza; // busco el ultimo
            while (actual.siguiente != cabeza) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo; // pego al final
            nuevoNodo.siguiente = cabeza; // cierro el ciclo
        }

        System.out.println(elemento + " ha sido agregado a la lista circular.");
    }

    public boolean delete(String elemento) {
        if (cabeza == null) {
            System.out.println("La lista circular esta vacia.");
            return false;
        }

        NodoCircular actual = cabeza;
        NodoCircular anterior = null;

        do {
            if (actual.dato.equals(elemento)) {

                // caso especial: si solo hay uno
                if (actual == cabeza && actual.siguiente == cabeza) {
                    cabeza = null; // ya no queda nada
                } else if (actual == cabeza) {
                    // si voy a borrar la cabeza, primero busco el ultimo para que apunte bien
                    NodoCircular temp = cabeza;
                    while (temp.siguiente != cabeza) {
                        temp = temp.siguiente;
                    }
                    temp.siguiente = cabeza.siguiente; // el ultimo apunta al segundo
                    cabeza = cabeza.siguiente;         // muevo la cabeza
                } else {
                    // borrar uno normal (no es cabeza)
                    anterior.siguiente = actual.siguiente;
                }

                System.out.println(elemento + " ha sido eliminado de la lista circular.");
                return true;
            }

            anterior = actual;          // guardo el de atras
            actual = actual.siguiente;  // avanzo
        } while (actual != cabeza);

        System.out.println(elemento + " no se encontro en la lista circular.");
        return false;
    }

    public void imprimir() {
        if (cabeza == null) {
            System.out.println("La lista circular esta vacia.");
            return;
        }

        NodoCircular actual = cabeza;
        System.out.println("Elementos en la lista circular:");

        do {
            System.out.println(actual.dato); // imprimo
            actual = actual.siguiente;       // avanzo
        } while (actual != cabeza);
    }
}

public class ListaCircularEjemplo {

    public static void main(String[] args) {

        ListaCircular listaCircular = new ListaCircular();

        listaCircular.add("Ana");
        listaCircular.add("Luis");
        listaCircular.add("Carlos");

        listaCircular.imprimir();

        listaCircular.delete("Luis");

        listaCircular.imprimir();
    }
}
