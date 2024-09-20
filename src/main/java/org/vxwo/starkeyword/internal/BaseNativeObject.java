package org.vxwo.starkeyword.internal;

public abstract class BaseNativeObject {
    private int nativeType;
    private long nativePtr;

    protected BaseNativeObject(int nativeType, long nativePtr) {
        this.nativeType = nativeType;
        this.nativePtr = nativePtr;
    }

    public int getNativeType() {
        return nativeType;
    }

    public long getNativePtr() {
        return nativePtr;
    }
}
