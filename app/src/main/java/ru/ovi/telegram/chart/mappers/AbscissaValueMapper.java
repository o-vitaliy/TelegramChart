package ru.ovi.telegram.chart.mappers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AbscissaValueMapper implements ValuesMapper<Double> {
    private static final SimpleDateFormat format = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

    @Override
    public String map(Double value) {
        return format.format(new Date(value.longValue()));
    }
}
