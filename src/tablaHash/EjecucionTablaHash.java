package tablaHash;

import java.util.Scanner;
import pckg001.Empleado;

public class EjecucionTablaHash {

    public static void main(String[] args) {

        // id automatico para cada empleado nuevo
        int id = 1;

        // creo la tabla hash con tamaño 7
        TablaHash tablaHash = new TablaHash(7);

        String key = "";
        Scanner input = new Scanner(System.in);

        while (true) {

            // menu medio rapido
            System.out.println("add: agregar | del: eliminar | list: mostrar | find: buscar | exit: salir");
            key = input.next();

            switch (key) {

                case "add":
                    System.out.print("Ingrese nombre: ");
                    String name = input.next();

                    // creo empleado con id automatico
                    Empleado empleado = new Empleado(id++, name);

                    // lo meto a la tabla hash
                    tablaHash.add(empleado);
                    break;

                case "del":
                    System.out.println("Introducir clave:");
                    tablaHash.deleteById(input.nextInt());
                    break;

                case "list":
                    tablaHash.list();
                    break;

                case "find":
                    System.out.println("Introducir clave:");
                    tablaHash.findEmpById(input.nextInt());
                    break;

                case "exit":
                    input.close();
                    System.exit(0);
                    break;

                default:
                    // si escribe algo raro pues sale
                    input.close();
                    System.exit(0);
                    break;
            }
        }
    }
}
