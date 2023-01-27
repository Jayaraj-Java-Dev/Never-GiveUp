package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.coremedia.iso.boxes.AbstractMediaHeaderBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@TargetApi(16)
public class Track {
    private long trackId;
    private final ArrayList<Sample> samples = new ArrayList<Sample>();
    private long duration;
    private final String handler;
    private AbstractMediaHeaderBox headerBox;
    private SampleDescriptionBox sampleDescriptionBox;
    private LinkedList<Integer> syncSamples;
    private final int timeScale;
    private final Date creationTime = new Date();
    private int height;
    private int width;
    private float volume;
    private final ArrayList<Long> sampleDurations = new ArrayList<Long>();
    private final boolean isAudio = false;
    private static final Map<Integer, Integer> samplingFrequencyIndexMap = new HashMap<Integer, Integer>();
    private long lastPresentationTimeUs;
    private boolean first = true;

    static {
        Track.samplingFrequencyIndexMap.put(96000, 0x0);
        Track.samplingFrequencyIndexMap.put(88200, 0x1);
        Track.samplingFrequencyIndexMap.put(64000, 0x2);
        Track.samplingFrequencyIndexMap.put(48000, 0x3);
        Track.samplingFrequencyIndexMap.put(44100, 0x4);
        Track.samplingFrequencyIndexMap.put(32000, 0x5);
        Track.samplingFrequencyIndexMap.put(24000, 0x6);
        Track.samplingFrequencyIndexMap.put(22050, 0x7);
        Track.samplingFrequencyIndexMap.put(16000, 0x8);
        Track.samplingFrequencyIndexMap.put(12000, 0x9);
        Track.samplingFrequencyIndexMap.put(11025, 0xa);
        Track.samplingFrequencyIndexMap.put(8000, 0xb);
    }

