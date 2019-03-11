package ru.ovi.telegram.chart.data;

public class ChartData {
    private final Line abscissa;
    private final Line[] ordinates;

    public ChartData(Line abscissa, Line[] ordinates) {
        this.abscissa = abscissa;
        this.ordinates = ordinates;
    }

    public Line getAbscissa() {
        return abscissa;
    }

    public Line[] getOrdinates() {
        return ordinates;
    }
}
