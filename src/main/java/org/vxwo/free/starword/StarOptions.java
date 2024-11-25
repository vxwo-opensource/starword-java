package org.vxwo.free.starword;

/**
 * Options related to star settings.
 */

public class StarOptions {
    /**
     * Whether to ignore case sensitivity.
     */
    private boolean ignoreCase;

    /**
     * The value of the left border.
     */
    private int leftBorder;

    /**
     * The value of the right border.
     */
    private int rightBorder;

    /**
     * Constructor to initialize all configuration options.
     *
     * @param ignoreCase whether to ignore case sensitivity.
     * @param leftBorder the value of the left border.
     * @param rightBorder the value of the right border.
     */
    public StarOptions(boolean ignoreCase, int leftBorder, int rightBorder) {
        this.ignoreCase = ignoreCase;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    /**
     * Retrieves the configuration for case sensitivity.
     *
     * @return {@code true} if case sensitivity is ignored; otherwise, {@code false}.
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Retrieves the value of the left border.
     *
     * @return the value of the left border.
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * Retrieves the value of the right border.
     *
     * @return the value of the right border.
     */
    public int getRightBorder() {
        return rightBorder;
    }
}
