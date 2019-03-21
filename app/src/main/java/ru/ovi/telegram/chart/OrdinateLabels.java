package ru.ovi.telegram.chart;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

class OrdinateLabels extends BaseDrawingElement {

    private List<Label> labels;

    OrdinateLabels(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
        createLabels();
    }

    @Override
    void draw() {
        for (final Label l : labels) {
            l.draw();
        }
    }

    @Override
    void prepareForDraw() {
        final List<Double> values = chartViewModel.getOrdinateValues();
        for (int i = 0; i < ChartViewModel.GRIDS_LINE_COUNT; i++) {
            final String text = chartViewModel.getOrdinatesValueMapper().map(values.get(i));
            labels.get(i).setText(text);
        }
    }

    @Override
    void update() {
        prepareForDraw();
    }

    private void createLabels() {
        final float delta = bounds.height() / ChartViewModel.GRIDS_LINE_COUNT;
        labels = new ArrayList<>();
        for (int i = 0; i < ChartViewModel.GRIDS_LINE_COUNT; i++) {
            final float textHeight = chartViewModel.getTextSize();

            final float y = bounds.bottom - textHeight - delta * i - delta / 2;
            labels.add(new Label(
                    new RectF(
                            bounds.left,
                            y,
                            bounds.right,
                            y + textHeight
                    ),
                    converter, chartViewModel
            ));
        }
    }
}
