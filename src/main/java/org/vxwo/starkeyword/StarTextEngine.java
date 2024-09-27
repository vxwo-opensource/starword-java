package org.vxwo.starkeyword;

import org.vxwo.starkeyword.internal.BaseNativeObject;
import org.vxwo.starkeyword.internal.NativeCleanuper;
import org.vxwo.starkeyword.internal.NativeEngine;

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

    public static StarTextEngine create(String[] keywords, boolean ignoreCawe, int leftBorder,
            int rightBorder) {
        long nativePtr = NativeEngine.starTextCreate(keywords, ignoreCawe, leftBorder, rightBorder);
        StarTextEngine obj = new StarTextEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
