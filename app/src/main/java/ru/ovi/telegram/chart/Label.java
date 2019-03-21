package ru.ovi.telegram.chart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

public class Label extends BaseDrawingElement {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "attribute vec4 a_Position;\n" +
                    "attribute vec2 a_Texture;" +
                    "uniform mat4 u_Matrix;\n" +
                    "varying vec2 v_Texture;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
                    "    v_Texture = a_Texture;\n" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform float u_alpha;\n" +
                    "uniform sampler2D u_TextureUnit;\n" +
                    "varying vec2 v_Texture;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_FragColor = vec4(texture2D(u_TextureUnit, v_Texture).rgb, u_alpha) ;\n" +
                    "\n" +
                    "}\n";

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT + TEXTURE_COUNT) * 4;

    private final int mProgram;

    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;
    private int uAlpha;

    private FloatBuffer vertexData;

    private int textWidth;
    private int textHeight;
    private String text;
    private int texture = -1;
    private float alpha = 1;

    private float[] vertices = {
            -1, 1, 1, 0, 0,
            -1, -1, 1, 0, 1,
            1, 1, 1, 1, 0,
            1, -1, 1, 1, 1,
    };

    private float animation[] = new float[16];

    private float[] mMatrix = new float[16];

    private boolean textureValid;

    protected Label(RectF bounds, CoordinatesConverter converter, ChartViewModel chartViewModel) {
        super(bounds, converter, chartViewModel);

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

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        vertexData.position(0);

        texture = loadTexture();

        ChartUtils.checkGlError("");

        Matrix.setIdentityM(mMatrix, 0);
        ChartUtils.checkGlError("");
    }


    @Override
    public void draw() {
        if (text == null) return;
        regenerateTexture();

        GLES20.glUseProgram(mProgram);
        aPositionLocation = glGetAttribLocation(mProgram, "a_Position");
        aTextureLocation = glGetAttribLocation(mProgram, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(mProgram, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(mProgram, "u_Matrix");
        uAlpha = glGetUniformLocation(mProgram, "u_alpha");
        final float result[] = new float[16];
        Matrix.multiplyMM(result, 0, mMatrix, 0, animation, 0);

        glUniform1f(uAlpha, alpha);
        glUniformMatrix4fv(uMatrixLocation, 1, false, result, 0);

        // координаты вершин
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // координаты текстур
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);

        // помещаем текстуру в target 2D юнита 0
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);

        // юнит текстуры
        glUniform1i(uTextureUnitLocation, 0);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        ChartUtils.checkGlError("");
    }

    private int loadTexture() {
        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);
        return textureIds[0];
    }

    @Override
    void prepareForDraw() {

    }

    @Override
    void update() {
       /* alpha -= 0.05;
        Matrix.translateM(animation, 0, 0, -1, 0);*/
    }

    public void setText(String text) {
        if (text.equals(this.text)) return;
        this.text = text;
        textWidth = (int) Math.ceil(chartViewModel.getTextPaint().measureText(text));
        textHeight = (int) Math.ceil(chartViewModel.getTextSize());
        textureValid = false;


        float width = converter.getWidth();
        float height = converter.getHeight();

        float ratioX = textWidth / Math.min(width, height);
        float ratioY = textHeight / Math.min(width, height);

        if (width < height) {
            ratioY *= width / height;
        } else {
            ratioX *= height / width;
        }
        Matrix.setIdentityM(mMatrix, 0);
        Matrix.scaleM(mMatrix, 0, ratioX, ratioY, 1);
        Matrix.translateM(mMatrix, 0, 1 - (width / textWidth), -((bounds.bottom - height / 2) / textHeight) * 2, 0);

        Matrix.setIdentityM(animation, 0);
    }

    private void regenerateTexture() {
        if (textureValid) return;
        textureValid = true;

        // помещаем текстуру в target 2D юнита 0
        glBindTexture(GL_TEXTURE_2D, texture);

        final Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.RED);

        canvas.drawText(text, 0, textHeight, chartViewModel.getTextPaint());
        //учитываем прозрачность текстуры
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        //включаем фильтры
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //переписываем Bitmap в память видеокарты
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        // удаляем Bitmap из памяти, т.к. картинка уже переписана в видеопамять
        bitmap.recycle();
        glBindTexture(GL_TEXTURE_2D, 0);
        ChartUtils.checkGlError("");
    }
}
