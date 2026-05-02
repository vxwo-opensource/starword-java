package org.vxwo.free.starword;

/**
 * Configuration options for the {@link StarTextEngine}.
 * <p>
 * Controls case sensitivity and the star masking behavior, specifying how many characters
 * at the left and right borders of a matched keyword remain visible.
 * </p>
 */
public class StarTextOptions {
    /**
     * Whether matching should ignore case.
     */
    private boolean ignoreCase;

    /**
     * Number of visible characters to retain at the beginning of a matched keyword.
     */
    private int leftBorder;

    /**
     * Number of visible characters to retain at the end of a matched keyword.
     */
    private int rightBorder;

    /**
     * Constructs a {@code StarTextOptions} instance.
     *
     * @param ignoreCase whether matching should ignore case
     * @param leftBorder number of visible characters at the beginning of a matched keyword
     * @param rightBorder number of visible characters at the end of a matched keyword
     */
    public StarTextOptions(boolean ignoreCase, int leftBorder, int rightBorder) {
        this.ignoreCase = ignoreCase;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    /**
     * Returns whether matching ignores case.
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Returns the number of visible characters at the beginning of a matched keyword.
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * Returns the number of visible characters at the end of a matched keyword.
     */
    public int getRightBorder() {
        return rightBorder;
    }
}
