package com.dhondoi.nonaseblak;

import org.junit.Test;

import com.dhondoi.nonaseblak.util.DateUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", new Locale("id", "ID"));
//        Date parse = null;
//        try {
//            parse = simpleDateFormat.parse(DateUtil.now());
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println(DateUtil.getStringDateNow());
    }
}