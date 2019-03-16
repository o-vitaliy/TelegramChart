package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class GridLines extends BaseChartElement {
    private static int STEPS = 5;

    private FloatBuffer vertexBuffer;
    private int vertexCount;


    public GridLines(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
    }

    @Override
    public void draw() {
        if (vertexBuffer == null) return;

        GLES20.glUseProgram(mProgram);
        GLES20.glLineWidth(chartViewModel.getGridLineWidth());
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, vertexBuffer);
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, chartViewModel.getGridColor(), 0);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount * 2);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    public void prepareForDraw() {
        vertexCount = STEPS;
        final float[] coordinates = new float[STEPS * COORDS_PER_VERTEX * 2];

        float height = bounds.height();

        //final double delta = maxValue - minValue;
        //final double step = delta / STEPS;
        final float stepHeight = height / STEPS;

        float y = bounds.bottom - stepHeight / 2;
        for (int i = 0; i < coordinates.length; i += 4) {
            float yP = converter.verticalValueToRelative(y);
            coordinates[i] = converter.horizontalValueToRelative(bounds.left);
            coordinates[i + 1] = yP;
            coordinates[i + 2] = converter.horizontalValueToRelative(bounds.right);
            coordinates[i + 3] = yP;
            y -= stepHeight;
        }
        vertexBuffer = ChartValuesUtil.coordinatesToBuffer(coordinates);
    }
}
