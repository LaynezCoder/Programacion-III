
public class Nodo {

    // valor del nodo
    private int n;

    // hijo izquierdo y derecho
    private Nodo leftChild;
    private Nodo rightChild;

    // si solo me pasan el numero, lo creo sin hijos
    public Nodo(int n) {
        this(n, null, null);
    }

    // si me pasan numero + hijos, lo creo completo
    public Nodo(int n, Nodo leftChild, Nodo rightChild) {
        this.n = n;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    // devuelve el numerito
    public int getN() {
        return n;
    }

    // devuelve el hijo izquierdo
    public Nodo getLeftChild() {
        return leftChild;
    }

    // devuelve el hijo derecho
    public Nodo getRightChild() {
        return rightChild;
    }
}
