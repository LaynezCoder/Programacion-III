/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contadorhash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Laynecito
 */
public class ContadorHash {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap<Character, Integer> hm = new HashMap();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese una frase!");
        String cadena = scanner.next();

        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) != ' ') {
                if (hm.containsKey(cadena.charAt(i))) {
                    int cantidad = hm.get(cadena.charAt(i));
                    cantidad++;

                    hm.put(cadena.charAt(i), cantidad);
                } else {
                    hm.put(cadena.charAt(i), 1);
                }

            }
        }

        for(Character c : hm.keySet()) {
            System.out.println("El caracter " + c + " " + "aparece: " + hm.get(c) + " veces");
        }
    }

}
