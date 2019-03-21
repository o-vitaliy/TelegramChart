package ru.ovi.telegram.chart.mappers;

public class OrdinatesValueMapper implements ValuesMapper<Double> {
    @Override
    public String map(Double value) {
        return String.valueOf(value.intValue());
    }
}
