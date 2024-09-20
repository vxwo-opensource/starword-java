package org.vxwo.jni.starjson;

import org.vxwo.jni.starjson.internal.NativeCleanuper;
import org.vxwo.jni.starjson.internal.NativeEngine;

public class StarJsonEngine {
    private long nativePtr;

    private StarJsonEngine(long nativePtr) {
        this.nativePtr = nativePtr;
    }

    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.process(nativePtr, content);
    }

    public static StarJsonEngine create(String[] keywords, boolean skipNumber, int border) {
        long nativePtr = NativeEngine.create(keywords, skipNumber, border);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj, nativePtr);
        return obj;
    }
}
