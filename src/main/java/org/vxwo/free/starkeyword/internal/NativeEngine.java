package org.vxwo.free.starkeyword.internal;

import org.vxwo.free.starkeyword.StarOptions;

public class NativeEngine {
    public static native long starJsonCreate(String[] keywords, boolean skipNumber,
            StarOptions options);

    public static native void starJsonCleanup(long ptr);

    public static native String starJsonProcess(long ptr, String content);

    public static native long starTextCreate(String[] keywords, StarOptions options);

    public static native void starTextCleanup(long ptr);

    public static native String starTextProcess(long ptr, String content);

    static {
        NativeLoader.loadLibrary("starkeyword-jni");
    }
}
