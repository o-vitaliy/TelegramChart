package ru.ovi.telegram.chart;

import android.graphics.RectF;

public class PreviewDraggingArea extends BaseDrawingSubElement implements Touchable {

    protected PreviewDraggingArea(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
    }

    @Override
    void draw(int positionHandle, int colorHandle) {

    }

    @Override
    void prepareForDraw() {

    }

    @Override
    public Touchable onTouched(float x, float y) {
        if (bounds.contains(x, y))
            return this;
        return null;
    }
}
