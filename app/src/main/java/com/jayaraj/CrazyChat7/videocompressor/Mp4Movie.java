package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.googlecode.mp4parser.util.Matrix;

import java.io.File;
import java.util.ArrayList;

@TargetApi(16)
public class Mp4Movie {
    private Matrix matrix = Matrix.ROTATE_0;
    private final ArrayList<Track> tracks = new ArrayList<Track>();
    private File cacheFile;
    private int width;
    private int height;

    public Matrix getMatrix() {
        return this.matrix;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setCacheFile(final File file) {
        this.cacheFile = file;
    }

    public void setRotation(final int angle) {
        if (angle == 0) {
            this.matrix = Matrix.ROTATE_0;
        } else if (angle == 90) {
            this.matrix = Matrix.ROTATE_90;
        } else if (angle == 180) {
            this.matrix = Matrix.ROTATE_180;
        } else if (angle == 270) {
            this.matrix = Matrix.ROTATE_270;
        }
    }

    public void setSize(final int w, final int h) {
        this.width = w;
        this.height = h;
    }

    public ArrayList<Track> getTracks() {
        return this.tracks;
    }

    public File getCacheFile() {
        return this.cacheFile;
    }

    public void addSample(final int trackIndex, final long offset, final MediaCodec.BufferInfo bufferInfo) throws Exception {
        if (trackIndex < 0 || trackIndex >= this.tracks.size()) {
            return;
        }
        final Track track = this.tracks.get(trackIndex);
        track.addSample(offset, bufferInfo);
    }

    public int addTrack(final MediaFormat mediaFormat, final boolean isAudio) throws Exception {
        this.tracks.add(new Track(this.tracks.size(), mediaFormat, isAudio));
        return this.tracks.size() - 1;
    }
}
