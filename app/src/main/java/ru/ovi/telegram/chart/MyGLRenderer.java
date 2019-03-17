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

public class MyGLRenderer implements GLSurfaceView.Renderer, Touchable {
    private final CoordinatesConverter converter;
    private final ChartViewModel viewModel;

    private GridLines gridLines;
    private Lines lines;
    private List<BaseChartElement> elementList = new ArrayList<>();

    MyGLRenderer(CoordinatesConverter converter, ChartViewModel viewModel) {
        this.converter = converter;
        this.viewModel = viewModel;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        Stream.of(elementList).forEach(BaseChartElement::draw);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        converter.setNewSizes(width, height);
        GLES20.glViewport(0, 0, width, height);

        final RectF mainChartRect = new RectF(100f, 0f, (float) width, (float) height - 200);
        gridLines = new GridLines(mainChartRect, converter, viewModel);
        elementList.add(gridLines);

        lines = new Lines(mainChartRect, converter, viewModel);
        elementList.add(lines);


        final RectF previewChartRect = new RectF(100f, height - 200, (float) width, (float) height);
        viewModel.setMaxRightOffset(previewChartRect.width());
        elementList.add(new PreviewLines(previewChartRect, converter, viewModel));
        elementList.add(new Preview(previewChartRect, converter, viewModel));

        if (viewModel.isInitialized()) {
            Stream.of(elementList).forEach(BaseChartElement::prepareForDraw);
        }
    }

    public void setChartData(ChartData chartData) {
        viewModel.init(chartData);
        Stream.of(elementList).forEach(BaseChartElement::prepareForDraw);
    }

    @Override
    public Touchable onTouched(float x, float y) {
        return ChartValuesUtil.findTouched(elementList, x, y);
    }

    public void update() {
        Stream.of(elementList).forEach(BaseChartElement::update);
    }
}