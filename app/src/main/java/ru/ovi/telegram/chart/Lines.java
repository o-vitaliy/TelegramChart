package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;
import android.util.Pair;
import com.annimon.stream.Stream;
import ru.ovi.telegram.chart.data.GraphLine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Lines extends BaseChartElement {
    private static int STEPS = 5;

    private List<Pair<float[], FloatBuffer>> lines = new ArrayList<>();
    private int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Lines(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);
    }

    @Override
    public void draw() {
        if (lines.isEmpty()) return;

        GLES20.glUseProgram(mProgram);
        GLES20.glLineWidth(6f);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        Stream.of(lines).forEach(line -> {
            GLES20.glVertexAttribPointer(
                    mPositionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    vertexStride, line.second);
            int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
            GLES20.glUniform4fv(colorHandle, 1, line.first, 0);
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertexCount);
        });
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    public void prepareForDraw() {
        vertexCount = chartViewModel.getOnScreenItemsCount();
        lines.clear();
        final float[] coordinates = new float[vertexCount * COORDS_PER_VERTEX];

        final List<GraphLine> chartLine = chartViewModel.getOrdinates();

        float height = bounds.height();
        float width = bounds.width();

        final float xStep = width / (vertexCount - 1);
        final float valuesDelta = (float) (chartViewModel.maxValue - chartViewModel.minValue);

        Stream.of(chartLine).forEach(line -> {
            for (int i = 0; i < vertexCount; i++) {
                float xP = bounds.left + chartViewModel.getItemsOffset() + xStep * i;
                float yP = bounds.top + height * ((line.getValues().get(i).floatValue() - (float) chartViewModel.minValue) / valuesDelta);
                coordinates[i * COORDS_PER_VERTEX] = converter.horizontalValueToRelative(xP);
                coordinates[i * COORDS_PER_VERTEX + 1] = converter.verticalValueToRelative(yP);
            }
            // initialize vertex byte buffer for shape coordinates
            ByteBuffer bb = ByteBuffer.allocateDirect(
                    // (# of coordinate values * 4 bytes per float)
                    coordinates.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(coordinates);
            vertexBuffer.position(0);

            lines.add(new Pair<>(
                    ChartValuesUtil.colorToFloatArray(line.getColor()),
                    vertexBuffer
            ));
        });
    }
}
