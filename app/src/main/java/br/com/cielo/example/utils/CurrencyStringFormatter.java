package br.com.cielo.example.utils;

import android.content.res.Resources;

import br.com.cielo.example.R;

public class CurrencyStringFormatter {
    public static String fromCentsToUnitsString(Resources resources, long valueInCents) {
        return String.format(resources.getString(R.string.currency_format), ((float) valueInCents / 100));
    }
}
