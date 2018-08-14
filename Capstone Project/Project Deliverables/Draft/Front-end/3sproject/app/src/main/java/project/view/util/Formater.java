package project.view.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Formater {

    public static String formatDoubleToMoney(String price) {
        Double p = Double.parseDouble(price);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(p);
        return str1;
    }
//    public static String formatDoubleToMoney(String price) {
//
//        NumberFormat format =
//                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
//        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale
//
//        price = (!TextUtils.isEmpty(price)) ? price : "0";
//        price = price.trim();
//        price = format.format(Double.parseDouble(price));
//        price = price.replaceAll(",", "\\.");
//
//        if (price.endsWith(".00")) {
//            int centsIndex = price.lastIndexOf(".00");
//            if (centsIndex != -1) {
//                price = price.substring(0, centsIndex);
//            }
//        }
//        price = String.format("%s đ", price);
//        return price;
//    }

    public static String formatDoubleToInt(String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s%%", price);
        return price;
    }


    public static int dpToPx(int dp,Resources r) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
