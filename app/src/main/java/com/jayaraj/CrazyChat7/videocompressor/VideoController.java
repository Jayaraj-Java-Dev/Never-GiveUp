package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@SuppressLint("NewApi")
public class VideoController {
    static final int COMPRESS_QUALITY_HIGH = 1;
    static final int COMPRESS_QUALITY_MEDIUM = 2;
    static final int COMPRESS_QUALITY_LOW = 3;

    public static File cachedFile;
    public String path;

    public static final String MIME_TYPE = "video/avc";
    private static final int PROCESSOR_TYPE_OTHER = 0;
    private static final int PROCESSOR_TYPE_QCOM = 1;
    private static final int PROCESSOR_TYPE_INTEL = 2;
    private static final int PROCESSOR_TYPE_MTK = 3;
    private static final int PROCESSOR_TYPE_SEC = 4;
    private static final int PROCESSOR_TYPE_TI = 5;
    private static volatile VideoController Instance;
    private boolean videoConvertFirstWrite = true;

    interface CompressProgressListener {
        void onProgress(float percent);
    }

    public static VideoController getInstance() {
        VideoController localInstance = VideoController.Instance;
        if (localInstance == null) {
            synchronized (VideoController.class) {
                localInstance = VideoController.Instance;
                if (localInstance == null) {
                    VideoController.Instance = localInstance = new VideoController();
                }
            }
        }
        return localInstance;
    }

    @SuppressLint("NewApi")
    public static int selectColorFormat(final MediaCodecInfo codecInfo, final String mimeType) {
        final MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        int lastColorFormat = 0;
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            final int colorFormat = capabilities.colorFormats[i];
            if (VideoController.isRecognizedFormat(colorFormat)) {
                lastColorFormat = colorFormat;
                if (!(codecInfo.getName().equals("OMX.SEC.AVC.Encoder") && colorFormat == 19)) {
                    return colorFormat;
                }
            }
        }
        return lastColorFormat;
    }

    private static boolean isRecognizedFormat(final int colorFormat) {
        switch (colorFormat) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                return false;
        }
    }

    public static native int convertVideoFrame(ByteBuffer src, ByteBuffer dest, int destFormat, int width, int height, int padding, int swap);

    private void didWriteData(boolean last, boolean error) {
        boolean firstWrite = this.videoConvertFirstWrite;
        if (firstWrite) {
            this.videoConvertFirstWrite = false;
        }
    }

    public static class VideoConvertRunnable implements Runnable {

        private final String videoPath;
        private final String destPath;

        private VideoConvertRunnable(final String videoPath, final String destPath) {
            this.videoPath = videoPath;
            this.destPath = destPath;
        }

        public static void runConversion(String videoPath, String destPath) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final VideoConvertRunnable wrapper = new VideoConvertRunnable(videoPath, destPath);
                        final Thread th = new Thread(wrapper, "VideoConvertRunnable");
                        th.start();
                        th.join();
                    } catch (final Exception e) {
                        Log.e("tmessages", e.getMessage());
                    }
                }
            }).start();
        }

        @Override
        public void run() {
            getInstance().convertVideo(this.videoPath, this.destPath, 0, null);
        }
    }

    public static MediaCodecInfo selectCodec(final String mimeType) {
        final int numCodecs = MediaCodecList.getCodecCount();
        MediaCodecInfo lastCodecInfo = null;
        for (int i = 0; i < numCodecs; i++) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            final String[] types = codecInfo.getSupportedTypes();
            for (final String type : types) {
                if (type.equalsIgnoreCase(mimeType)) {
                    lastCodecInfo = codecInfo;
                    if (!lastCodecInfo.getName().equals("OMX.SEC.avc.enc")) {
                        return lastCodecInfo;
                    } else if (lastCodecInfo.getName().equals("OMX.SEC.AVC.Encoder")) {
                        return lastCodecInfo;
                    }
                }
            }
        }
        return lastCodecInfo;
    }

    /**
     * Background conversion for queueing tasks
     * @param path source file to compress
     * @param dest destination directory to put result
     */

