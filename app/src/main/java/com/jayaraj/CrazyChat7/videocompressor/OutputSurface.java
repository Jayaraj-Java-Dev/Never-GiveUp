package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

@TargetApi(16)
public class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {

    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    private EGL10 mEGL;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private final Object mFrameSyncObject = new Object();
    private boolean mFrameAvailable;
    private TextureRenderer mTextureRender;
    private int mWidth;
    private int mHeight;
    private int rotateRender;
    private ByteBuffer mPixelBuf;

    public OutputSurface(final int width, final int height, final int rotate) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        this.mWidth = width;
        this.mHeight = height;
        this.rotateRender = rotate;
        this.mPixelBuf = ByteBuffer.allocateDirect(this.mWidth * this.mHeight * 4);
        this.mPixelBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.eglSetup(width, height);
        this.makeCurrent();
        this.setup();
    }

    public OutputSurface() {
        this.setup();
    }

    private void setup() {
        this.mTextureRender = new TextureRenderer(this.rotateRender);
        this.mTextureRender.surfaceCreated();
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    private void eglSetup(final int width, final int height) {
        this.mEGL = (EGL10) EGLContext.getEGL();
        this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        if (this.mEGLDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL10 display");
        }

        if (!this.mEGL.eglInitialize(this.mEGLDisplay, null)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL10");
        }

        final int[] attribList = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_RENDERABLE_TYPE, OutputSurface.EGL_OPENGL_ES2_BIT,
                EGL10.EGL_NONE
        };
        final EGLConfig[] configs = new EGLConfig[1];
        final int[] numConfigs = new int[1];
        if (!this.mEGL.eglChooseConfig(this.mEGLDisplay, attribList, configs, configs.length, numConfigs)) {
            throw new RuntimeException("unable to find RGB888+pbuffer EGL config");
        }
        final int[] attrib_list = {
                OutputSurface.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, configs[0], EGL10.EGL_NO_CONTEXT, attrib_list);
        this.checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }
        final int[] surfaceAttribs = {
                EGL10.EGL_WIDTH, width,
                EGL10.EGL_HEIGHT, height,
                EGL10.EGL_NONE
        };
        this.mEGLSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, configs[0], surfaceAttribs);
        this.checkEglError("eglCreatePbufferSurface");
        if (this.mEGLSurface == null) {
            throw new RuntimeException("surface was null");
        }
    }

    public void release() {
        if (this.mEGL != null) {
            if (this.mEGL.eglGetCurrentContext().equals(this.mEGLContext)) {
                this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            }
            this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        }
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mEGL = null;
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public void makeCurrent() {
        if (this.mEGL == null) {
            throw new RuntimeException("not configured for makeCurrent");
        }
        this.checkEglError("before makeCurrent");
        if (!this.mEGL.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void changeFragmentShader(final String fragmentShader) {
        this.mTextureRender.changeFragmentShader(fragmentShader);
    }

    public void awaitNewImage() {
        final int TIMEOUT_MS = 5000;
        synchronized (this.mFrameSyncObject) {
            while (!this.mFrameAvailable) {
                try {
                    this.mFrameSyncObject.wait(TIMEOUT_MS);
                    if (!this.mFrameAvailable) {
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (final InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
            this.mFrameAvailable = false;
        }
        this.mTextureRender.checkGlError("before updateTexImage");
        this.mSurfaceTexture.updateTexImage();
    }

    public void drawImage(final boolean invert) {
        this.mTextureRender.drawFrame(this.mSurfaceTexture, invert);
    }

    @Override
    public void onFrameAvailable(final SurfaceTexture st) {
        synchronized (this.mFrameSyncObject) {
            if (this.mFrameAvailable) {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
            this.mFrameAvailable = true;
            this.mFrameSyncObject.notifyAll();
        }
    }

    public ByteBuffer getFrame() {
        this.mPixelBuf.rewind();
        GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, this.mPixelBuf);
        return this.mPixelBuf;
    }

    private void checkEglError(final String msg) {
        if (this.mEGL.eglGetError() != EGL10.EGL_SUCCESS) {
            throw new RuntimeException("EGL error encountered (see log)");
        }
    }
}
