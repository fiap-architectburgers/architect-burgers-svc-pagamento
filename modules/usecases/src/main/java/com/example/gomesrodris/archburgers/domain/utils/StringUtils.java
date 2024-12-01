package com.example.gomesrodris.archburgers.domain.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
