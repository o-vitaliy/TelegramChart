package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

public class Preview extends BaseChartElement {


    private PreviewRegionBound leftBound;
    private PreviewRegionBound rightBound;

    private List<BaseDrawingSubElement> subElements = new ArrayList<>();

    public Preview(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
    }

    @Override
    public void draw() {
        if (subElements.isEmpty()) return;

        GLES20.glUseProgram(mProgram);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        Stream.of(subElements).forEach(e -> e.draw(positionHandle, colorHandle));

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public void prepareForDraw() {
        prepareGrid();
        prepareLeftBound();
        prepareRightBound();
        prepareLeftOverlay();
        prepareRightOverlay();

        Stream.of(subElements).forEach(BaseDrawingSubElement::prepareForDraw);
    }

    private void prepareGrid() {
        create(subElements, PreviewGrid.class, bounds, converter, chartViewModel);
    }

    private void prepareLeftBound() {
        leftBound = create(
                subElements,
                PreviewRegionBound.class,
                new RectF(
                        bounds.left + chartViewModel.getLeftOffset(),
                        bounds.top,
                        bounds.left + chartViewModel.getLeftOffset() + chartViewModel.getBoundWidth(),
                        bounds.bottom
                ),
                converter,
                chartViewModel
        );
    }

    private void prepareRightBound() {
        rightBound = create(
                subElements,
                PreviewRegionBound.class,
                new RectF(
                        bounds.left + chartViewModel.getRightOffset() - chartViewModel.getBoundWidth(),
                        bounds.top,
                        bounds.left + chartViewModel.getRightOffset(),
                        bounds.bottom
                ),
                converter,
                chartViewModel
        );
    }

    private void prepareLeftOverlay() {
        create(
                subElements,
                PreviewOverlay.class,
                new RectF(
                        bounds.left,
                        bounds.top,
                        bounds.left + chartViewModel.getLeftOffset(),
                        bounds.bottom
                ),
                converter,
                chartViewModel
        );
    }

    private void prepareRightOverlay() {
        create(
                subElements,
                PreviewOverlay.class,
                new RectF(
                        bounds.left + chartViewModel.getRightOffset(),
                        bounds.top,
                        bounds.right,
                        bounds.bottom
                ),
                converter,
                chartViewModel
        );
    }

    private static <E extends BaseDrawingSubElement> E create(List<BaseDrawingSubElement> elements, Class<E> eClass, RectF bounds, final CoordinatesConverter converter, final ChartViewModel chartViewModel) {
        final E element = BaseDrawingSubElement.create(
                eClass, bounds, converter, chartViewModel);
        elements.add(element);
        return element;
    }
}
