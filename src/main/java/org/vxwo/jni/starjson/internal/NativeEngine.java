package org.vxwo.jni.starjson.internal;

public class NativeEngine {
    public static native long create(String[] keywords, boolean skipNumber, int border);

    public static native void cleanup(long ptr);

    public static native String process(long ptr, String content);

    static {
        NativeLoader.loadLibrary("starjson");
    }
}
