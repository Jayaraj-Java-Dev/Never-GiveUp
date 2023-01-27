package com.jayaraj.CrazyChat7.videocompressor;

import android.os.AsyncTask;

/**
 * Created by Vincent Woo
 * Date: 2017/8/16
 * Time: 15:15
 */

public class VideoCompress {
    private static final String TAG = VideoCompress.class.getSimpleName();

    public static VideoCompressTask compressVideoHigh(final String srcPath, final String destPath, final CompressListener listener) {
        final VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_HIGH);
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoMedium(final String srcPath, final String destPath, final CompressListener listener) {
        final VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_MEDIUM);
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoLow(final String srcPath, final String destPath, final CompressListener listener) {
        final VideoCompressTask task =  new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_LOW);
        task.execute(srcPath, destPath);
        return task;
    }

    private static class VideoCompressTask extends AsyncTask<String, Float, Boolean> {
        private final CompressListener mListener;
        private final int mQuality;

        public VideoCompressTask(final CompressListener listener, final int quality) {
            this.mListener = listener;
            this.mQuality = quality;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (this.mListener != null) {
                this.mListener.onStart();
            }
        }

        @Override
        protected Boolean doInBackground(final String... paths) {
            return VideoController.getInstance().convertVideo(paths[0], paths[1], this.mQuality, new VideoController.CompressProgressListener() {
                @Override
                public void onProgress(final float percent) {
                    VideoCompressTask.this.publishProgress(percent);
                }
            });
        }

        @Override
        protected void onProgressUpdate(final Float... percent) {
            super.onProgressUpdate(percent);
            if (this.mListener != null) {
                this.mListener.onProgress(percent[0]);
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            if (this.mListener != null) {
                if (result) {
                    this.mListener.onSuccess();
                } else {
                    this.mListener.onFail();
                }
            }
        }
    }

    public interface CompressListener {
        void onStart();
        void onSuccess();
        void onFail();
        void onProgress(float percent);
    }
}
