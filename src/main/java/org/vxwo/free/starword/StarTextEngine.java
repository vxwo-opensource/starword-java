package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.BaseNativeObject;
import org.vxwo.free.starword.internal.NativeCleanuper;
import org.vxwo.free.starword.internal.NativeEngine;

public class StarTextEngine extends BaseNativeObject {
    private StarTextEngine(long nativePtr) {
        super(NativeCleanuper.TYPE_TEXT, nativePtr);
    }

    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starTextProcess(getNativePtr(), content);
    }

    public static StarTextEngine create(String[] keywords, StarOptions options) {
        long nativePtr = NativeEngine.starTextCreate(keywords, options);
        StarTextEngine obj = new StarTextEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
