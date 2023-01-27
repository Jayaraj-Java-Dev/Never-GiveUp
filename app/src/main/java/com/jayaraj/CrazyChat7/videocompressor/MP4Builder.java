package com.jayaraj.CrazyChat7.videocompressor;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.SyncSampleBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.Matrix;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@TargetApi(16)
public class MP4Builder {
    private InterleaveChunkMdat mdat;
    private Mp4Movie currentMp4Movie;
    private FileOutputStream fos;
    private FileChannel fc;
    private long dataOffset;
    private long writedSinceLastMdat;
    private boolean writeNewMdat = true;
    private final HashMap<Track, long[]> track2SampleSizes = new HashMap<>();
    private ByteBuffer sizeBuffer;

    public MP4Builder createMovie(final Mp4Movie mp4Movie) throws Exception {
        this.currentMp4Movie = mp4Movie;

        this.fos = new FileOutputStream(mp4Movie.getCacheFile());
        this.fc = this.fos.getChannel();

        final FileTypeBox fileTypeBox = this.createFileTypeBox();
        fileTypeBox.getBox(this.fc);
        this.dataOffset += fileTypeBox.getSize();
        this.writedSinceLastMdat += this.dataOffset;

        this.mdat = new InterleaveChunkMdat();

        this.sizeBuffer = ByteBuffer.allocateDirect(4);

        return this;
    }

    private void flushCurrentMdat() throws Exception {
        final long oldPosition = this.fc.position();
        this.fc.position(this.mdat.getOffset());
        this.mdat.getBox(this.fc);
        this.fc.position(oldPosition);
        this.mdat.setDataOffset(0);
        this.mdat.setContentSize(0);
        this.fos.flush();
    }

    public boolean writeSampleData(final int trackIndex, final ByteBuffer byteBuf, final MediaCodec.BufferInfo bufferInfo, final boolean isAudio) throws Exception {
        if (this.writeNewMdat) {
            this.mdat.setContentSize(0);
            this.mdat.getBox(this.fc);
            this.mdat.setDataOffset(this.dataOffset);
            this.dataOffset += 16;
            this.writedSinceLastMdat += 16;
            this.writeNewMdat = false;
        }

        this.mdat.setContentSize(this.mdat.getContentSize() + bufferInfo.size);
        this.writedSinceLastMdat += bufferInfo.size;

        boolean flush = false;
        if (this.writedSinceLastMdat >= 32 * 1024) {
            this.flushCurrentMdat();
            this.writeNewMdat = true;
            flush = true;
            this.writedSinceLastMdat -= 32 * 1024;
        }

        this.currentMp4Movie.addSample(trackIndex, this.dataOffset, bufferInfo);
        byteBuf.position(bufferInfo.offset + (isAudio ? 0 : 4));
        byteBuf.limit(bufferInfo.offset + bufferInfo.size);

        if (!isAudio) {
            this.sizeBuffer.position(0);
            this.sizeBuffer.putInt(bufferInfo.size - 4);
            this.sizeBuffer.position(0);
            this.fc.write(this.sizeBuffer);
        }

        this.fc.write(byteBuf);
        this.dataOffset += bufferInfo.size;

        if (flush) {
            this.fos.flush();
        }
        return flush;
    }

    public int addTrack(final MediaFormat mediaFormat, final boolean isAudio) throws Exception {
        return this.currentMp4Movie.addTrack(mediaFormat, isAudio);
    }

    public void finishMovie(final boolean error) throws Exception {
        if (this.mdat.getContentSize() != 0) {
            this.flushCurrentMdat();
        }

        for (final Track track : this.currentMp4Movie.getTracks()) {
            final List<Sample> samples = track.getSamples();
            final long[] sizes = new long[samples.size()];
            for (int i = 0; i < sizes.length; i++) {
                sizes[i] = samples.get(i).getSize();
            }
            this.track2SampleSizes.put(track, sizes);
        }

        final Box moov = this.createMovieBox(this.currentMp4Movie);
        moov.getBox(this.fc);
        this.fos.flush();

        this.fc.close();
        this.fos.close();
    }

    protected FileTypeBox createFileTypeBox() {
        final LinkedList<String> minorBrands = new LinkedList<>();
        minorBrands.add("isom");
        minorBrands.add("3gp4");
        return new FileTypeBox("isom", 0, minorBrands);
    }

    private class InterleaveChunkMdat implements Box {
        private Container parent;
        private long contentSize = 1024 * 1024 * 1024;
        private long dataOffset;

