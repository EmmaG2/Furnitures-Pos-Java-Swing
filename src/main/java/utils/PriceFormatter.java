package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {
    public static String formatPrice(double price) {
        return NumberFormat.getCurrencyInstance(Locale.of("es", "MX")).format(price);
    }
}
