package ru.ovi.telegram.chart;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import ru.ovi.telegram.chart.data.ChartData;

public class ChartView extends GLSurfaceView {
    private final CoordinatesConverter converter = new CoordinatesConverter();
    private final ChartViewModel viewModel = new ChartViewModel();

    private float mPreviousX;
    private float mPreviousY;

    private MyGLRenderer renderer;

    private Touchable touchable;

    public ChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        renderer = new MyGLRenderer(converter, viewModel);
        setRenderer(renderer);
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchable = renderer.onTouched(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                moved(touchable, dx);
                requestRender();
                break;

            default:
                touchable = null;
                break;
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public void setChartData(ChartData chartData) {
        renderer.setChartData(chartData);
        requestRender();
    }

    private void moved(final Touchable touchable, float delta) {
        if (touchable instanceof PreviewRegionBound) {
            final PreviewRegionBoundType type = ((PreviewRegionBound) touchable).type;
            if (type.equals(PreviewRegionBoundType.LEFT))
                viewModel.changeLeftOffset(delta);
            else
                viewModel.changeRightOffset(delta);

            renderer.update();
        }
    }
}
