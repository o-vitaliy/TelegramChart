package ru.ovi.telegram.chart;

import android.graphics.Color;
import android.text.TextPaint;
import ru.ovi.telegram.chart.data.ChartData;
import ru.ovi.telegram.chart.data.GraphLine;
import ru.ovi.telegram.chart.mappers.AbscissaValueMapper;
import ru.ovi.telegram.chart.mappers.OrdinatesValueMapper;
import ru.ovi.telegram.chart.mappers.ValuesMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ChartViewModel {

    static int GRIDS_LINE_COUNT = 5;

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

    private float minValueInOffset;
    private float maxValueInOffset;

    private final TextPaint textPaint;

    private ValuesMapper<Double> ordinatesValueMapper = new OrdinatesValueMapper();
    private ValuesMapper<Double> abscissaValueMapper = new AbscissaValueMapper();

    public ChartViewModel() {
        textPaint = new TextPaint();
        textPaint.setTextSize(32);
        textPaint.setColor(Color.BLACK);
    }

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

    public int getItemsCount() {
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
        findMinMaxValueInOffset();
    }

    public void changeRightOffset(float delta) {
        rightOffset = Math.min(rightOffset + delta, maxRightOffset);
        rightOffset = Math.max(rightOffset, leftOffset + boundWidth * 2);
        findMinMaxValueInOffset();
    }

    public void setMaxRightOffset(float maxRightOffset) {
        this.maxRightOffset = maxRightOffset;
        rightOffset = maxRightOffset;
        leftOffset = maxRightOffset - minOffset;
        findMinMaxValueInOffset();
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
        findMinMaxValueInOffset();
    }

    private void findMinMaxValueInOffset() {
        if (chartData == null) return;
        final int count = getItemsCount();
        int leftCountOffset = (int) Math.floor(count * leftOffset / maxRightOffset);
        int rightCountOffset = (int) Math.ceil(count * rightOffset / maxRightOffset);
        final List<GraphLine> lines = chartData.getOrdinates();

        maxValueInOffset = (float) ChartValuesUtil.maxInLines(lines, leftCountOffset, rightCountOffset);
        minValueInOffset = (float) ChartValuesUtil.minInLines(lines, leftCountOffset, rightCountOffset);
    }

    public float getMinValueInOffset() {
        return minValueInOffset;
    }

    public float getMaxValueInOffset() {
        return maxValueInOffset;
    }

    public float getTextSize() {
        return textPaint.getTextSize();
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public List<Double> getOrdinateValues() {
       /*
       final Predicate<Double> predicate = v -> v.floatValue() < leftOffset;
        final int itemsCount = chartData.getAbscissa().getValues().size();
        final int startIndex = Math.max(0, ListUtils.findIndex(chartData.getAbscissa().getValues(), predicate, Integer.MIN_VALUE));
        final int endIndex = Math.min(itemsCount - 1, ListUtils.findIndex(chartData.getAbscissa().getValues().subList(startIndex, itemsCount), predicate, Integer.MAX_VALUE));
      */
        final double delta = maxValueInOffset - minValueInOffset;
        final double step = delta / GRIDS_LINE_COUNT;
        final List<Double> list = new ArrayList<>(GRIDS_LINE_COUNT);
        list.add((double) minValueInOffset);
        for (int i = 1; i < GRIDS_LINE_COUNT; i++) {
            list.add(list.get(i - 1) + step);
        }
        return list;
    }

    public ValuesMapper<Double> getOrdinatesValueMapper() {
        return ordinatesValueMapper;
    }

    public ValuesMapper<Double> getAbscissaValueMapper() {
        return abscissaValueMapper;
    }
}
