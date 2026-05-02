package org.vxwo.free.starword;

/**
 * Defines the star masking pattern applied to a matched keyword or value.
 * <p>
 * Specifies how many characters remain visible at the left and right borders,
 * with the middle portion replaced by asterisks. An optional {@code rightBorderChar}
 * can be used to mark the position where the right border begins.
 * </p>
 */
public class StarMethod {
    /**
     * Number of visible characters at the beginning of a matched keyword.
     */
    private int leftBorder;

    /**
     * Number of visible characters at the end of a matched keyword.
     */
    private int rightBorder;

    /**
     * Optional character marking where the right border begins; {@code '\0'} if unset.
     */
    private char rightBorderChar;

    /**
     * Constructs a {@code StarMethod} without a right border marker character.
     *
     * @param leftBorder number of visible characters at the beginning
     * @param rightBorder number of visible characters at the end
     */
    public StarMethod(int leftBorder, int rightBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.rightBorderChar = '\0';
    }

    /**
     * Constructs a {@code StarMethod} with a right border marker character.
     *
     * @param leftBorder number of visible characters at the beginning
     * @param rightBorder number of visible characters at the end
     * @param rightBorderChar character marking where the right border begins
     */
    public StarMethod(int leftBorder, int rightBorder, char rightBorderChar) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.rightBorderChar = rightBorderChar;
    }

    /**
     * Returns the number of visible characters at the beginning.
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * Returns the number of visible characters at the end.
     */
    public int getRightBorder() {
        return rightBorder;
    }

    /**
     * Returns the right border marker character, or {@code '\0'} if none was set.
     */
    public char getRightBorderChar() {
        return rightBorderChar;
    }
}
