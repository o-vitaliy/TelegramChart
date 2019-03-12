package ru.ovi.telegram.chart.data;

import java.util.List;

public class GraphLine {
    private final int color;
    private final String name;
    private final List<Double> values;

    public GraphLine(int color, String name, List<Double> values) {
        this.color = color;
        this.name = name;
        this.values = values;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<Double> getValues() {
        return values;
    }
}
