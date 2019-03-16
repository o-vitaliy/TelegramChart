package ru.ovi.telegram.chart;

import android.graphics.RectF;

public abstract class BaseDrawingElement {
    protected final RectF bounds;
    protected final CoordinatesConverter converter;
    protected final ChartViewModel chartViewModel;

    protected BaseDrawingElement(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        this.bounds = bounds;
        this.converter = converter;
        this.chartViewModel = chartViewModel;
    }

    abstract void draw();

    abstract void prepareForDraw();

    abstract void update();
}
