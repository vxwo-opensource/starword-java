package org.vxwo.free.starword;

/**
 * Configuration options for the {@link StarJsonEngine}.
 * <p>
 * Controls case sensitivity and the default {@link StarMethod} used when masking
 * JSON field values.
 * </p>
 */
public class StarJsonOptions {
    /**
     * Whether matching should ignore case.
     */
    private boolean ignoreCase;

    /**
     * The default star masking method applied to matched values.
     */
    private StarMethod method;

    /**
     * Constructs a {@code StarJsonOptions} instance.
     *
     * @param ignoreCase whether matching should ignore case
     * @param method the default star masking method applied to matched values
     */
    public StarJsonOptions(boolean ignoreCase, StarMethod method) {
        this.ignoreCase = ignoreCase;
        this.method = method;
    }

    /**
     * Returns whether matching ignores case.
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Returns the default star masking method.
     */
    public StarMethod getMethod() {
        return method;
    }
}
