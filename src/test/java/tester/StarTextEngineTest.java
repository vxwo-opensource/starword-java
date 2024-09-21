package tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.vxwo.starkeyword.StarTextEngine;

public class StarTextEngineTest {
    private static final String[] keywords = new String[] {"phone", "mobile"};

    private StarTextEngine getDefaultEngine() {
        return StarTextEngine.create(keywords, false, 1);
    }

    @Test
    @Order(1000)
    void testStarTextIgnore() {
        StarTextEngine starEngine = getDefaultEngine();

        String source = "i have a number";
        String target = "i have a number";
        Assertions.assertEquals(target, starEngine.process(source));

        String source1 = "i have a PhonE";
        String target1 = "i have a PhonE";
        Assertions.assertEquals(target1, starEngine.process(source1));
    }

    @Test
    @Order(1001)
    void testStarTextSimple() {
        StarTextEngine starEngine = getDefaultEngine();

        String source = "i have a phone";
        String target = "i have a p***e";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1002)
    void testStarTextMuiltple() {
        StarTextEngine starEngine = getDefaultEngine();

        String source = "i have a phone, mobile, phone2, mobile2";
        String target = "i have a p***e, m****e, p***e2, m****e2";
        Assertions.assertEquals(target, starEngine.process(source));
    }


    @Test
    @Order(2000)
    void testStarTextNoBorder() {
        StarTextEngine starEngine = StarTextEngine.create(keywords, false, 0);

        String source = "i have a phone";
        String target = "i have a *****";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(2001)
    void testStarTextIgnoreCase() {
        StarTextEngine starEngine = StarTextEngine.create(keywords, true, 0);

        String source = "i have a PhoNe";
        String target = "i have a *****";
        Assertions.assertEquals(target, starEngine.process(source));
    }

}
