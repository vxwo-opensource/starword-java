package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.BaseNativeObject;
import org.vxwo.free.starword.internal.NativeCleanuper;
import org.vxwo.free.starword.internal.NativeEngine;

/**
 * Use "star" to mask words in a "simple text".
 */

public class StarTextEngine extends BaseNativeObject {
    private StarTextEngine(long nativePtr) {
        super(NativeCleanuper.TYPE_TEXT, nativePtr);
    }

    /**
     * Processes the given text content by replacing specified keywords with "stars".
     * <p>
     * This method interacts with the native engine to perform the masking.
     * </p>
     *
     * @param content the text content to be processed.
     * @return the processed text with specified keywords masked. If the input is {@code null}
     *         or empty, the original content is returned.
     */
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starTextProcess(getNativePtr(), content);
    }

    /**
     * Creates a new {@code StarTextEngine} instance with the specified keywords and options.
     * <p>
     * This method initializes the native resources required for text processing.
     * </p>
     *
     * @param keywords an array of keywords to be masked in the text content.
     * @param options  the {@link StarOptions} object containing configuration settings for the engine.
     * @return a new instance of {@code StarTextEngine}.
     */
    public static StarTextEngine create(String[] keywords, StarOptions options) {
        long nativePtr = NativeEngine.starTextCreate(keywords, options);
        StarTextEngine obj = new StarTextEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
