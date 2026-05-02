package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.BaseNativeObject;
import org.vxwo.free.starword.internal.NativeCleanuper;
import org.vxwo.free.starword.internal.NativeEngine;

/**
 * Masks specified keywords in plain text by replacing their inner characters with asterisks.
 * <p>
 * This engine operates on plain text content. Keywords are matched based on the
 * configured {@link StarTextOptions}, and matched portions are partially masked
 * while preserving the configured left and right border characters.
 * </p>
 * <p>
 * Instances are backed by native resources and should not be used after their
 * native handle is released.
 * </p>
 */
public class StarTextEngine extends BaseNativeObject {
    private StarTextEngine(long nativePtr) {
        super(TYPE_TEXT, nativePtr);
    }

    /**
     * Masks configured keywords in the given text.
     *
     * @param content the text to process
     * @return the text with matched keywords partially masked; returns the original
     *         content if it is {@code null} or empty
     */
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starTextProcess(getNativePtr(), content);
    }

    /**
     * Creates a new {@code StarTextEngine} instance.
     *
     * @param options configuration settings controlling matching and masking behavior
     * @param keywords keywords to be masked in processed text
     * @return a new {@code StarTextEngine} instance
     */
    public static StarTextEngine create(StarTextOptions options, String[] keywords) {
        long nativePtr = NativeEngine.starTextCreate(options, keywords);
        StarTextEngine obj = new StarTextEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
