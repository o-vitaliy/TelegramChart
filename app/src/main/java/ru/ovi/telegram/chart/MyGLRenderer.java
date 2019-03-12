package ru.ovi.telegram.chart;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.annimon.stream.Stream;
import ru.ovi.telegram.chart.data.ChartData;

import java.util.ArrayList;
import java.util.List;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square mSquare;
    private Line line;
    private ChartData chartData;
    private CoordinatesConverter converter = new CoordinatesConverter();
    private HorizontalLines horizontalLines;
    private List<BaseChartElement> elementList = new ArrayList<>();

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        mTriangle = new Triangle();
        mSquare = new Square();
        line = new Line();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (chartData == null) return;

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glLineWidth(10f);
        mSquare.draw();
        mTriangle.draw();
        line.draw();

        Stream.of(elementList).forEach(BaseChartElement::draw);

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        converter.setNewSizes(width, height);
        GLES20.glViewport(0, 0, width, height);
        horizontalLines = new HorizontalLines(new RectF(10f, 0f, (float) width, (float) height), converter);
        elementList.add(horizontalLines);

        if (chartData != null) {
            Stream.of(elementList).forEach(e -> e.prepareForDraw(chartData));
        }
    }

    public void setChartData(ChartData chartData) {
        this.chartData = chartData;
        Stream.of(elementList).forEach(e -> e.prepareForDraw(chartData));
    }
}