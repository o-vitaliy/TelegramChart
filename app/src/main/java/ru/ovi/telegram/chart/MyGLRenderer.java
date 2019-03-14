package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.annimon.stream.Stream;
import ru.ovi.telegram.chart.data.ChartData;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private CoordinatesConverter converter = new CoordinatesConverter();
    private GridLines gridLines;
    private Lines lines;
    private List<BaseChartElement> elementList = new ArrayList<>();
    private ChartViewModel viewModel = new ChartViewModel();

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glLineWidth(10f);

        Stream.of(elementList).forEach(BaseChartElement::draw);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        converter.setNewSizes(width, height);
        GLES20.glViewport(0, 0, width, height);

        gridLines = new GridLines(new RectF(100f, 0f, (float) width, (float) height), converter, viewModel);
        elementList.add(gridLines);

        lines = new Lines(new RectF(100f, 0f, (float) width, (float) height), converter, viewModel);
        elementList.add(lines);

        if (viewModel.isInitialized()) {
            Stream.of(elementList).forEach(BaseChartElement::prepareForDraw);
        }
    }

    public void setChartData(ChartData chartData) {
        viewModel.init(chartData);
        Stream.of(elementList).forEach(BaseChartElement::prepareForDraw);
    }
}