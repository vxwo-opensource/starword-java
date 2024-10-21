package org.vxwo.free.starword;

public class StarOptions {
    private boolean ignoreCase;
    private int leftBorder;
    private int rightBorder;

    public StarOptions(boolean ignoreCase, int leftBorder, int rightBorder) {
        this.ignoreCase = ignoreCase;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }
}
