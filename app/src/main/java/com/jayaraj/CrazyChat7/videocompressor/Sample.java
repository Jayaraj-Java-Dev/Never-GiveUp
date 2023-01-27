package com.jayaraj.CrazyChat7.videocompressor;

public class Sample {
    private long offset;
    private long size;

    public Sample(final long offset, final long size) {
        this.offset = offset;
        this.size = size;
    }

    public long getOffset() {
        return this.offset;
    }

    public long getSize() {
        return this.size;
    }
}
