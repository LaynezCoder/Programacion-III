
class NodoUsuario {

    int id;
    String nombre;
    int altura;
    NodoUsuario izquierdo, derecho;

    NodoUsuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.altura = 1;
    }
}

public class ArbolAVLUsuarios {

    private NodoUsuario raiz;

    // Obtener altura
    private int altura(NodoUsuario n) {
        if (n == null) {
            return 0;
        }
        return n.altura;
    }

    // Obtener balance
    private int obtenerBalance(NodoUsuario n) {
        if (n == null) {
            return 0;
        }
        return altura(n.izquierdo) - altura(n.derecho);
    }

    // Rotación derecha
    private NodoUsuario rotarDerecha(NodoUsuario y) {
        NodoUsuario x = y.izquierdo;
        NodoUsuario t2 = x.derecho;

        x.derecho = y;
        y.izquierdo = t2;

        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    // Rotación izquierda
    private NodoUsuario rotarIzquierda(NodoUsuario x) {
        NodoUsuario y = x.derecho;
        NodoUsuario t2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = t2;

        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    // Inserción interna recursiva
    private NodoUsuario insertar(NodoUsuario nodo, int id, String nombre) {
        if (nodo == null) {
            return new NodoUsuario(id, nombre);
        }

        if (id < nodo.id) {
            nodo.izquierdo = insertar(nodo.izquierdo, id, nombre);
        } else if (id > nodo.id) {
            nodo.derecho = insertar(nodo.derecho, id, nombre);
        } else {
            System.out.println("No se permiten IDs duplicados: " + id);
            return nodo;
        }

        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));

        int balance = obtenerBalance(nodo);

        // LL
        if (balance > 1 && id < nodo.izquierdo.id) {
            return rotarDerecha(nodo);
        }

        // RR
        if (balance < -1 && id > nodo.derecho.id) {
            return rotarIzquierda(nodo);
        }

        // LR
        if (balance > 1 && id > nodo.izquierdo.id) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // RL
        if (balance < -1 && id < nodo.derecho.id) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    // Método público fácil de usar
    public void insertar(int id, String nombre) {
        raiz = insertar(raiz, id, nombre);
    }

    // Recorrido inOrden
    public void inOrden() {
        inOrden(raiz);
    }

    private void inOrden(NodoUsuario nodo) {
        if (nodo != null) {
            inOrden(nodo.izquierdo);
            System.out.println("ID: " + nodo.id + " | Nombre: " + nodo.nombre + " | Altura: " + nodo.altura);
            inOrden(nodo.derecho);
        }
    }

    // Mostrar raíz
    public void mostrarRaiz() {
        if (raiz != null) {
            System.out.println("Raíz actual: " + raiz.id + " - " + raiz.nombre);
        } else {
            System.out.println("El árbol está vacío");
        }
    }

    // Main de prueba
    public static void main(String[] args) {
        ArbolAVLUsuarios arbol = new ArbolAVLUsuarios();

        arbol.insertar(30, "Carlos");
        arbol.insertar(20, "Ana");
        arbol.insertar(10, "Luis");
        arbol.insertar(25, "Marta");
        arbol.insertar(40, "Pedro");
        arbol.insertar(50, "Sofia");

        arbol.mostrarRaiz();

        System.out.println("\nRecorrido inorden:");
        arbol.inOrden();
    }
}