        public Container getParent() {
            return this.parent;
        }

        public long getOffset() {
            return this.dataOffset;
        }

        public void setDataOffset(final long offset) {
            this.dataOffset = offset;
        }

        public void setParent(final Container parent) {
            this.parent = parent;
        }

        public void setContentSize(final long contentSize) {
            this.contentSize = contentSize;
        }

        public long getContentSize() {
            return this.contentSize;
        }

        public String getType() {
            return "mdat";
        }

        public long getSize() {
            return 16 + this.contentSize;
        }

        private boolean isSmallBox(final long contentSize) {
            return (contentSize + 8) < 4294967296L;
        }

        @Override
        public void parse(final DataSource dataSource, final ByteBuffer header, final long contentSize, final BoxParser boxParser) throws IOException {

        }

        public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
            final ByteBuffer bb = ByteBuffer.allocate(16);
            final long size = this.getSize();
            if (this.isSmallBox(size)) {
                IsoTypeWriter.writeUInt32(bb, size);
            } else {
                IsoTypeWriter.writeUInt32(bb, 1);
            }
            bb.put(IsoFile.fourCCtoBytes("mdat"));
            if (this.isSmallBox(size)) {
                bb.put(new byte[8]);
            } else {
                IsoTypeWriter.writeUInt64(bb, size);
            }
            bb.rewind();
            writableByteChannel.write(bb);
        }
    }

    public static long gcd(final long a, final long b) {
        if (b == 0) {
            return a;
        }
        return MP4Builder.gcd(b, a % b);
    }

    public long getTimescale(final Mp4Movie mp4Movie) {
        long timescale = 0;
        if (!mp4Movie.getTracks().isEmpty()) {
            timescale = mp4Movie.getTracks().iterator().next().getTimeScale();
        }
        for (final Track track : mp4Movie.getTracks()) {
            timescale = MP4Builder.gcd(track.getTimeScale(), timescale);
        }
        return timescale;
    }

    protected MovieBox createMovieBox(final Mp4Movie movie) {
        final MovieBox movieBox = new MovieBox();
        final MovieHeaderBox mvhd = new MovieHeaderBox();

        mvhd.setCreationTime(new Date());
        mvhd.setModificationTime(new Date());
        mvhd.setMatrix(Matrix.ROTATE_0);
        final long movieTimeScale = this.getTimescale(movie);
        long duration = 0;

        for (final Track track : movie.getTracks()) {
            final long tracksDuration = track.getDuration() * movieTimeScale / track.getTimeScale();
            if (tracksDuration > duration) {
                duration = tracksDuration;
            }
        }

        mvhd.setDuration(duration);
        mvhd.setTimescale(movieTimeScale);
        mvhd.setNextTrackId(movie.getTracks().size() + 1);

        movieBox.addBox(mvhd);
        for (final Track track : movie.getTracks()) {
            movieBox.addBox(this.createTrackBox(track, movie));
        }
        return movieBox;
    }

    protected TrackBox createTrackBox(final Track track, final Mp4Movie movie) {
        final TrackBox trackBox = new TrackBox();
        final TrackHeaderBox tkhd = new TrackHeaderBox();

        tkhd.setEnabled(true);
        tkhd.setInMovie(true);
        tkhd.setInPreview(true);
        if (track.isAudio()) {
            tkhd.setMatrix(Matrix.ROTATE_0);
        } else {
            tkhd.setMatrix(movie.getMatrix());
        }
        tkhd.setAlternateGroup(0);
        tkhd.setCreationTime(track.getCreationTime());
        tkhd.setDuration(track.getDuration() * this.getTimescale(movie) / track.getTimeScale());
        tkhd.setHeight(track.getHeight());
        tkhd.setWidth(track.getWidth());
        tkhd.setLayer(0);
        tkhd.setModificationTime(new Date());
        tkhd.setTrackId(track.getTrackId() + 1);
        tkhd.setVolume(track.getVolume());

        trackBox.addBox(tkhd);

        final MediaBox mdia = new MediaBox();
        trackBox.addBox(mdia);
        final MediaHeaderBox mdhd = new MediaHeaderBox();
        mdhd.setCreationTime(track.getCreationTime());
        mdhd.setDuration(track.getDuration());
        mdhd.setTimescale(track.getTimeScale());
        mdhd.setLanguage("eng");
        mdia.addBox(mdhd);
        final HandlerBox hdlr = new HandlerBox();
        hdlr.setName(track.isAudio() ? "SoundHandle" : "VideoHandle");
        hdlr.setHandlerType(track.getHandler());

        mdia.addBox(hdlr);

        final MediaInformationBox minf = new MediaInformationBox();
        minf.addBox(track.getMediaHeaderBox());

        final DataInformationBox dinf = new DataInformationBox();
        final DataReferenceBox dref = new DataReferenceBox();
        dinf.addBox(dref);
        final DataEntryUrlBox url = new DataEntryUrlBox();
        url.setFlags(1);
        dref.addBox(url);
        minf.addBox(dinf);

        final Box stbl = this.createStbl(track);
        minf.addBox(stbl);
        mdia.addBox(minf);

        return trackBox;
    }

    protected Box createStbl(final Track track) {
        final SampleTableBox stbl = new SampleTableBox();

        this.createStsd(track, stbl);
        this.createStts(track, stbl);
        this.createStss(track, stbl);
        this.createStsc(track, stbl);
        this.createStsz(track, stbl);
        this.createStco(track, stbl);

        return stbl;
    }

    protected void createStsd(final Track track, final SampleTableBox stbl) {
        stbl.addBox(track.getSampleDescriptionBox());
    }

    protected void createStts(final Track track, final SampleTableBox stbl) {
        TimeToSampleBox.Entry lastEntry = null;
        final List<TimeToSampleBox.Entry> entries = new ArrayList<>();

        for (final long delta : track.getSampleDurations()) {
            if (lastEntry != null && lastEntry.getDelta() == delta) {
                lastEntry.setCount(lastEntry.getCount() + 1);
            } else {
                lastEntry = new TimeToSampleBox.Entry(1, delta);
                entries.add(lastEntry);
            }
        }
        final TimeToSampleBox stts = new TimeToSampleBox();
        stts.setEntries(entries);
        stbl.addBox(stts);
    }

    protected void createStss(final Track track, final SampleTableBox stbl) {
        final long[] syncSamples = track.getSyncSamples();
        if (syncSamples != null && syncSamples.length > 0) {
            final SyncSampleBox stss = new SyncSampleBox();
            stss.setSampleNumber(syncSamples);
            stbl.addBox(stss);
        }
    }

    protected void createStsc(final Track track, final SampleTableBox stbl) {
        final SampleToChunkBox stsc = new SampleToChunkBox();
        stsc.setEntries(new LinkedList<SampleToChunkBox.Entry>());

        long lastOffset = -1;
        int lastChunkNumber = 1;
        int lastSampleCount = 0;

        int previousWritedChunkCount = -1;

        final int samplesCount = track.getSamples().size();
        for (int a = 0; a < samplesCount; a++) {
            final Sample sample = track.getSamples().get(a);
            final long offset = sample.getOffset();
            final long size = sample.getSize();

            lastOffset = offset + size;
            lastSampleCount++;

            boolean write = false;
            if (a != samplesCount - 1) {
                final Sample nextSample = track.getSamples().get(a + 1);
                if (lastOffset != nextSample.getOffset()) {
                    write = true;
                }
            } else {
                write = true;
            }
            if (write) {
                if (previousWritedChunkCount != lastSampleCount) {
                    stsc.getEntries().add(new SampleToChunkBox.Entry(lastChunkNumber, lastSampleCount, 1));
                    previousWritedChunkCount = lastSampleCount;
                }
                lastSampleCount = 0;
                lastChunkNumber++;
            }
        }
        stbl.addBox(stsc);
    }

    protected void createStsz(final Track track, final SampleTableBox stbl) {
        final SampleSizeBox stsz = new SampleSizeBox();
        stsz.setSampleSizes(this.track2SampleSizes.get(track));
        stbl.addBox(stsz);
    }

    protected void createStco(final Track track, final SampleTableBox stbl) {
        final ArrayList<Long> chunksOffsets = new ArrayList<>();
        long lastOffset = -1;
        for (final Sample sample : track.getSamples()) {
            final long offset = sample.getOffset();
            if (lastOffset != -1 && lastOffset != offset) {
                lastOffset = -1;
            }
            if (lastOffset == -1) {
                chunksOffsets.add(offset);
            }
            lastOffset = offset + sample.getSize();
        }
        final long[] chunkOffsetsLong = new long[chunksOffsets.size()];
        for (int a = 0; a < chunksOffsets.size(); a++) {
            chunkOffsetsLong[a] = chunksOffsets.get(a);
        }

        final StaticChunkOffsetBox stco = new StaticChunkOffsetBox();
        stco.setChunkOffsets(chunkOffsetsLong);
        stbl.addBox(stco);
    }
}
