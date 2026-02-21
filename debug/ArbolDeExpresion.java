
public class ArbolDeExpresion {

    public static void main(String[] m) {

        final String EXPRESION_DE_PRUEBA = "ab+cde+**",
                Ejmplo2 = "22/24+467-5****";

        NodoExpresion raiz = new NodoExpresion("");
        raiz = raiz.CrearArbolDeExpresiones(Ejmplo2);

        System.out.println("Expresion postfix mostrada en PostOrder");
        raiz.postOrder();

        System.out.println("\n\nExpresion Infix mostrada en InOrder");
        raiz.inOrder();
    }
}
