package ru.ovi.telegram.chart;

import android.graphics.RectF;

import java.lang.reflect.Constructor;

public abstract class BaseDrawingSubElement {
    final RectF bounds;
    protected final CoordinatesConverter converter;
    final ChartViewModel chartViewModel;

    protected BaseDrawingSubElement(final RectF bounds, final CoordinatesConverter converter, final ChartViewModel chartViewModel) {
        this.bounds = bounds;
        this.converter = converter;
        this.chartViewModel = chartViewModel;
    }


    abstract void draw(int positionHandle, int colorHandle);

    abstract void prepareForDraw();

    public static <E extends BaseDrawingSubElement> E create(final Class<E> eClass, final RectF bounds, final CoordinatesConverter converter, final ChartViewModel chartViewModel) {
        try {
            Class<?> clazz = Class.forName(eClass.getName());
            Constructor<?> ctor = clazz.getConstructor(RectF.class, CoordinatesConverter.class, ChartViewModel.class);
            return (E) ctor.newInstance(new Object[]{bounds, converter, chartViewModel});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
