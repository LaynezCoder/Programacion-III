import java.util.EmptyStackException;
import java.util.Stack;

public class NodoExpresion {
    public NodoExpresion Iz, Dr;
    public String texto = "";

    public NodoExpresion(String nuevo_texto) {
        texto = nuevo_texto;
    }

    public NodoExpresion(String nuevo_texto, NodoExpresion izquierda, NodoExpresion derecha) {
        texto = nuevo_texto;
        Iz = izquierda;
        Dr = derecha;
    }

    public boolean esOperador(String nuevo_texto) {
        String[] mOperadores = { "+", "-", "*", "/", "%", "^" };
        for (int i = 0; i < mOperadores.length; ++i) {
            if (nuevo_texto.equalsIgnoreCase(mOperadores[i]) == true) {
                return true;
            }
        }
        return false;
    }

    private void msi(String datos) {
        System.out.println(datos);
    }

    public void postOrder() {
        vPostOrder = "";
        System.out.println(postOrder(this));
    }

    private String vPostOrder = "";

    private String postOrder(NodoExpresion nueva_raiz) {
        if (nueva_raiz == null) {
            return "";
        }

        postOrder(nueva_raiz.Iz);
        postOrder(nueva_raiz.Dr);
        vPostOrder += nueva_raiz.texto;
        return vPostOrder;
    }

    public void inOrder() {
        vInOrder = "";
        System.out.println(inOrder(this));
    }

    private String vInOrder = "";

    private String inOrder(NodoExpresion nueva_raiz) {
        if (nueva_raiz == null) {
            return "";
        }

        if (this.esOperador(nueva_raiz.texto) == true) {
            vInOrder += "(";
        }

        inOrder(nueva_raiz.Iz);
        vInOrder += nueva_raiz.texto;
        inOrder(nueva_raiz.Dr);

        if (this.esOperador(nueva_raiz.texto) == true) {
            vInOrder += ")";
        }

        return vInOrder;
    }

    public NodoExpresion CrearArbolDeExpresiones(String expresion) {
        if (expresion.isEmpty() == true) {
            return null;
        }

        Stack<NodoExpresion> s = new Stack<NodoExpresion>();

        for (char c : expresion.toCharArray()) {
            if (this.esOperador(c + "") == true) {

                NodoExpresion x = null;
                try {
                    x = s.pop();
                } catch (EmptyStackException e) {
                }

                NodoExpresion y = null;
                try {
                    y = s.pop();
                } catch (EmptyStackException e) {
                }

                NodoExpresion nodo = new NodoExpresion(c + "", y, x);
                s.push(nodo);

            } else {
                s.push(new NodoExpresion(c + ""));
            }
        }

        return s.peek();
    }
}