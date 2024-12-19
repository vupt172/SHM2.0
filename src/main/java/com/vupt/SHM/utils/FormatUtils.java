package com.vupt.SHM.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
    public static String formatVNDCurrency(long price) {
        Locale vn = new Locale("vi", "VN");
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
        return vndFormat.format(price);
    }
}
