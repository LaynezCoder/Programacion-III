
import java.util.*;

public class ArbolBinario {

    // aqui guardo la raiz del arbol
    private Nodo root;

    // set de la raiz (o sea asignarla)
    public void setRoot(Nodo root) {
        this.root = root;
    }

    // get de la raiz para usarla en los recorridos
    public Nodo getRoot() {
        return root;
    }

    // aqui armo el arbol del ejemplo, con los numeritos que salen en la tarea
    public Nodo initial() {
        // hojas nodos sin hijos
        Nodo D = new Nodo(2);
        Nodo E = new Nodo(4);
        Nodo F = new Nodo(6);
        Nodo G = new Nodo(8);

        // nodos intermedios ya tienen hijos
        Nodo B = new Nodo(3, D, E);
        Nodo C = new Nodo(7, F, G);

        // este es el mero root
        Nodo A = new Nodo(5, B, C);

        return A;
    }

    // PREORDEN recursivo: primero imprime la raiz, luego izquierda, luego derecha
    public void preOrder(Nodo Nodo) {
        // si esta vacio, ya no hace nada
        if (Nodo == null) {
            return;
        }

        // imprime el valor del nodo actual
        System.out.print(Nodo.getN() + " ");

        // se va por la izquierda
        preOrder(Nodo.getLeftChild());

        // y luego por la derecha
        preOrder(Nodo.getRightChild());
    }

    // este metodo es solo para que compile igual que tu main (el "R" realmente no se usa)
    public void preOrder(Nodo Nodo, String R) {
        preOrder(Nodo);
    }

    // INORDEN recursivo: izquierda, raiz, derecha
    public void inOrder(Nodo Nodo) {
        if (Nodo == null) {
            return;
        }

        // primero baja a la izquierda
        inOrder(Nodo.getLeftChild());

        // imprime cuando regresa
        System.out.print(Nodo.getN() + " ");

        // luego se va a la derecha
        inOrder(Nodo.getRightChild());
    }

    // POSTORDEN recursivo: izquierda, derecha y al final imprime la raiz
    public void postOrder(Nodo Nodo) {
        if (Nodo == null) {
            return;
        }

        postOrder(Nodo.getLeftChild());
        postOrder(Nodo.getRightChild());

        // imprime al final
        System.out.print(Nodo.getN() + " ");
    }

    // PREORDEN iterativo: lo mismo que preOrder pero sin recursion, usando pila
    public void iterativePreOrder(Nodo Nodo) {
        Stack<Nodo> stack = new Stack<>();

        // mientras haya algo por recorrer (nodo actual o pila)
        while (Nodo != null || stack.size() > 0) {

            // aqui baja por la izquierda imprimiendo de una vez
            while (Nodo != null) {
                System.out.print(Nodo.getN() + " ");
                stack.push(Nodo);              // lo guardo para despues regresar
                Nodo = Nodo.getLeftChild();    // me voy a la izquierda
            }

            // si ya no hay izquierda, saco uno de la pila y me voy a su derecha
            if (stack.size() > 0) {
                Nodo = stack.pop();
                Nodo = Nodo.getRightChild();
            }
        }
    }

    // INORDEN iterativo: izquierda primero pero sin imprimir hasta que regresa
    public void iterativeInOrder(Nodo Nodo) {
        Stack<Nodo> stack = new Stack<>();

        while (Nodo != null || stack.size() > 0) {

            // baja todo lo que pueda por la izquierda guardando en pila
            while (Nodo != null) {
                stack.push(Nodo);
                Nodo = Nodo.getLeftChild();
            }

            // cuando ya no hay izquierda, saco uno, lo imprimo y me voy a su derecha
            if (stack.size() > 0) {
                Nodo = stack.pop();
                System.out.print(Nodo.getN() + " ");
                Nodo = Nodo.getRightChild();
            }
        }
    }

    // POSTORDEN (iterativo con 2 pilas): primero guardo orden y al final imprimo
    public void iterativePostOrder(Nodo Nodo) {
        Stack<Nodo> stack = new Stack<>();
        Stack<Nodo> temp = new Stack<>();

        // aqui voy recorriendo y metiendo en temp para luego imprimir al reves
        while (Nodo != null || stack.size() > 0) {

            // en este caso baja por la derecha primero
            while (Nodo != null) {
                temp.push(Nodo);              // esta pila me ayuda para imprimir al final
                stack.push(Nodo);             // esta es para regresar despues
                Nodo = Nodo.getRightChild();
            }

            // cuando ya no hay derecha, regreso y me voy a la izquierda
            if (stack.size() > 0) {
                Nodo = stack.pop();
                Nodo = Nodo.getLeftChild();
            }
        }

        // y aqui ya imprimo en el orden correcto del postorden
        while (temp.size() > 0) {
            Nodo = temp.pop();
            System.out.print(Nodo.getN() + " ");
        }
    }

    // esto saca la profundidad (o altura) del arbol
    public int getTreeDepth(Nodo Nodo) {
        if (Nodo == null) {
            return 0;
        }

        // saco cuanto mide por izquierda y por derecha
        int left = getTreeDepth(Nodo.getLeftChild());
        int right = getTreeDepth(Nodo.getRightChild());

        // agarro el mas grande y le sumo 1 (por el nodo actual)
        return (left < right) ? (right + 1) : (left + 1);
    }

    public static void main(String[] args) {

        // creo el arbol
        ArbolBinario bt = new ArbolBinario();
        bt.setRoot(bt.initial());

        // PREORDEN
        System.out.println("preOrder Recorrido de Arbol Binario: ");
        bt.preOrder(bt.getRoot(), "R");
        System.out.println();
        bt.iterativePreOrder(bt.getRoot());
        System.out.println();

        // INORDEN
        System.out.println("inOrder Recorrido de Arbol Binario: ");
        bt.inOrder(bt.getRoot());
        System.out.println();
        bt.iterativeInOrder(bt.getRoot());
        System.out.println();

        // POSTORDEN
        System.out.println("postOrder Recorrido de Arbol Binario: ");
        bt.postOrder(bt.getRoot());
        System.out.println();
        bt.iterativePostOrder(bt.getRoot());
        System.out.println();

        // PROFUNDIDAD
        System.out.print("Profundidad de Arbol Binario: ");
        System.out.println(bt.getTreeDepth(bt.getRoot()));
    }
}
