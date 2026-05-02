package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.BaseNativeObject;
import org.vxwo.free.starword.internal.NativeCleanuper;
import org.vxwo.free.starword.internal.NativeEngine;

/**
 * Masks values in JSON content by replacing inner characters with asterisks.
 * <p>
 * This engine parses JSON and selectively masks field values based on configured
 * keywords and per-key {@link StarStrategy} rules. It preserves JSON structure
 * while partially obscuring matched values.
 * </p>
 * <p>
 * Instances are backed by native resources and should not be used after their
 * native handle is released.
 * </p>
 */
public class StarJsonEngine extends BaseNativeObject {
    private StarJsonEngine(long nativePtr) {
        super(TYPE_JSON, nativePtr);
    }

    /**
     * Masks configured values in the given JSON content.
     *
     * @param content the JSON text to process
     * @return the JSON with matched values partially masked; returns the original
     *         content if it is {@code null} or empty
     */
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starJsonProcess(getNativePtr(), content);
    }

    /**
     * Creates a new {@code StarJsonEngine} instance with base keywords.
     *
     * @param options configuration settings controlling matching and masking behavior
     * @param keywords base keywords whose corresponding JSON values will be masked
     * @return a new {@code StarJsonEngine} instance
     */
    public static StarJsonEngine create(StarJsonOptions options, String[] keywords) {
        long nativePtr = NativeEngine.starJsonCreate(options, keywords, null);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }

    /**
     * Creates a new {@code StarJsonEngine} instance with base keywords and custom strategies.
     *
     * @param options configuration settings controlling matching and masking behavior
     * @param keywords base keywords whose corresponding JSON values will be masked
     * @param strategies additional per-key masking rules that override defaults
     * @return a new {@code StarJsonEngine} instance
     */
    public static StarJsonEngine create(StarJsonOptions options, String[] keywords,
            StarStrategy[] strategies) {
        long nativePtr = NativeEngine.starJsonCreate(options, keywords, strategies);
        StarJsonEngine obj = new StarJsonEngine(nativePtr);
        NativeCleanuper.register(obj);
        return obj;
    }
}
