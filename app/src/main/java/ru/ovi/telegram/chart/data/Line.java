package ru.ovi.telegram.chart.data;

public class Line {
    private final int color;
    private final String name;
    private final double values[];

    public Line(int color, String name, double[] values) {
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

    public double[] getValues() {
        return values;
    }
}
