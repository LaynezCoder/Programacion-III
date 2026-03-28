package pkg2703;


import java.util.HashMap;
import java.util.Map;

public class ContadorFrecuencia {

    public static void main(String[] args) {

        // texto que se va a analizar letra por letra
        String texto = "las tablas hash son veloces y eficientes";

        // aqui guardo cada letra con la cantidad de veces que aparece
        Map<Character, Integer> freqMap = new HashMap<Character, Integer>();

        // recorro todo el texto caracter por caracter
        for (char c : texto.toCharArray()) {

            // si es espacio no lo tomo en cuenta
            if (c == ' ') {
                continue;
            }

            // si la letra ya existe le suma 1
            // si no existe entonces empieza en 0 y luego suma 1
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // imprime el mapa completo
        System.out.println("Frecuencias: " + freqMap);

        // aqui ordena de mayor a menor segun la frecuencia
        freqMap.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .forEach(entry
                        -> System.out.println(entry.getKey() + " = " + entry.getValue())
                );
    }
}
