/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listas;

import java.util.*;

public class Ejemplo {

    public static void main(String[] args) {

        //Se crea una lista que almacena Strings
        List<String> lista = new LinkedList<String>();

        //Se agregan nuevos elementos
        lista.add("María");

        lista.add("Pedro");

        lista.add("Jose");

        lista.add("Marcos");

        //Se crea una instancia de un iterador para poder recorrer cada elemento
        Iterator<String> it = lista.iterator();

        //Mientras exista un elemento, se imprime el elemento
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        //Se elimina PEDRO
        lista.remove("Pedro"); // it debe descartarse

        System.out.println("Pedro borrado");

        //Se crea una instancia de ListIterator, otro iterador mas potente y se le coloca como indice, el tamanio total de la lista
        ListIterator<String> it2 = lista.listIterator(lista.size());

        
        //a diferencia de hasNext, hasPrevious se mueve hacia atras una vez existan elementos.
        while (it2.hasPrevious()) { //esta vez vamos hacia atrás

            //Se imprimen
            System.out.println(it2.previous());
        }

    }

}
