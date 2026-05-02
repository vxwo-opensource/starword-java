package org.vxwo.free.starword;

/**
 * Defines a per-key masking rule for JSON processing in {@link StarJsonEngine}.
 * <p>
 * Each strategy associates a {@link StarMethod} (controlling how many characters
 * are masked) with a set of keywords, allowing fine-grained control over which
 * JSON fields are masked and how their values are obscured.
 * </p>
 */
public class StarStrategy {

    /**
     * The masking method defining visible character borders.
     */
    private StarMethod method;

    /**
     * The keywords to which this strategy applies.
     */
    private String[] keywords;

    /**
     * Constructs a {@code StarStrategy}.
     *
     * @param method the masking method defining how values are obscured
     * @param keywords the keywords this strategy applies to
     */
    public StarStrategy(StarMethod method, String[] keywords) {
        this.method = method;
        this.keywords = keywords;
    }

    /**
     * Returns the masking method.
     *
     * @return the masking method
     */
    public StarMethod getMethod() {
        return method;
    }

    /**
     * Returns the keywords this strategy applies to.
     *
     * @return the keyword array
     */
    public String[] getKeywords() {
        return keywords;
    }
}
