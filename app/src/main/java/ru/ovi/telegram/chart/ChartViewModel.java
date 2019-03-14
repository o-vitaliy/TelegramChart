package ru.ovi.telegram.chart;

import ru.ovi.telegram.chart.data.ChartData;
import ru.ovi.telegram.chart.data.GraphLine;

import java.util.List;

public class ChartViewModel {

    private ChartData chartData;
    private boolean initialized;
    double maxValue;
    double minValue;

    public void init(ChartData chartData) {
        this.chartData = chartData;
        final List<GraphLine> lines = chartData.getOrdinates();

        maxValue = ChartValuesUtil.maxInLines(lines);
        minValue = ChartValuesUtil.minInLines(lines);

        initialized = true;
    }

    public List<GraphLine> getOrdinates() {
        return chartData.getOrdinates();
    }

    public int getOnScreenItemsCount() {
        return chartData.getAbscissa().getValues().size();
    }

    public float getItemsOffset() {
        return 0;
    }

    public boolean isInitialized(){
        return initialized;
    }
}
