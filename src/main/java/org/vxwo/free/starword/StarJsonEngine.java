package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.BaseNativeObject;
import org.vxwo.free.starword.internal.NativeCleanuper;
import org.vxwo.free.starword.internal.NativeEngine;

/**
 * Use "star" to mask value of words in a "JSON text".
 */

public class StarJsonEngine extends BaseNativeObject {
    private StarJsonEngine(long nativePtr) {
        super(TYPE_JSON, nativePtr);
    }

    /**
     * Processes the given JSON content to mask specific values.
     *
     * @param content the JSON text to be processed
     * @return the processed JSON text with masked values, 
     *         or the original content if it is null or empty
     */
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starJsonProcess(getNativePtr(), content);
    }

    /**
     * Creates a new instance of StarJsonEngine with specified keywords and options.
     *
     * @param keywords an array of keywords that should be masked in the JSON text
     * @param options  additional options for configuring the masking behavior
     * @return a new instance of {@link StarJsonEngine}
     */
    public static StarJsonEngine create(String[] keywords, StarOptions options) {
        long nativePtr = NativeEngine.starJsonCreate(keywords, options);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
