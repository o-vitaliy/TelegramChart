package ru.ovi.telegram.chart;

import android.graphics.RectF;
import android.opengl.GLES20;
import ru.ovi.telegram.chart.data.ChartData;

public abstract class BaseChartElement {

    static final int COORDS_PER_VERTEX = 2;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position =  vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    protected final int mProgram;
    protected final RectF bounds;
    protected final CoordinatesConverter converter;

    public BaseChartElement(RectF bounds, CoordinatesConverter converter) {
        this.bounds = bounds;
        this.converter = converter;

        // prepare shaders and OpenGL program
        int vertexShader = ChartUtils.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = ChartUtils.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    public abstract void draw();

    public abstract void prepareForDraw(final ChartData chartData);
}
