package ru.ovi.telegram.chart.data;

import java.util.List;

public class ChartData {
    private final GraphLine abscissa;
    private final List<GraphLine> ordinates;

    public ChartData(GraphLine abscissa, List<GraphLine> ordinates) {
        this.abscissa = abscissa;
        this.ordinates = ordinates;
    }

    public GraphLine getAbscissa() {
        return abscissa;
    }

    public List<GraphLine> getOrdinates() {
        return ordinates;
    }
}
