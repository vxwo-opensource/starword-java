package tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.vxwo.starkeyword.StarJsonEngine;

public class StarJsonEngineTest {
    private static StarJsonEngine starEngine =
            StarJsonEngine.create(new String[] {"phone", "mobile"}, false, 2);

    @Test
    @Order(1000)
    void testStarJsonIgnore() {
        String json = "{\"number\":\"12345678\"}";
        String target = "{\"number\":\"12345678\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1001)
    void testStarJsonSimple() {
        String json = "{\"phone\":\"12345678\"}";
        String target = "{\"phone\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1002)
    void testStarJsonEmpty() {
        String json = "{\"phone\":\"\"}";
        String target = "{\"phone\":\"\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1003)
    void testStarJsonSymbol() {
        String json = "{\"phone\": null, \"num\": 1}";
        String target = "{\"phone\": null, \"num\": 1}";
        Assertions.assertEquals(target, starEngine.process(json));

        String json1 = "{\"phone\": true, \"mobile\": false}";
        String target1 = "{\"phone\": true, \"mobile\": false}";
        Assertions.assertEquals(target1, starEngine.process(json1));

        String json2 = "{\"phone\": \"null\"}";
        String target2 = "{\"phone\": \"nu**\"}";
        Assertions.assertEquals(target2, starEngine.process(json2));
    }

    @Test
    @Order(1004)
    void testStarJsonNUmber() {
        String json1 = "{\"phone\": 12345678}";
        String target1 = "{\"phone\": 12****78}";
        Assertions.assertEquals(target1, starEngine.process(json1));

        String json2 = "{\"phone\": -}";
        String target2 = "{\"phone\": *}";
        Assertions.assertEquals(target2, starEngine.process(json2));

        String json3 = "{\"phone\": -....}";
        String target3 = "{\"phone\": -.*..}";
        Assertions.assertEquals(target3, starEngine.process(json3));

        String json4 = "{\"phone\": -0.12345678.}";
        String target4 = "{\"phone\": -0********8.}";
        Assertions.assertEquals(target4, starEngine.process(json4));

        String json5 = "{\"phone\": -0.123}";
        String target5 = "{\"phone\": -0**23}";
        Assertions.assertEquals(target5, starEngine.process(json5));

        String json6 = "{\"phone\": 1234e5678}";
        String target6 = "{\"phone\": 12*****78}";
        Assertions.assertEquals(target6, starEngine.process(json6));

        String json7 = "{\"phone\": 1234E5678}";
        String target7 = "{\"phone\": 12*****78}";
        Assertions.assertEquals(target7, starEngine.process(json7));

        String json8 = "{\"phone\": 1234.e5678}";
        String target8 = "{\"phone\": 12******78}";
        Assertions.assertEquals(target8, starEngine.process(json8));

        String json9 = "{\"phone\": 1234e.5678}";
        String target9 = "{\"phone\": 12******78}";
        Assertions.assertEquals(target9, starEngine.process(json9));
    }

    @Test
    @Order(1011)
    void testStarJsonNoKey() {
        String json = "{\"cctv\":\"123\"}";
        Assertions.assertEquals(json, starEngine.process(json));
    }

    @Test
    @Order(1012)
    void testStarJsonNoKeyEnd() {
        String json = "{\"phone,\"mobile\":\"12345678\"}";
        String target = "{\"phone,\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1021)
    void testStarJsonNoValue() {
        String json = "{\"phone\",\"mobile\":\"12345678\"}";
        String target = "{\"phone\",\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1022)
    void testStarJsonIgnoreValue() {
        String json = "{\"phone\":  12345678,\"mobile\":\"12345678\"}";
        String target = "{\"phone\":  12****78,\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1023)
    void testStarJsonMutipleValue() {
        String json = "{\"phone\":\"12345678\",\"mobile\":\"12345678\"}";
        String target = "{\"phone\":\"12****78\",\"mobile\":\"12****78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1031)
    void testStarJsonLevel() {
        String json = "{\"phone\":{\"mobile\":\"12345678\"}}";
        String target = "{\"phone\":{\"mobile\":\"12****78\"}}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1032)
    void testStarJsonInline() {
        String json = "{\"phone\": {\\\"phone\\\": \\\"12345678\\\"}}";
        String target = "{\"phone\": {\\\"phone\\\": \\\"12****78\\\"}}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1041)
    void testStarJsonEscapeDoubleQuoteFirst() {
        String json = "{\"phone\":\"\\\"12345678\"}";
        String target = "{\"phone\":\"\\\"******78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1042)
    void testStarJsonEscapeDoubleQuoteMiddle() {
        String json = "{\"phone\":\"12345\\\"678\"}";
        String target = "{\"phone\":\"12******78\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1043)
    void testStarJsonEscapeDoubleQuoteEnd() {
        String json = "{\"phone\":\"12345678\\\"\"}";
        String target = "{\"phone\":\"12******\\\"\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1051)
    void testStarJsonEscapeBackSlashOnEnd() {
        String json = "{\"phone\":\"12345678\\\\\"}";
        String target = "{\"phone\":\"12******\\\\\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1052)
    void testStarJsonEscapeBackSlashOnly() {
        String json = "{\"phone\":\"\\\\\\\\\\\\\"}";
        String target = "{\"phone\":\"\\\\**\\\\\"}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1061)
    void testStarJsonArray() {
        String json = "{\"phone\":[\"12345678\"]}";
        String target = "{\"phone\":[\"12****78\"]}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1062)
    void testStarJsonArrayNumber() {
        String json = "{\"phone\":[12345678,,12345678,  ,]}";
        String target = "{\"phone\":[12****78,,12****78,  ,]}";
        Assertions.assertEquals(target, starEngine.process(json));
    }

    @Test
    @Order(1063)
    void testStarJsonArrayInline() {
        String json = "{\"phone\":[12345678,,[12345678]}";
        String target = "{\"phone\":[12****78,,[12345678]}";
        Assertions.assertEquals(target, starEngine.process(json));
    }
}