    public Track(final int id, final MediaFormat format, boolean isAudio) throws Exception {
        this.trackId = id;
        if (!isAudio) {
            this.sampleDurations.add((long)3015);
            this.duration = 3015;
            this.width = format.getInteger(MediaFormat.KEY_WIDTH);
            this.height = format.getInteger(MediaFormat.KEY_HEIGHT);
            this.timeScale = 90000;
            this.syncSamples = new LinkedList<Integer>();
            this.handler = "vide";
            this.headerBox = new VideoMediaHeaderBox();
            this.sampleDescriptionBox = new SampleDescriptionBox();
            final String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.equals("video/avc")) {
                final VisualSampleEntry visualSampleEntry = new VisualSampleEntry("avc1");
                visualSampleEntry.setDataReferenceIndex(1);
                visualSampleEntry.setDepth(24);
                visualSampleEntry.setFrameCount(1);
                visualSampleEntry.setHorizresolution(72);
                visualSampleEntry.setVertresolution(72);
                visualSampleEntry.setWidth(this.width);
                visualSampleEntry.setHeight(this.height);

                final AvcConfigurationBox avcConfigurationBox = new AvcConfigurationBox();

                if (format.getByteBuffer("csd-0") != null) {
                    final ArrayList<byte[]> spsArray = new ArrayList<byte[]>();
                    final ByteBuffer spsBuff = format.getByteBuffer("csd-0");
                    spsBuff.position(4);
                    final byte[] spsBytes = new byte[spsBuff.remaining()];
                    spsBuff.get(spsBytes);
                    spsArray.add(spsBytes);

                    final ArrayList<byte[]> ppsArray = new ArrayList<byte[]>();
                    final ByteBuffer ppsBuff = format.getByteBuffer("csd-1");
                    ppsBuff.position(4);
                    final byte[] ppsBytes = new byte[ppsBuff.remaining()];
                    ppsBuff.get(ppsBytes);
                    ppsArray.add(ppsBytes);
                    avcConfigurationBox.setSequenceParameterSets(spsArray);
                    avcConfigurationBox.setPictureParameterSets(ppsArray);
                }
                //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(spsBytes);
                //SeqParameterSet seqParameterSet = SeqParameterSet.read(byteArrayInputStream);

                avcConfigurationBox.setAvcLevelIndication(13);
                avcConfigurationBox.setAvcProfileIndication(100);
                avcConfigurationBox.setBitDepthLumaMinus8(-1);
                avcConfigurationBox.setBitDepthChromaMinus8(-1);
                avcConfigurationBox.setChromaFormat(-1);
                avcConfigurationBox.setConfigurationVersion(1);
                avcConfigurationBox.setLengthSizeMinusOne(3);
                avcConfigurationBox.setProfileCompatibility(0);

                visualSampleEntry.addBox(avcConfigurationBox);
                this.sampleDescriptionBox.addBox(visualSampleEntry);
            } else if (mime.equals("video/mp4v")) {
                final VisualSampleEntry visualSampleEntry = new VisualSampleEntry("mp4v");
                visualSampleEntry.setDataReferenceIndex(1);
                visualSampleEntry.setDepth(24);
                visualSampleEntry.setFrameCount(1);
                visualSampleEntry.setHorizresolution(72);
                visualSampleEntry.setVertresolution(72);
                visualSampleEntry.setWidth(this.width);
                visualSampleEntry.setHeight(this.height);

                this.sampleDescriptionBox.addBox(visualSampleEntry);
            }
        } else {
            this.sampleDurations.add((long)1024);
            this.duration = 1024;
            isAudio = true;
            this.volume = 1;
            this.timeScale = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            this.handler = "soun";
            this.headerBox = new SoundMediaHeaderBox();
            this.sampleDescriptionBox = new SampleDescriptionBox();
            final AudioSampleEntry audioSampleEntry = new AudioSampleEntry("mp4a");
            audioSampleEntry.setChannelCount(format.getInteger(MediaFormat.KEY_CHANNEL_COUNT));
            audioSampleEntry.setSampleRate(format.getInteger(MediaFormat.KEY_SAMPLE_RATE));
            audioSampleEntry.setDataReferenceIndex(1);
            audioSampleEntry.setSampleSize(16);

            final ESDescriptorBox esds = new ESDescriptorBox();
            final ESDescriptor descriptor = new ESDescriptor();
            descriptor.setEsId(0);

            final SLConfigDescriptor slConfigDescriptor = new SLConfigDescriptor();
            slConfigDescriptor.setPredefined(2);
            descriptor.setSlConfigDescriptor(slConfigDescriptor);

            final DecoderConfigDescriptor decoderConfigDescriptor = new DecoderConfigDescriptor();
            decoderConfigDescriptor.setObjectTypeIndication(0x40);
            decoderConfigDescriptor.setStreamType(5);
            decoderConfigDescriptor.setBufferSizeDB(1536);
            decoderConfigDescriptor.setMaxBitRate(96000);
            decoderConfigDescriptor.setAvgBitRate(96000);

            final AudioSpecificConfig audioSpecificConfig = new AudioSpecificConfig();
            audioSpecificConfig.setAudioObjectType(2);
            audioSpecificConfig.setSamplingFrequencyIndex(Track.samplingFrequencyIndexMap.get((int)audioSampleEntry.getSampleRate()));
            audioSpecificConfig.setChannelConfiguration(audioSampleEntry.getChannelCount());
            decoderConfigDescriptor.setAudioSpecificInfo(audioSpecificConfig);

            descriptor.setDecoderConfigDescriptor(decoderConfigDescriptor);

            final ByteBuffer data = descriptor.serialize();
            esds.setEsDescriptor(descriptor);
            esds.setData(data);
            audioSampleEntry.addBox(esds);
            this.sampleDescriptionBox.addBox(audioSampleEntry);
        }
    }

    public long getTrackId() {
        return this.trackId;
    }

    public void addSample(final long offset, final MediaCodec.BufferInfo bufferInfo) {
        final boolean isSyncFrame = !this.isAudio && (bufferInfo.flags & MediaCodec.BUFFER_FLAG_SYNC_FRAME) != 0;
        this.samples.add(new Sample(offset, bufferInfo.size));
        if (this.syncSamples != null && isSyncFrame) {
            this.syncSamples.add(this.samples.size());
        }

        long delta = bufferInfo.presentationTimeUs - this.lastPresentationTimeUs;
        this.lastPresentationTimeUs = bufferInfo.presentationTimeUs;
        delta = (delta * this.timeScale + 500000L) / 1000000L;
        if (!this.first) {
            this.sampleDurations.add(this.sampleDurations.size() - 1, delta);
            this.duration += delta;
        }
        this.first = false;
    }

    public ArrayList<Sample> getSamples() {
        return this.samples;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getHandler() {
        return this.handler;
    }

    public AbstractMediaHeaderBox getMediaHeaderBox() {
        return this.headerBox;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSyncSamples() {
        if (this.syncSamples == null || this.syncSamples.isEmpty()) {
            return null;
        }
        final long[] returns = new long[this.syncSamples.size()];
        for (int i = 0; i < this.syncSamples.size(); i++) {
            returns[i] = this.syncSamples.get(i);
        }
        return returns;
    }

    public int getTimeScale() {
        return this.timeScale;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getVolume() {
        return this.volume;
    }

    public ArrayList<Long> getSampleDurations() {
        return this.sampleDurations;
    }

    public boolean isAudio() {
        return this.isAudio;
    }
}
