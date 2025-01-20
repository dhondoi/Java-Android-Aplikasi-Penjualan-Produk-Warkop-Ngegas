package com.dhondoi.nonaseblak.util;

public class StringCheckerUtil {
    public static void checkedEmpty(String... strings) throws Exception {
        for (String string : strings) {
            if (string == null || string.trim().isEmpty())
                throw new Exception("Data Kosong");
        }
    }

    public static boolean isEmpty(String... strings) {
        for (String string : strings) {
            if (string == null || string.trim().isEmpty())
                return true;
        }
        return false;
    }
}
