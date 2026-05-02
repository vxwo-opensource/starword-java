package org.vxwo.free.starword.internal;

import org.vxwo.free.starword.StarTextOptions;
import org.vxwo.free.starword.StarJsonOptions;
import org.vxwo.free.starword.StarMethod;
import org.vxwo.free.starword.StarStrategy;

public class NativeEngine {
    public static native String starProcess(StarMethod method, String content);

    public static native long starJsonCreate(StarJsonOptions options, String[] keywords,
            StarStrategy[] strategies);

    public static native void starJsonCleanup(long ptr);

    public static native String starJsonProcess(long ptr, String content);

    public static native long starTextCreate(StarTextOptions options, String[] keywords);

    public static native void starTextCleanup(long ptr);

    public static native String starTextProcess(long ptr, String content);

    static {
        NativeLoader.loadLibrary("starword-java");
    }
}
