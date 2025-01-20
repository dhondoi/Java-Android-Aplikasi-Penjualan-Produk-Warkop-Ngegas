package com.dhondoi.nonaseblak.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private final static SimpleDateFormat DATE_TIME_DB = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("id", "ID"));
    private final static SimpleDateFormat DATE_FROM_DATE_PICKER = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
    private final static SimpleDateFormat NORMAL_DATE_TIME = new SimpleDateFormat("EEEE, dd-MMMM-yyyy HH:mm", new Locale("id", "ID"));
    private final static SimpleDateFormat REPORT_INCOME_DATE = new SimpleDateFormat("dd-MMMM-yyyy", new Locale("id", "ID"));
    private final static SimpleDateFormat NORMAL_PRINT_DATE_TIME = new SimpleDateFormat("EEEE, dd-MM-yy HH:mm", new Locale("id", "ID"));

    private DateUtil() {
    }

    public static String getStringDateNow() {
        return DATE_TIME_DB.format(new Date());
    }

    public static String toNormalFormatDateTime(String dateTime) throws ParseException {
        return NORMAL_DATE_TIME.format(DATE_TIME_DB.parse(dateTime));
    }

    public static String getStringDateForReport(String dateTime) throws ParseException {
        return REPORT_INCOME_DATE.format(DATE_TIME_DB.parse(dateTime));
    }

    public static Date getDateForReport(String date) throws ParseException {
        return REPORT_INCOME_DATE.parse(date);
    }

    public static Date getDateFromDatePicker(String date) throws ParseException {
        return DATE_FROM_DATE_PICKER.parse(date);
    }

    public static String getStringDateNowForPrint() {
        return NORMAL_PRINT_DATE_TIME.format(new Date());
    }
}