public void scheduleVideoConvert(final String path, final String dest) {
    this.startVideoConvertFromQueue(path, dest);
    }

    private void startVideoConvertFromQueue(final String path, final String dest) {
        VideoConvertRunnable.runConversion(path, dest);
    }

    @TargetApi(16)
    private long readAndWriteTrack(final MediaExtractor extractor, final MP4Builder mediaMuxer, final MediaCodec.BufferInfo info, final long start, final long end, final File file, final boolean isAudio) throws Exception {
        final int trackIndex = this.selectTrack(extractor, isAudio);
        if (trackIndex >= 0) {
            extractor.selectTrack(trackIndex);
            final MediaFormat trackFormat = extractor.getTrackFormat(trackIndex);
            final int muxerTrackIndex = mediaMuxer.addTrack(trackFormat, isAudio);
            final int maxBufferSize = trackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
            boolean inputDone = false;
            if (start > 0) {
                extractor.seekTo(start, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
            } else {
                extractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
            }
            final ByteBuffer buffer = ByteBuffer.allocateDirect(maxBufferSize);
            long startTime = -1;

            while (!inputDone) {

                boolean eof = false;
                final int index = extractor.getSampleTrackIndex();
                if (index == trackIndex) {
                    info.size = extractor.readSampleData(buffer, 0);

                    if (info.size < 0) {
                        info.size = 0;
                        eof = true;
                    } else {
                        info.presentationTimeUs = extractor.getSampleTime();
                        if (start > 0 && startTime == -1) {
                            startTime = info.presentationTimeUs;
                        }
                        if (end < 0 || info.presentationTimeUs < end) {
                            info.offset = 0;
                            info.flags = extractor.getSampleFlags();
                            if (mediaMuxer.writeSampleData(muxerTrackIndex, buffer, info, isAudio)) {
                                // didWriteData(messageObject, file, false, false);
                            }
                            extractor.advance();
                        } else {
                            eof = true;
                        }
                    }
                } else if (index == -1) {
                    eof = true;
                }
                if (eof) {
                    inputDone = true;
                }
            }

            extractor.unselectTrack(trackIndex);
            return startTime;
        }
        return -1;
    }

    @TargetApi(16)
    private int selectTrack(final MediaExtractor extractor, final boolean audio) {
        final int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            final MediaFormat format = extractor.getTrackFormat(i);
            final String mime = format.getString(MediaFormat.KEY_MIME);
            if (audio) {
                if (mime.startsWith("audio/")) {
                    return i;
                }
            } else {
                if (mime.startsWith("video/")) {
                    return i;
                }
            }
        }
        return -5;
    }

    /**
     * Perform the actual video compression. Processes the frames and does the magic
     * @param sourcePath the source uri for the file as per
     * @param destinationPath the destination directory where compressed video is eventually saved
     * @return
     */
    @TargetApi(16)
    public boolean  convertVideo(String sourcePath, final String destinationPath, final int quality, final CompressProgressListener listener) {
        path=sourcePath;

        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this.path);
        final String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        final String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        final String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        final long duration = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;

        final long startTime = -1;
        final long endTime = -1;

        int rotationValue = Integer.valueOf(rotation);
        final int originalWidth = Integer.valueOf(width);
        final int originalHeight = Integer.valueOf(height);

        int resultWidth;
        int resultHeight;
        final int bitrate;
        switch (quality) {
            default:
            case VideoController.COMPRESS_QUALITY_HIGH:
                resultWidth = originalWidth * 2 / 3;
                resultHeight = originalHeight * 2 / 3;
                bitrate = resultWidth * resultHeight * 30;
                break;
            case VideoController.COMPRESS_QUALITY_MEDIUM:
                resultWidth = originalWidth / 2;
                resultHeight = originalHeight / 2;
                bitrate = resultWidth * resultHeight * 10;
                break;
            case VideoController.COMPRESS_QUALITY_LOW:
                resultWidth = originalWidth / 2;
                resultHeight = originalHeight / 2;
                bitrate = (resultWidth/2) * (resultHeight/2) * 10;
                break;
        }

        int rotateRender = 0;

        final File cacheFile = new File(destinationPath);

        if (Build.VERSION.SDK_INT < 18 && resultHeight > resultWidth && resultWidth != originalWidth && resultHeight != originalHeight) {
            final int temp = resultHeight;
            resultHeight = resultWidth;
            resultWidth = temp;
            rotationValue = 90;
            rotateRender = 270;
        } else if (Build.VERSION.SDK_INT > 20) {
            if (rotationValue == 90) {
                final int temp = resultHeight;
                resultHeight = resultWidth;
                resultWidth = temp;
                rotationValue = 0;
                rotateRender = 270;
            } else if (rotationValue == 180) {
                rotateRender = 180;
                rotationValue = 0;
            } else if (rotationValue == 270) {
                final int temp = resultHeight;
                resultHeight = resultWidth;
                resultWidth = temp;
                rotationValue = 0;
                rotateRender = 90;
            }
        }


        final File inputFile = new File(this.path);
        if (!inputFile.canRead()) {
            this.didWriteData(true, true);
            return false;
        }

        this.videoConvertFirstWrite = true;
        boolean error = false;
        long videoStartTime = startTime;

        final long time = System.currentTimeMillis();

        if (resultWidth != 0 && resultHeight != 0) {
            MP4Builder mediaMuxer = null;
            MediaExtractor extractor = null;

            try {
                final MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                final Mp4Movie movie = new Mp4Movie();
                movie.setCacheFile(cacheFile);
                movie.setRotation(rotationValue);
                movie.setSize(resultWidth, resultHeight);
                mediaMuxer = new MP4Builder().createMovie(movie);
                extractor = new MediaExtractor();
                extractor.setDataSource(inputFile.toString());


                if (resultWidth != originalWidth || resultHeight != originalHeight) {
                    final int videoIndex;
                    videoIndex = this.selectTrack(extractor, false);

                    if (videoIndex >= 0) {
                        MediaCodec decoder = null;
                        MediaCodec encoder = null;
                        InputSurface inputSurface = null;
                        OutputSurface outputSurface = null;

                        try {
                            long videoTime = -1;
                            boolean outputDone = false;
                            boolean inputDone = false;
                            boolean decoderDone = false;
                            int swapUV = 0;
                            int videoTrackIndex = -5;

                            final int colorFormat;
                            int processorType = VideoController.PROCESSOR_TYPE_OTHER;
                            final String manufacturer = Build.MANUFACTURER.toLowerCase();
                            if (Build.VERSION.SDK_INT < 18) {
                                final MediaCodecInfo codecInfo = VideoController.selectCodec(VideoController.MIME_TYPE);
                                colorFormat = VideoController.selectColorFormat(codecInfo, VideoController.MIME_TYPE);
                                if (colorFormat == 0) {
                                    throw new RuntimeException("no supported color format");
                                }
                                final String codecName = codecInfo.getName();
                                if (codecName.contains("OMX.qcom.")) {
                                    processorType = VideoController.PROCESSOR_TYPE_QCOM;
                                    if (Build.VERSION.SDK_INT == 16) {
                                        if (manufacturer.equals("lge") || manufacturer.equals("nokia")) {
                                            swapUV = 1;
                                        }
                                    }
                                } else if (codecName.contains("OMX.Intel.")) {
                                    processorType = VideoController.PROCESSOR_TYPE_INTEL;
                                } else if (codecName.equals("OMX.MTK.VIDEO.ENCODER.AVC")) {
                                    processorType = VideoController.PROCESSOR_TYPE_MTK;
                                } else if (codecName.equals("OMX.SEC.AVC.Encoder")) {
                                    processorType = VideoController.PROCESSOR_TYPE_SEC;
                                    swapUV = 1;
                                } else if (codecName.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                                    processorType = VideoController.PROCESSOR_TYPE_TI;
                                }
                                Log.e("tmessages", "codec = " + codecInfo.getName() + " manufacturer = " + manufacturer + "device = " + Build.MODEL);
                            } else {
                                colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface;
                            }
                            Log.e("tmessages", "colorFormat = " + colorFormat);

                            int resultHeightAligned = resultHeight;
                            int padding = 0;
                            int bufferSize = resultWidth * resultHeight * 3 / 2;
                            if (processorType == VideoController.PROCESSOR_TYPE_OTHER) {
                                if (resultHeight % 16 != 0) {
                                    resultHeightAligned += (16 - (resultHeight % 16));
                                    padding = resultWidth * (resultHeightAligned - resultHeight);
                                    bufferSize += padding * 5 / 4;
                                }
                            } else if (processorType == VideoController.PROCESSOR_TYPE_QCOM) {
                                if (!manufacturer.equalsIgnoreCase("lge")) {
                                    final int uvoffset = (resultWidth * resultHeight + 2047) & ~2047;
                                    padding = uvoffset - (resultWidth * resultHeight);
                                    bufferSize += padding;
                                }
                            } else if (processorType == VideoController.PROCESSOR_TYPE_TI) {
                                //resultHeightAligned = 368;
                                //bufferSize = resultWidth * resultHeightAligned * 3 / 2;
                                //resultHeightAligned += (16 - (resultHeight % 16));
                                //padding = resultWidth * (resultHeightAligned - resultHeight);
                                //bufferSize += padding * 5 / 4;
                            } else if (processorType == VideoController.PROCESSOR_TYPE_MTK) {
                                if (manufacturer.equals("baidu")) {
                                    resultHeightAligned += (16 - (resultHeight % 16));
                                    padding = resultWidth * (resultHeightAligned - resultHeight);
                                    bufferSize += padding * 5 / 4;
                                }
                            }

                            extractor.selectTrack(videoIndex);
                            if (startTime > 0) {
                                extractor.seekTo(startTime, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                            } else {
                                extractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                            }
                            final MediaFormat inputFormat = extractor.getTrackFormat(videoIndex);

                            final MediaFormat outputFormat = MediaFormat.createVideoFormat(VideoController.MIME_TYPE, resultWidth, resultHeight);
                            outputFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
                            outputFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
                            outputFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
                            outputFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
                            if (Build.VERSION.SDK_INT < 18) {
                                outputFormat.setInteger("stride", resultWidth + 32);
                                outputFormat.setInteger("slice-height", resultHeight);
                            }

                            encoder = MediaCodec.createEncoderByType(VideoController.MIME_TYPE);
                            encoder.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                            if (Build.VERSION.SDK_INT >= 18) {
                                inputSurface = new InputSurface(encoder.createInputSurface());
                                inputSurface.makeCurrent();
                            }
                            encoder.start();

                            decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME));
                            if (Build.VERSION.SDK_INT >= 18) {
                                outputSurface = new OutputSurface();
                            } else {
                                outputSurface = new OutputSurface(resultWidth, resultHeight, rotateRender);
                            }
                            decoder.configure(inputFormat, outputSurface.getSurface(), null, 0);
                            decoder.start();

                            final int TIMEOUT_USEC = 2500;
                            ByteBuffer[] decoderInputBuffers = null;
                            ByteBuffer[] encoderOutputBuffers = null;
                            ByteBuffer[] encoderInputBuffers = null;
                            if (Build.VERSION.SDK_INT < 21) {
                                decoderInputBuffers = decoder.getInputBuffers();
                                encoderOutputBuffers = encoder.getOutputBuffers();
                                if (Build.VERSION.SDK_INT < 18) {
                                    encoderInputBuffers = encoder.getInputBuffers();
                                }
                            }

                            while (!outputDone) {
                                if (!inputDone) {
                                    boolean eof = false;
                                    final int index = extractor.getSampleTrackIndex();
                                    if (index == videoIndex) {
                                        final int inputBufIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (inputBufIndex >= 0) {
                                            final ByteBuffer inputBuf;
                                            if (Build.VERSION.SDK_INT < 21) {
                                                inputBuf = decoderInputBuffers[inputBufIndex];
                                            } else {
                                                inputBuf = decoder.getInputBuffer(inputBufIndex);
                                            }
                                            final int chunkSize = extractor.readSampleData(inputBuf, 0);
                                            if (chunkSize < 0) {
                                                decoder.queueInputBuffer(inputBufIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                                inputDone = true;
                                            } else {
                                                decoder.queueInputBuffer(inputBufIndex, 0, chunkSize, extractor.getSampleTime(), 0);
                                                extractor.advance();
                                            }
                                        }
                                    } else if (index == -1) {
                                        eof = true;
                                    }
                                    if (eof) {
                                        final int inputBufIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (inputBufIndex >= 0) {
                                            decoder.queueInputBuffer(inputBufIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                            inputDone = true;
                                        }
                                    }
                                }

                                boolean decoderOutputAvailable = !decoderDone;
                                boolean encoderOutputAvailable = true;
                                while (decoderOutputAvailable || encoderOutputAvailable) {
                                    final int encoderStatus = encoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                                    if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                                        encoderOutputAvailable = false;
                                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                                        if (Build.VERSION.SDK_INT < 21) {
                                            encoderOutputBuffers = encoder.getOutputBuffers();
                                        }
                                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                        final MediaFormat newFormat = encoder.getOutputFormat();
                                        if (videoTrackIndex == -5) {
                                            videoTrackIndex = mediaMuxer.addTrack(newFormat, false);
                                        }
                                    } else if (encoderStatus < 0) {
                                        throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                                    } else {
                                        final ByteBuffer encodedData;
                                        if (Build.VERSION.SDK_INT < 21) {
                                            encodedData = encoderOutputBuffers[encoderStatus];
                                        } else {
                                            encodedData = encoder.getOutputBuffer(encoderStatus);
                                        }
                                        if (encodedData == null) {
                                            throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                                        }
                                        if (info.size > 1) {
                                            if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0) {
                                                if (mediaMuxer.writeSampleData(videoTrackIndex, encodedData, info, false)) {
                                                    this.didWriteData(false, false);
                                                }
                                            } else if (videoTrackIndex == -5) {
                                                final byte[] csd = new byte[info.size];
                                                encodedData.limit(info.offset + info.size);
                                                encodedData.position(info.offset);
                                                encodedData.get(csd);
                                                ByteBuffer sps = null;
                                                ByteBuffer pps = null;
                                                for (int a = info.size - 1; a >= 0; a--) {
                                                    if (a > 3) {
                                                        if (csd[a] == 1 && csd[a - 1] == 0 && csd[a - 2] == 0 && csd[a - 3] == 0) {
                                                            sps = ByteBuffer.allocate(a - 3);
                                                            pps = ByteBuffer.allocate(info.size - (a - 3));
                                                            sps.put(csd, 0, a - 3).position(0);
                                                            pps.put(csd, a - 3, info.size - (a - 3)).position(0);
                                                            break;
                                                        }
                                                    } else {
                                                        break;
                                                    }
                                                }

                                                final MediaFormat newFormat = MediaFormat.createVideoFormat(VideoController.MIME_TYPE, resultWidth, resultHeight);
                                                if (sps != null && pps != null) {
                                                    newFormat.setByteBuffer("csd-0", sps);
                                                    newFormat.setByteBuffer("csd-1", pps);
                                                }
                                                videoTrackIndex = mediaMuxer.addTrack(newFormat, false);
                                            }
                                        }
                                        outputDone = (info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
                                        encoder.releaseOutputBuffer(encoderStatus, false);
                                    }
                                    if (encoderStatus != MediaCodec.INFO_TRY_AGAIN_LATER) {
                                        continue;
                                    }

                                    if (!decoderDone) {
                                        final int decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                                        if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                                            decoderOutputAvailable = false;
                                        } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {

                                        } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                            final MediaFormat newFormat = decoder.getOutputFormat();
                                            Log.e("tmessages", "newFormat = " + newFormat);
                                        } else if (decoderStatus < 0) {
                                            throw new RuntimeException("unexpected result from decoder.dequeueOutputBuffer: " + decoderStatus);
                                        } else {
                                            boolean doRender;
                                            if (Build.VERSION.SDK_INT >= 18) {
                                                doRender = info.size != 0;
                                            } else {
                                                doRender = info.size != 0 || info.presentationTimeUs != 0;
                                            }
                                            if (endTime > 0 && info.presentationTimeUs >= endTime) {
                                                inputDone = true;
                                                decoderDone = true;
                                                doRender = false;
                                                info.flags |= MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                                            }
                                            if (startTime > 0 && videoTime == -1) {
                                                if (info.presentationTimeUs < startTime) {
                                                    doRender = false;
                                                    Log.e("tmessages", "drop frame startTime = " + startTime + " present time = " + info.presentationTimeUs);
                                                } else {
                                                    videoTime = info.presentationTimeUs;
                                                }
                                            }
                                            decoder.releaseOutputBuffer(decoderStatus, doRender);
                                            if (doRender) {
                                                boolean errorWait = false;
                                                try {
                                                    outputSurface.awaitNewImage();
                                                } catch (final Exception e) {
                                                    errorWait = true;
                                                    Log.e("tmessages", e.getMessage());
                                                }
                                                if (!errorWait) {
                                                    if (Build.VERSION.SDK_INT >= 18) {
                                                        outputSurface.drawImage(false);
                                                        inputSurface.setPresentationTime(info.presentationTimeUs * 1000);

                                                        if (listener != null) {
                                                            listener.onProgress((float) info.presentationTimeUs / (float) duration * 100);
                                                        }

                                                        inputSurface.swapBuffers();
                                                    } else {
                                                        final int inputBufIndex = encoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                        if (inputBufIndex >= 0) {
                                                            outputSurface.drawImage(true);
                                                            final ByteBuffer rgbBuf = outputSurface.getFrame();
                                                            final ByteBuffer yuvBuf = encoderInputBuffers[inputBufIndex];
                                                            yuvBuf.clear();
                                                            VideoController.convertVideoFrame(rgbBuf, yuvBuf, colorFormat, resultWidth, resultHeight, padding, swapUV);
                                                            encoder.queueInputBuffer(inputBufIndex, 0, bufferSize, info.presentationTimeUs, 0);
                                                        } else {
                                                            Log.e("tmessages", "input buffer not available");
                                                        }
                                                    }
                                                }
                                            }
                                            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                                decoderOutputAvailable = false;
                                                Log.e("tmessages", "decoder stream end");
                                                if (Build.VERSION.SDK_INT >= 18) {
                                                    encoder.signalEndOfInputStream();
                                                } else {
                                                    final int inputBufIndex = encoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                    if (inputBufIndex >= 0) {
                                                        encoder.queueInputBuffer(inputBufIndex, 0, 1, info.presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (videoTime != -1) {
                                videoStartTime = videoTime;
                            }
                        } catch (final Exception e) {
                            Log.e("tmessages", e.getMessage());
                            error = true;
                        }

                        extractor.unselectTrack(videoIndex);

                        if (outputSurface != null) {
                            outputSurface.release();
                        }
                        if (inputSurface != null) {
                            inputSurface.release();
                        }
                        if (decoder != null) {
                            decoder.stop();
                            decoder.release();
                        }
                        if (encoder != null) {
                            encoder.stop();
                            encoder.release();
                        }
                    }
                } else {
                    final long videoTime = this.readAndWriteTrack(extractor, mediaMuxer, info, startTime, endTime, cacheFile, false);
                    if (videoTime != -1) {
                        videoStartTime = videoTime;
                    }
                }
                if (!error) {
                    this.readAndWriteTrack(extractor, mediaMuxer, info, videoStartTime, endTime, cacheFile, true);
                }
            } catch (final Exception e) {
                error = true;
                Log.e("tmessages", e.getMessage());
            } finally {
                if (extractor != null) {
                    extractor.release();
                }
                if (mediaMuxer != null) {
                    try {
                        mediaMuxer.finishMovie(false);
                    } catch (final Exception e) {
                        Log.e("tmessages", e.getMessage());
                    }
                }
                Log.e("tmessages", "time = " + (System.currentTimeMillis() - time));
            }
        } else {
            this.didWriteData(true, true);
            return false;
        }
        this.didWriteData(true, error);

        VideoController.cachedFile =cacheFile;

       /* File fdelete = inputFile;
        if (fdelete.exists()) {
            if (fdelete.delete()) {
               Log.e("file Deleted :" ,inputFile.getPath());
            } else {
                Log.e("file not Deleted :" , inputFile.getPath());
            }
        }*/

        //inputFile.delete();
        Log.e("ViratPath", this.path +"");
        Log.e("ViratPath",cacheFile.getPath()+"");
        Log.e("ViratPath",inputFile.getPath()+"");


       /* Log.e("ViratPath",path+"");
        File replacedFile = new File(path);

        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            fos = new FileOutputStream(replacedFile);
             inputStream = new FileInputStream(cacheFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            inputStream.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    //    cacheFile.delete();

       /* try {
           // copyFile(cacheFile,inputFile);
            //inputFile.delete();
            FileUtils.copyFile(cacheFile,inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
         // cacheFile.delete();
       // inputFile.delete();
        return true;
    }

    public static void copyFile(final File src, final File dst) throws IOException
    {
        final FileChannel inChannel = new FileInputStream(src).getChannel();
        final FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try
        {
            inChannel.transferTo(1, inChannel.size(), outChannel);
        }
        finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }
}