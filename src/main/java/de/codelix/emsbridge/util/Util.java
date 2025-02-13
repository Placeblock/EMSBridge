package de.codelix.emsbridge.util;

public class Util {

    public static void validateEntityName(String name) {
        if (name.length() > 20) {
            throw new IllegalArgumentException("Name is too long");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name is too short");
        }
        if (!name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Name must only contain letters");
        }
    }
}
