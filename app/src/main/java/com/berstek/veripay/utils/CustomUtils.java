package com.berstek.veripay.utils;

import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomUtils {

    public static boolean editTextEmpty(EditText... args) {

        for (EditText e : args) {
            if (e.getText().length() == 0)
                return true;
        }

        return false;
    }

    public static String formatDF(double num) {
        DecimalFormat df = new DecimalFormat("0.00");

        return df.format(num);
    }

    public static String parseDateMMdd(long d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        return dateFormat.format(new Date(d));
    }

    public static String parseDateMMddYYYY(long d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
        return dateFormat.format(new Date(d));
    }
}
