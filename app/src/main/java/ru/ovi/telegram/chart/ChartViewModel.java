package ru.ovi.telegram.chart;

import android.graphics.Color;
import ru.ovi.telegram.chart.data.ChartData;
import ru.ovi.telegram.chart.data.GraphLine;

import java.util.List;

public class ChartViewModel {

    private final float gridColor[] = ChartValuesUtil.colorToFloatArray(Color.parseColor("#A0CCCCCC"));
    private final float previewOverlayColor[] = ChartValuesUtil.colorToFloatArray(Color.parseColor("#70EEEEEE"));
    private final float gridLineWidth = 2;
    private final float chartLineWidth = 6;
    private final float boundWidth = 40;

    private ChartData chartData;
    private boolean initialized;
    double maxValue;
    double minValue;
    private float leftOffset = 100;
    private float rightOffset = 500;
    private float maxRightOffset;
    private float minOffset = 100;

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

    public boolean isInitialized() {
        return initialized;
    }

    public float[] getGridColor() {
        return gridColor;
    }

    public float getGridLineWidth() {
        return gridLineWidth;
    }

    public float getChartLineWidth() {
        return chartLineWidth;
    }


    public float getLeftOffset() {
        return leftOffset;
    }

    public float getRightOffset() {
        return rightOffset;
    }

    public float getBoundWidth() {
        return boundWidth;
    }

    public float[] getPreviewOverlayColor() {
        return previewOverlayColor;
    }

    public void changeLeftOffset(float delta) {
        leftOffset = Math.max(0, leftOffset + delta);
        leftOffset = Math.min(leftOffset, rightOffset - boundWidth * 2);
    }

    public void changeRightOffset(float delta) {
        rightOffset = Math.min(rightOffset + delta, maxRightOffset);
        rightOffset = Math.max(rightOffset, leftOffset + boundWidth * 2);
    }

    public void setMaxRightOffset(float maxRightOffset) {
        this.maxRightOffset = maxRightOffset;
    }

    public void changeOffset(float delta) {
        float boundsDelta = rightOffset - leftOffset;
        if (delta > 0) {
            changeRightOffset(delta);
            leftOffset = rightOffset - boundsDelta;
        } else {
            changeLeftOffset(delta);
            rightOffset = leftOffset + boundsDelta;
        }
    }
}
