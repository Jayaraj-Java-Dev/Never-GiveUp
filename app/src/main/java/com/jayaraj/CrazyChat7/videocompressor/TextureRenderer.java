package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@TargetApi(16)
public class TextureRenderer {

    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * TextureRenderer.FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private static final float[] mTriangleVerticesData = {
            -1.0f, -1.0f, 0, 0.f, 0.f,
            1.0f, -1.0f, 0, 1.f, 0.f,
            -1.0f, 1.0f, 0, 0.f, 1.f,
            1.0f, 1.0f, 0, 1.f, 1.f,
    };
    private final FloatBuffer mTriangleVertices;

    private static final String VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;\n" +
            "uniform mat4 uSTMatrix;\n" +
            "attribute vec4 aPosition;\n" +
            "attribute vec4 aTextureCoord;\n" +
            "varying vec2 vTextureCoord;\n" +
            "void main() {\n" +
            "  gl_Position = uMVPMatrix * aPosition;\n" +
            "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n" +
            "}\n";

    private static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform samplerExternalOES sTexture;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
            "}\n";

    private final float[] mMVPMatrix = new float[16];
    private final float[] mSTMatrix = new float[16];
    private int mProgram;
    private int mTextureID = -12345;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int maPositionHandle;
    private int maTextureHandle;
    private int rotationAngle;

    public TextureRenderer(final int rotation) {
        this.rotationAngle = rotation;
        this.mTriangleVertices = ByteBuffer.allocateDirect(TextureRenderer.mTriangleVerticesData.length * TextureRenderer.FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTriangleVertices.put(TextureRenderer.mTriangleVerticesData).position(0);
        Matrix.setIdentityM(this.mSTMatrix, 0);
    }

    public int getTextureId() {
        return this.mTextureID;
    }

    public void drawFrame(final SurfaceTexture st, final boolean invert) {
        this.checkGlError("onDrawFrame start");
        st.getTransformMatrix(this.mSTMatrix);

        if (invert) {
            this.mSTMatrix[5] = -this.mSTMatrix[5];
            this.mSTMatrix[13] = 1.0f - this.mSTMatrix[13];
        }

        GLES20.glUseProgram(this.mProgram);
        this.checkGlError("glUseProgram");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, this.mTextureID);
        this.mTriangleVertices.position(TextureRenderer.TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(this.maPositionHandle, 3, GLES20.GL_FLOAT, false, TextureRenderer.TRIANGLE_VERTICES_DATA_STRIDE_BYTES, this.mTriangleVertices);
        this.checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(this.maPositionHandle);
        this.checkGlError("glEnableVertexAttribArray maPositionHandle");
        this.mTriangleVertices.position(TextureRenderer.TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(this.maTextureHandle, 2, GLES20.GL_FLOAT, false, TextureRenderer.TRIANGLE_VERTICES_DATA_STRIDE_BYTES, this.mTriangleVertices);
        this.checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(this.maTextureHandle);
        this.checkGlError("glEnableVertexAttribArray maTextureHandle");
        GLES20.glUniformMatrix4fv(this.muSTMatrixHandle, 1, false, this.mSTMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        this.checkGlError("glDrawArrays");
        GLES20.glFinish();
    }

    public void surfaceCreated() {
        this.mProgram = this.createProgram(TextureRenderer.VERTEX_SHADER, TextureRenderer.FRAGMENT_SHADER);
        if (this.mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
        this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "aPosition");
        this.checkGlError("glGetAttribLocation aPosition");
        if (this.maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
        this.checkGlError("glGetAttribLocation aTextureCoord");
        if (this.maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
        this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
        this.checkGlError("glGetUniformLocation uMVPMatrix");
        if (this.muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }
        this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
        this.checkGlError("glGetUniformLocation uSTMatrix");
        if (this.muSTMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }
        final int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        this.mTextureID = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, this.mTextureID);
        this.checkGlError("glBindTexture mTextureID");
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        this.checkGlError("glTexParameter");

        Matrix.setIdentityM(this.mMVPMatrix, 0);
        if (this.rotationAngle != 0) {
            Matrix.rotateM(this.mMVPMatrix, 0, this.rotationAngle, 0, 0, 1);
        }
    }

    public void changeFragmentShader(final String fragmentShader) {
        GLES20.glDeleteProgram(this.mProgram);
        this.mProgram = this.createProgram(TextureRenderer.VERTEX_SHADER, fragmentShader);
        if (this.mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
    }

    private int loadShader(final int shaderType, final String source) {
        int shader = GLES20.glCreateShader(shaderType);
        this.checkGlError("glCreateShader type=" + shaderType);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        final int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        return shader;
    }

    private int createProgram(final String vertexSource, final String fragmentSource) {
        final int vertexShader = this.loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        final int pixelShader = this.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        this.checkGlError("glCreateProgram");
        if (program == 0) {
            return 0;
        }
        GLES20.glAttachShader(program, vertexShader);
        this.checkGlError("glAttachShader");
        GLES20.glAttachShader(program, pixelShader);
        this.checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    public void checkGlError(final String op) {
        final int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
