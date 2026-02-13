// Una lista simple usando un arreglo de Strings.
// La idea es poder agregar, eliminar, buscar e imprimir cosas sin usar ArrayList.

class ListaSimple {

    private String[] elementos; // aqui guardo los nombres o cosas que meto a la lista
    private int tamano;         // cuantos elementos llevo metidos

    // constructor: yo le digo que tan grande quiero la lista (capacidad)
    public ListaSimple(int capacidad) {
        elementos = new String[capacidad];
        tamano = 0; // al inicio esta vacia
    }

    // agregar un elemento al final si aun hay espacio
    public void add(String elemento) {
        // si todavia caben cosas, lo meto
        if (tamano < elementos.length) {
            elementos[tamano] = elemento;
            tamano++; // subo el contador porque ya meti uno mas
            System.out.println(elemento + " ha sido agregado a la lista.");
        } else {
            // si no cabe, pues ya estuvo jaja
            System.out.println("La lista esta llena.");
        }
    }

    // buscar si un elemento existe en la lista
    public boolean buscar(String elemento) {
        // voy revisando uno por uno
        for (int i = 0; i < tamano; i++) {
            if (elementos[i].equals(elemento)) {
                System.out.println(elemento + " si esta en la lista.");
                return true;
            }
        }

        // si llegue aqui es porque no lo encontre
        System.out.println(elemento + " no se encontro en la lista.");
        return false;
    }

    // eliminar un elemento (si existe) y correr los demas para que no quede vacio
    public boolean delete(String elemento) {

        // primero busco en que posicion esta
        for (int i = 0; i < tamano; i++) {
            if (elementos[i].equals(elemento)) {

                // aqui corro todo a la izquierda desde donde borre
                for (int j = i; j < tamano - 1; j++) {
                    elementos[j] = elementos[j + 1];
                }

                // limpio la ultima posicion que quedo duplicada (opcional pero bonito)
                elementos[tamano - 1] = null;

                // bajo el tamano porque ya elimine uno
                tamano--;

                System.out.println(elemento + " ha sido eliminado de la lista.");
                return true;
            }
        }

        // si no se encontro, pues no se puede borrar
        System.out.println(elemento + " no se encontro para eliminar.");
        return false;
    }

    // imprimir lo que hay en la lista
    public void imprimir() {
        System.out.println("Elementos en la lista:");
        for (int i = 0; i < tamano; i++) {
            System.out.println(elementos[i]);
        }
    }
}

// aqui solo lo pruebo para ver que si funciona
public class ListaEjemplo {

    public static void main(String[] args) {

        // creo una lista con capacidad 5
        ListaSimple lista = new ListaSimple(5);

        // agrego unos nombres
        lista.add("Juan");
        lista.add("Maria");
        lista.add("Pedro");

        // imprimo como va
        lista.imprimir();

        // busco uno
        lista.buscar("Pedro");
        lista.buscar("Luis"); // este no esta

        // elimino a maria
        lista.delete("Maria");

        // vuelvo a imprimir para ver que ya no sale
        lista.imprimir();
    }
}
