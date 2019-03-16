package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import static ru.ovi.telegram.chart.BaseChartElement.COORDS_PER_VERTEX;
import static ru.ovi.telegram.chart.BaseChartElement.VERTEX_STRIDE;

public class PreviewGrid extends BaseDrawingSubElement {
    private final int GRID_VERTEX_COUNT = 5;
    private FloatBuffer gridsBuffer;

    protected PreviewGrid(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
    }

    @Override
    void draw(int positionHandle, int colorHandle) {
        GLES20.glLineWidth(chartViewModel.getGridLineWidth());

        GLES20.glVertexAttribPointer(
                positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, gridsBuffer);
        GLES20.glUniform4fv(colorHandle, 1, chartViewModel.getGridColor(), 0);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, GRID_VERTEX_COUNT);
    }

    @Override
    void prepareForDraw() {
        float[] grid = new float[]{
                converter.horizontalValueToRelative(bounds.left),
                converter.verticalValueToRelative(bounds.top),
                converter.horizontalValueToRelative(bounds.right),
                converter.verticalValueToRelative(bounds.top),
                converter.horizontalValueToRelative(bounds.right),
                converter.verticalValueToRelative(bounds.bottom),
                converter.horizontalValueToRelative(bounds.left),
                converter.verticalValueToRelative(bounds.bottom),
                converter.horizontalValueToRelative(bounds.left),
                converter.verticalValueToRelative(bounds.top),
        };

        gridsBuffer = ChartValuesUtil.coordinatesToBuffer(grid);
    }
}
