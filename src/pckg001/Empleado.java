/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pckg001;

public class Empleado {

    // id del empleado, esto lo use como clave
    public int id;

    // nombre del empleado
    public String name;

    // apuntador al siguiente nodo
    // esto sirve para la lista enlazada
    public Empleado next;

    // constructor
    public Empleado(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        // esto es solo para que al imprimir el objeto salga entendible
        return "Empleado{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
