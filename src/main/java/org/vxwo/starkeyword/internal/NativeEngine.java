package org.vxwo.starkeyword.internal;

public class NativeEngine {
    public static native long starJsonCreate(String[] keywords, boolean skipNumber, int border);

    public static native void starJsonCleanup(long ptr);

    public static native String starJsonProcess(long ptr, String content);

    static {
        NativeLoader.loadLibrary("starkeyword");
    }
}
