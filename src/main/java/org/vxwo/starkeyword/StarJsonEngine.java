package org.vxwo.starkeyword;

import org.vxwo.starkeyword.internal.BaseNativeObject;
import org.vxwo.starkeyword.internal.NativeCleanuper;
import org.vxwo.starkeyword.internal.NativeEngine;

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

    public static StarJsonEngine create(String[] keywords, boolean skipNumber, int border) {
        long nativePtr = NativeEngine.starJsonCreate(keywords, skipNumber, border);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
