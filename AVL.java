
class Nodo {

    int dato;
    int altura;
    Nodo izquierdo;
    Nodo derecho;

    public Nodo(int dato) {
        this.dato = dato;
        this.altura = 1;
        this.izquierdo = null;
        this.derecho = null;
    }
}

public class AVL {

    // metodo para obtener la altura
    public static int altura(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.altura;
    }

    //metodo para el factor de quilibro
    public static int factorEquilibrio(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }

        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    // analizamos el balanceo
    public static void analizarBalanceo(Nodo nodo) {
        if (nodo == null) {
            System.out.println("El nodo es null");
            return;
        }

        int factor = factorEquilibrio(nodo);

        System.out.println("Nodo analizado " + nodo.dato);
        System.out.println("Factor de equilibrio " + factor);

        //si árbol está cargado hacia la izquierda
        if (factor > 1) {
            int factorHijoIzquierdo = factorEquilibrio(nodo.izquierdo);

            if (factorHijoIzquierdo >= 0) {
                System.out.println("Rotacion requerida simple a la Derecha");
            } else {
                System.out.println("Rotacion requerida: doble Izquierda-Derecha");
            }
        } // Caso el árbol está cargado hacia la derecha
        else if (factor < -1) {
            int factorHijoDerecho = factorEquilibrio(nodo.derecho);

            if (factorHijoDerecho <= 0) {
                System.out.println("Rotación requerida Simple a la Izquierda");
            } else {
                System.out.println("Rotación requerida oble Derecha-Izquierda");
            }
        } // en caso de que el nodo está balanceado
        else {
            System.out.println("El nodo está balanceado, no necesita rotación");
        }
    }

    public static void main(String[] args) {

        System.out.println("CASO 1: Rotación Simple a la Derecha");
        Nodo raiz1 = new Nodo(30);
        raiz1.izquierdo = new Nodo(20);
        raiz1.izquierdo.izquierdo = new Nodo(10);

        raiz1.altura = 3;
        raiz1.izquierdo.altura = 2;
        raiz1.izquierdo.izquierdo.altura = 1;

        analizarBalanceo(raiz1);

        System.out.println("------------------------------");

        System.out.println("CASO 2: Rotación Simple a la Izquierda");
        Nodo raiz2 = new Nodo(10);
        raiz2.derecho = new Nodo(20);
        raiz2.derecho.derecho = new Nodo(30);

        raiz2.altura = 3;
        raiz2.derecho.altura = 2;
        raiz2.derecho.derecho.altura = 1;

        analizarBalanceo(raiz2);

        System.out.println("------------------------------");

        System.out.println("CASO 3: Rotación Doble Izquierda-Derecha");
        Nodo raiz3 = new Nodo(30);
        raiz3.izquierdo = new Nodo(10);
        raiz3.izquierdo.derecho = new Nodo(20);

        raiz3.altura = 3;
        raiz3.izquierdo.altura = 2;
        raiz3.izquierdo.derecho.altura = 1;

        analizarBalanceo(raiz3);

        System.out.println("------------------------------");

        System.out.println("CASO 4: Rotación Doble Derecha-Izquierda");
        Nodo raiz4 = new Nodo(10);
        raiz4.derecho = new Nodo(30);
        raiz4.derecho.izquierdo = new Nodo(20);

        raiz4.altura = 3;
        raiz4.derecho.altura = 2;
        raiz4.derecho.izquierdo.altura = 1;

        analizarBalanceo(raiz4);

        System.out.println("------------------------------");

        System.out.println("CASO 5: Nodo balanceado");
        Nodo raiz5 = new Nodo(20);
        raiz5.izquierdo = new Nodo(10);
        raiz5.derecho = new Nodo(30);

        raiz5.altura = 2;
        raiz5.izquierdo.altura = 1;
        raiz5.derecho.altura = 1;

        analizarBalanceo(raiz5);
    }

}
