package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.BaseNativeObject;
import org.vxwo.free.starword.internal.NativeCleanuper;
import org.vxwo.free.starword.internal.NativeEngine;

public class StarJsonEngine extends BaseNativeObject {
    private StarJsonEngine(long nativePtr) {
        super(NativeCleanuper.TYPE_JSON, nativePtr);
    }

    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starJsonProcess(getNativePtr(), content);
    }

    public static StarJsonEngine create(String[] keywords, boolean skipNumber,
            StarOptions options) {
        long nativePtr = NativeEngine.starJsonCreate(keywords, skipNumber, options);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
