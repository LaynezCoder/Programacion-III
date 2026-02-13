// PILA (tipo stack), lo ultimo que entra es lo primero que sale

class Pila {

    private String[] elementos; // aqui guardo las cosas
    private int tope;           // esto me dice cual es el de arriba

    public Pila(int capacidad) {
        elementos = new String[capacidad]; // tamaño fijo
        tope = -1; // -1 porque aun no hay nada
    }

    public void push(String elemento) {
        // si todavia hay espacio
        if (tope < elementos.length - 1) {
            elementos[++tope] = elemento; // primero subo el tope y luego guardo
            System.out.println(elemento + " ha sido apilado.");
        } else {
            System.out.println("La pila esta llena.");
        }
    }

    public String pop() {
        // si hay algo para sacar
        if (tope >= 0) {
            String elemento = elementos[tope--]; // agarro el de arriba y bajo el tope
            System.out.println(elemento + " ha sido desapilado.");
            return elemento;
        }

        System.out.println("La pila esta vacia.");
        return null;
    }

    public boolean search(String elemento) {
        // busco desde abajo hasta arriba (hasta el tope)
        for (int i = 0; i <= tope; i++) {
            if (elementos[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    public void imprimir() {
        System.out.println("Elementos en la pila:");
        // imprimo de arriba hacia abajo para que se mire como pila
        for (int i = tope; i >= 0; i--) {
            System.out.println(elementos[i]);
        }
    }
}

public class PilaEjemplo {

    public static void main(String[] args) {

        Pila pila = new Pila(5);

        pila.push("Elemento 1");
        pila.push("Elemento 2");
        pila.push("Elemento 3");

        pila.imprimir();

        pila.pop();

        pila.imprimir();

        System.out.println("Buscar 'Elemento 2': " + pila.search("Elemento 2"));
    }
}
