package tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.vxwo.free.starkeyword.StarJsonEngine;

public class StarJsonEngineTest {
    private static final String[] keywords = new String[] {"phone", "mobile"};

    private StarJsonEngine getDefaultEngine() {
        return StarJsonEngine.create(keywords, false, false, 2, 2);
    }

    @Test
    @Order(1000)
    void testStarJsonIgnore() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"number\":\"12345678\"}";
        String target = "{\"number\":\"12345678\"}";
        Assertions.assertEquals(target, starEngine.process(source));

        String source1 = "{\"PhonE\":\"12345678\"}";
        String target1 = "{\"PhonE\":\"12345678\"}";
        Assertions.assertEquals(target1, starEngine.process(source1));
    }

    @Test
    @Order(1001)
    void testStarJsonSimple() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"12345678\"}";
        String target = "{\"phone\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1002)
    void testStarJsonEmpty() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"\"}";
        String target = "{\"phone\":\"\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1003)
    void testStarJsonSymbol() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\": null, \"num\": 1}";
        String target = "{\"phone\": null, \"num\": 1}";
        Assertions.assertEquals(target, starEngine.process(source));

        String source1 = "{\"phone\": true, \"mobile\": false}";
        String target1 = "{\"phone\": true, \"mobile\": false}";
        Assertions.assertEquals(target1, starEngine.process(source1));

        String source2 = "{\"phone\": \"null\"}";
        String target2 = "{\"phone\": \"nu**\"}";
        Assertions.assertEquals(target2, starEngine.process(source2));
    }

    @Test
    @Order(1004)
    void testStarJsonNUmber() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source1 = "{\"phone\": 12345678}";
        String target1 = "{\"phone\": 12****78}";
        Assertions.assertEquals(target1, starEngine.process(source1));

        String source2 = "{\"phone\": -}";
        String target2 = "{\"phone\": *}";
        Assertions.assertEquals(target2, starEngine.process(source2));

        String source3 = "{\"phone\": -....}";
        String target3 = "{\"phone\": -.*..}";
        Assertions.assertEquals(target3, starEngine.process(source3));

        String source4 = "{\"phone\": -0.12345678.}";
        String target4 = "{\"phone\": -0********8.}";
        Assertions.assertEquals(target4, starEngine.process(source4));

        String source5 = "{\"phone\": -0.123}";
        String target5 = "{\"phone\": -0**23}";
        Assertions.assertEquals(target5, starEngine.process(source5));

        String source6 = "{\"phone\": 1234e5678}";
        String target6 = "{\"phone\": 12*****78}";
        Assertions.assertEquals(target6, starEngine.process(source6));

        String source7 = "{\"phone\": 1234E5678}";
        String target7 = "{\"phone\": 12*****78}";
        Assertions.assertEquals(target7, starEngine.process(source7));

        String source8 = "{\"phone\": 1234.e5678}";
        String target8 = "{\"phone\": 12******78}";
        Assertions.assertEquals(target8, starEngine.process(source8));

        String source9 = "{\"phone\": 1234e.5678}";
        String target9 = "{\"phone\": 12******78}";
        Assertions.assertEquals(target9, starEngine.process(source9));
    }

    @Test
    @Order(1011)
    void testStarJsonNoKey() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"cctv\":\"123\"}";
        Assertions.assertEquals(source, starEngine.process(source));
    }

    @Test
    @Order(1012)
    void testStarJsonNoKeyEnd() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone,\"mobile\":\"12345678\"}";
        String target = "{\"phone,\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1021)
    void testStarJsonNoValue() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\",\"mobile\":\"12345678\"}";
        String target = "{\"phone\",\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1022)
    void testStarJsonIgnoreValue() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":  12345678,\"mobile\":\"12345678\"}";
        String target = "{\"phone\":  12****78,\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1023)
    void testStarJsonMutipleValue() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"12345678\",\"mobile\":\"12345678\"}";
        String target = "{\"phone\":\"12****78\",\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1031)
    void testStarJsonLevel() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":{\"mobile\":\"12345678\"}}";
        String target = "{\"phone\":{\"mobile\":\"12****78\"}}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1032)
    void testStarJsonInline() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\": {\\\"phone\\\": \\\"12345678\\\"}}";
        String target = "{\"phone\": {\\\"phone\\\": \\\"12****78\\\"}}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1041)
    void testStarJsonEscapeDoubleQuoteFirst() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"\\\"12345678\"}";
        String target = "{\"phone\":\"\\\"******78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1042)
    void testStarJsonEscapeDoubleQuoteMiddle() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"12345\\\"678\"}";
        String target = "{\"phone\":\"12******78\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1043)
    void testStarJsonEscapeDoubleQuoteEnd() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"12345678\\\"\"}";
        String target = "{\"phone\":\"12******\\\"\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1051)
    void testStarJsonEscapeBackSlashOnEnd() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"12345678\\\\\"}";
        String target = "{\"phone\":\"12******\\\\\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1052)
    void testStarJsonEscapeBackSlashOnly() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":\"\\\\\\\\\\\\\"}";
        String target = "{\"phone\":\"\\\\**\\\\\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1061)
    void testStarJsonArray() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":[\"12345678\"]}";
        String target = "{\"phone\":[\"12****78\"]}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1062)
    void testStarJsonArrayNumber() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":[12345678,,12345678,  ,]}";
        String target = "{\"phone\":[12****78,,12****78,  ,]}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(1063)
    void testStarJsonArrayInline() {
        StarJsonEngine starEngine = getDefaultEngine();

        String source = "{\"phone\":[12345678,,[12345678]}";
        String target = "{\"phone\":[12****78,,[12345678]}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(2000)
    void testStarJsonNoBorder() {
        StarJsonEngine starEngine = StarJsonEngine.create(keywords, false, false, 0, 0);

        String source = "{\"phone\":\"12345678\"}";
        String target = "{\"phone\":\"********\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(2001)
    void testStarJsonIgnoreCase() {
        StarJsonEngine starEngine = StarJsonEngine.create(keywords, true, false, 0, 0);

        String source = "{\"PhonE\":\"12345678\"}";
        String target = "{\"PhonE\":\"********\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(2002)
    void testStarJsonIgnoreCaseLeft1() {
        StarJsonEngine starEngine = StarJsonEngine.create(keywords, true, false, 1, 0);

        String source = "{\"PhonE\":\"12345678\"}";
        String target = "{\"PhonE\":\"1*******\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }

    @Test
    @Order(2001)
    void testStarJsonIgnoreCaseRight1() {
        StarJsonEngine starEngine = StarJsonEngine.create(keywords, true, false, 0, 1);

        String source = "{\"PhonE\":\"12345678\"}";
        String target = "{\"PhonE\":\"*******8\"}";
        Assertions.assertEquals(target, starEngine.process(source));
    }
}
