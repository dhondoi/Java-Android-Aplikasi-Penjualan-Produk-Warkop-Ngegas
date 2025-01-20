package com.dhondoi.nonaseblak.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class CurrencyUtil {

    private CurrencyUtil() {
    }

    public static String toCurrency(int number) {

        return setUp().format(number);
    }

    public static Number toInteger(String currency) throws ParseException {
        return setUp().parse(currency);
    }

    private static DecimalFormat setUp() {
        DecimalFormat currency = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("Rp ");
        currency.setMaximumFractionDigits(0);
        currency.setDecimalFormatSymbols(decimalFormatSymbols);
        return currency;
    }
}
