package org.vxwo.free.starkeyword;

import org.vxwo.free.starkeyword.internal.BaseNativeObject;
import org.vxwo.free.starkeyword.internal.NativeCleanuper;
import org.vxwo.free.starkeyword.internal.NativeEngine;

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

    public static StarJsonEngine create(String[] keywords, boolean ignoreCawe, boolean skipNumber,
            int leftBorder, int rightBorder) {
        long nativePtr = NativeEngine.starJsonCreate(keywords, ignoreCawe, skipNumber, leftBorder,
                rightBorder);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
