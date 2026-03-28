package tablaHash;

import EmpleadoListaEnlazada.EmpleadoListaEnlazada;
import pckg001.Empleado;

public class TablaHash {

    // arreglo donde cada posicion tiene una lista enlazada
    // esto se usa para manejar colisiones
    private EmpleadoListaEnlazada[] empleadoListaEnlazadaArray;

    // tamaño de la tabla hash
    private int size;

    // constructor
    public TablaHash(int size) {
        this.size = size;
        this.empleadoListaEnlazadaArray = new EmpleadoListaEnlazada[size];

        // aqui inicializo cada espacio del arreglo
        // porque si no, daria null
        for (int i = 0; i < size; i++) {
            empleadoListaEnlazadaArray[i] = new EmpleadoListaEnlazada();
        }
    }

    // eliminar empleado segun su id
    public void deleteById(int id) {
        int i = hashFun(id); // saco en que posicion cae
        empleadoListaEnlazadaArray[i].deleteEmpById(id);
    }

    // buscar empleado por id
    public void findEmpById(int id) {
        int i = hashFun(id); // saco su posicion hash

        Empleado empleado = empleadoListaEnlazadaArray[i].findEmpById(id);

        if (empleado != null) {
            System.out.println("Id valor correspondiente: " + empleado);
        } else {
            System.out.println("No se encontró empleado con id: " + id);
        }
    }

    // mostrar todo lo que hay en la tabla hash
    public void list() {
        for (int i = 0; i < size; i++) {
            empleadoListaEnlazadaArray[i].list(i);
        }
    }

    // agregar empleado a la tabla hash
    public void add(Empleado empleado) {

        // saco en que indice va a caer segun su id
        int indiceLista = hashFun(empleado.id);

        // lo agrego a la lista enlazada de esa posicion
        empleadoListaEnlazadaArray[indiceLista].add(empleado);
    }

    // funcion hash sencilla
    // usa modulo para repartir ids en el arreglo
    private int hashFun(int id) {
        return id % size;
    }
}
