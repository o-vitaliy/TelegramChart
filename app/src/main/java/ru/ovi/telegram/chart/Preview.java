package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

public class Preview extends BaseChartElement implements Touchable {

    private PreviewRegionBound leftBound;
    private PreviewRegionBound rightBound;
    private PreviewOverlay leftOverlay;
    private PreviewOverlay rightOverlay;

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

    @Override
    void update() {
        float deltaLeft = bounds.left + chartViewModel.getLeftOffset() - leftBound.bounds.left;
        if (deltaLeft != 0) {
            leftBound.bounds.offset(deltaLeft, 0);
            leftOverlay.bounds.right += deltaLeft;

            leftBound.prepareForDraw();
            leftOverlay.prepareForDraw();
        }

        float deltaRight = bounds.left + chartViewModel.getRightOffset() - rightBound.bounds.right;
        if (deltaRight != 0) {
            rightBound.bounds.offset(deltaRight, 0);
            rightOverlay.bounds.left += deltaRight;

            rightBound.prepareForDraw();
            rightOverlay.prepareForDraw();
        }
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
        leftBound.type = PreviewRegionBoundType.LEFT;
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
        rightBound.type = PreviewRegionBoundType.RIGHT;
    }

    private void prepareLeftOverlay() {
        leftOverlay = create(
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
        rightOverlay = create(
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

    @Override
    public Touchable onTouched(float x, float y) {
        return ChartValuesUtil.findTouched(subElements, x, y);
    }

    private static <E extends BaseDrawingSubElement> E create(List<BaseDrawingSubElement> elements, Class<E> eClass, RectF bounds, final CoordinatesConverter converter, final ChartViewModel chartViewModel) {
        final E element = BaseDrawingSubElement.create(
                eClass, bounds, converter, chartViewModel);
        elements.add(element);
        return element;
    }
}
