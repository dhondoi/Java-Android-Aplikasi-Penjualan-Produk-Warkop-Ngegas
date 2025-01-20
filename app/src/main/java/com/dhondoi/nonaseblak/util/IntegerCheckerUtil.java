package com.dhondoi.nonaseblak.util;

public class IntegerCheckerUtil {
    public static void checkLessThan1(Long... longs) throws Exception {
        for (Long aLong : longs) {
            if (aLong == null || aLong < 1)
                throw new Exception("Angka Kurang Dari 1");
        }
    }

    public static boolean isLessThan1(Long... longs) {
        for (Long aLong : longs) {
            if (aLong == null || aLong < 1)
                return true;
        }
        return false;
    }
}
