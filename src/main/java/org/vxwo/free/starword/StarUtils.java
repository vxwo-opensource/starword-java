package org.vxwo.free.starword;

import org.vxwo.free.starword.internal.NativeEngine;

/**
 * Utility class providing star-masking operations for sensitive text.
 * <p>
 * Applies a masking pattern that preserves a configurable number of characters
 * at the beginning and end of matched content, replacing the middle portion
 * with asterisks (e.g., {@code "phone12345678" → "phone***78"}).
 * </p>
 */
public final class StarUtils {
    private StarUtils() {}

    /**
     * Applies star masking to the given content using the specified method.
     *
     * @param method   the masking pattern defining visible character counts
     * @param content  the text to process; may be {@code null} or empty
     * @return the masked content, or the original input if {@code null} or empty
     */
    public static String process(StarMethod method, String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return NativeEngine.starProcess(method, content);
    }
}
