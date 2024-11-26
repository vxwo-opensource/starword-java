package org.vxwo.free.starword.internal;

public abstract class BaseNativeObject {
    public static final int TYPE_JSON = 1;
    public static final int TYPE_TEXT = 2;

    private final int nativeType;
    private final long nativePtr;

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

    public static void nativeCleanup(int nativeType, long nativePtr) {
        if (nativeType == TYPE_JSON) {
            NativeEngine.starJsonCleanup(nativePtr);
        } else if (nativeType == TYPE_TEXT) {
            NativeEngine.starTextCleanup(nativePtr);
        }
    }
}
