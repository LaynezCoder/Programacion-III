/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2703;

import java.util.LinkedList;

// esta clase Entry basicamente guarda una pareja clave-valor
class Entry {

    String key;
    String value;

    // constructor para guardar la llave y su valor
    Entry(String key, String value) {
        this.key = key;
        this.value = value;
    }
}

public class MiniDiccionario {

    private int SIZE = 10; // tamaño fijo de la tabla hash, aqui solo puse 10 por ejemplo
    private LinkedList<Entry>[] table; // arreglo de listas enlazadas para manejar colisiones

    public MiniDiccionario() {
        table = new LinkedList[SIZE];

        // aqui inicializo cada posicion del arreglo con una lista vacia
        for (int i = 0; i < SIZE; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // esta funcion convierte la llave en un indice
    // usando hashCode y luego saco modulo con el tamaño
    private int hash(String key) {
        return Math.abs(key.hashCode() % SIZE);
    }

    // metodo para insertar datos al diccionario
    public void put(String key, String value) {
        int idx = hash(key); // saco en que posicion va

        // reviso si ya existe esa llave en la lista
        for (Entry e : table[idx]) {
            if (e.key.equals(key)) {
                e.value = value; // si ya existe solo actualizo el valor
                return; // y termino aqui
            }
        }

        // si no existe, entonces lo agrego al final de la lista
        table[idx].add(new Entry(key, value));
    }

    // metodo para buscar un valor por medio de su llave
    public String get(String key) {
        int idx = hash(key); // busco el indice con la funcion hash

        // recorro la lista que esta en esa posicion
        for (Entry e : table[idx]) {
            if (e.key.equals(key)) {
                return e.value; // si lo encuentra devuelve el valor
            }
        }

        // si no lo encuentra devuelve este mensaje
        return "No encontrado";
    }

    // metodo para eliminar una llave con su valor
    public void remove(String key) {
        int idx = hash(key); // saco la posicion donde deberia estar

        // removeIf elimina el elemento si la condicion se cumple
        table[idx].removeIf(e -> e.key.equals(key));
    }

    public static void main(String[] args) {
        MiniDiccionario dic = new MiniDiccionario();

        // aqui meto algunos datos de prueba
        dic.put("Apple", "Manzana");
        dic.put("Book", "Libro");
        dic.put("Cat", "Gato");

        // pruebo buscando una palabra
        System.out.println("Apple significa: " + dic.get("Apple")); // deberia salir Manzana

        // ahora borro Apple
        dic.remove("Apple");

        // vuelvo a buscarla para ver si si se elimino
        System.out.println("Tras borrar, Apple es: " + dic.get("Apple")); // deberia salir No encontrado
    }
}
