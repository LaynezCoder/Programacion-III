package com.proyecto;

public class Base62Encoder {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String encode(long number) {
        if (number == 0) {
            return "a";
        }

        StringBuilder sb = new StringBuilder();

        while (number > 0) {
            int index = (int) (number % 62);
            sb.append(BASE62.charAt(index));
            number = number / 62;
        }

        return sb.reverse().toString();
    }
}
