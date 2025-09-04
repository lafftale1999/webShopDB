package org.example.helpers;

public class helper {
    public static Integer parseInt(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        return Integer.valueOf(s);
    }
}
