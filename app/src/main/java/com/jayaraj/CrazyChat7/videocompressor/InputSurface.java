package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Build;
import android.view.Surface;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class InputSurface {
    private static final boolean VERBOSE = false;
    private static final int EGL_RECORDABLE_ANDROID = 0x3142;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private Surface mSurface;

    public InputSurface(final Surface surface) {
        if (surface == null) {
            throw new NullPointerException();
        }
        this.mSurface = surface;
        this.eglSetup();
    }

    private void eglSetup() {
        this.mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (this.mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        final int[] version = new int[2];
        if (!EGL14.eglInitialize(this.mEGLDisplay, version, 0, version, 1)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }

        final int[] attribList = {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, InputSurface.EGL_OPENGL_ES2_BIT,
                InputSurface.EGL_RECORDABLE_ANDROID, 1,
                EGL14.EGL_NONE
        };
        final EGLConfig[] configs = new EGLConfig[1];
        final int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(this.mEGLDisplay, attribList, 0, configs, 0, configs.length,
                numConfigs, 0)) {
            throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
        }

        final int[] attrib_list = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };

        this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, configs[0], EGL14.EGL_NO_CONTEXT, attrib_list, 0);
        this.checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }

        final int[] surfaceAttribs = {
                EGL14.EGL_NONE
        };
        this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, configs[0], this.mSurface,
                surfaceAttribs, 0);
        this.checkEglError("eglCreateWindowSurface");
        if (this.mEGLSurface == null) {
            throw new RuntimeException("surface was null");
        }
    }

    public void release() {
        if (EGL14.eglGetCurrentContext().equals(this.mEGLContext)) {
            EGL14.eglMakeCurrent(this.mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        }
        EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
        EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mSurface = null;
    }

    public void makeCurrent() {
        if (!EGL14.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public boolean swapBuffers() {
        return EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void setPresentationTime(final long nsecs) {
        EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, nsecs);
    }

    private void checkEglError(final String msg) {
        boolean failed = false;
        int error;
        while ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            failed = true;
        }
        if (failed) {
            throw new RuntimeException("EGL error encountered (see log)");
        }
    }
}
