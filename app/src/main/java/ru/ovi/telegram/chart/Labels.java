package ru.ovi.telegram.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.text.TextPaint;
import ru.ovi.telegram.chart.t.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;
import static android.opengl.GLES20.glEnableVertexAttribArray;

public class Labels {

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
                    "\n" +
                    "uniform sampler2D u_TextureUnit;\n" +
                    "varying vec2 v_Texture;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_FragColor = texture2D(u_TextureUnit, v_Texture);\n" +
                    "\n" +
                    "}\n";

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT + TEXTURE_COUNT) * 4;

    protected final int mProgram;

    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private FloatBuffer vertexData;

    private int textWidth;
    private int textHeight;
    private TextPaint tp;
    final String text = "Hello world";
    private int texture;

    float[] vertices = {
            -1, 1, 1, 0, 0,
            -1, -1, 1, 0, 1,
            1, 1, 1, 1, 0,
            1, -1, 1, 1, 1,
    };

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMatrix = new float[16];

    Labels() {
        tp = new TextPaint();
        tp.setTextSize(32);
        tp.setColor(Color.BLACK);

        textHeight = (int) tp.getTextSize();
        textWidth = (int) tp.measureText(text);

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
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);

        aPositionLocation = glGetAttribLocation(mProgram, "a_Position");
        aTextureLocation = glGetAttribLocation(mProgram, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(mProgram, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(mProgram, "u_Matrix");

        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.setIdentityM(mMatrix, 0);

        float width = (int) CoordinatesConverter.INSTANCE.getWidth();
        float height = (int) CoordinatesConverter.INSTANCE.getHeight();

        float ratioX = textWidth / Math.min(width, height);
        float ratioY = textHeight / Math.min(width, height);

        if (width < height) {
            ratioY *= width / height;
        } else {
            ratioX *= height / width;
        }

        Matrix.scaleM(mMatrix, 0, ratioX, ratioY, 1);
        Matrix.translateM(mMatrix, 0, -(width / textWidth) + 1, (height / textHeight) - 1, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);

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


    public int loadTexture() {
        Bitmap bitmap = Bitmap.createBitmap((int) textWidth, (int) textHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.RED);

        canvas.drawText(text, 0, tp.getTextSize(), tp);

        // создание объекта текстуры
        final int[] textureIds = new int[1];

        //создаем пустой массив из одного элемента
        //в этот массив OpenGL ES запишет свободный номер текстуры,
        // получаем свободное имя текстуры, которое будет записано в names[0]
        glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            return 0;
        }

        //glActiveTexture — select active texture unit
        glActiveTexture(GL_TEXTURE0);
        //делаем текстуру с именем textureIds[0] текущей
        glBindTexture(GL_TEXTURE_2D, textureIds[0]);

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

        // сброс приязки объекта текстуры к блоку текстуры
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureIds[0];
    }
}
