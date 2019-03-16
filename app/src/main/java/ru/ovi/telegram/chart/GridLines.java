package ru.ovi.telegram.chart;

import android.graphics.Color;
import android.graphics.RectF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GridLines extends BaseChartElement {
    private static int STEPS = 5;

    final float color[] = ChartValuesUtil.colorToFloatArray(Color.parseColor("#F0CCCCCC"));
    private FloatBuffer vertexBuffer;
    private int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public GridLines(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
    }

    @Override
    public void draw() {
        if (vertexBuffer == null) return;

        GLES20.glUseProgram(mProgram);
        GLES20.glLineWidth(2f);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
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
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                coordinates.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);
    }
}
