/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EmpleadoListaEnlazada;

import pckg001.Empleado;

public class EmpleadoListaEnlazada {

    // cabeza de la lista enlazada
    // desde aqui empieza todo
    private Empleado head;

    // eliminar empleado por id
    public void deleteEmpById(int id) {

        // si no hay nada en la lista, no hay nada que borrar
        if (head == null) {
            return;
        }

        Empleado temp = head;

        while (true) {

            // si el primero es el que quiero borrar
            if (temp.id == id) {
                head = temp.next;
                break;
            }

            // si ya llegue al final, ya no lo encontre
            if (temp.next == null) {
                break;
            }

            // si el siguiente nodo es el que quiero eliminar
            if (temp.next.id == id) {
                temp.next = temp.next.next;
                break;
            }

            // sigo avanzando en la lista
            temp = temp.next;
        }
    }

    // buscar empleado por id
    public Empleado findEmpById(int id) {

        // si la lista esta vacia retorno null
        if (head == null) {
            return null;
        }

        Empleado temp = head;

        while (true) {

            // si lo encontre lo dejo ahi
            if (temp.id == id) {
                break;
            }

            // si llegue al final y no estaba
            if (temp.next == null) {
                temp = null;
                break;
            }

            // sigo recorriendo
            temp = temp.next;
        }

        return temp;
    }

    // mostrar la lista enlazada
    public void list(int no) {

        // si no hay nodos, aviso que esta vacia
        if (head == null) {
            System.out.println("Sección " + (no + 1) + " la lista vinculada está vacía");
            return;
        }

        System.out.print("Sección " + (no + 1) + " información de la lista vinculada: ");

        Empleado temp = head;

        while (true) {
            System.out.print(temp + " --> ");

            if (temp.next == null) {
                break;
            }

            temp = temp.next;
        }

        System.out.println();
    }

    // agregar un empleado al final de la lista
    public void add(Empleado emp) {

        // si no hay nada, este sera el primero
        if (head == null) {
            head = emp;
            return;
        }

        Empleado temp = head;

        // me voy hasta el ultimo nodo
        while (temp.next != null) {
            temp = temp.next;
        }

        // enlazo el nuevo al final
        temp.next = emp;
    }
}
