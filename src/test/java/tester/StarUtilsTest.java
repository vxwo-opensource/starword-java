package tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.vxwo.free.starword.StarMethod;
import org.vxwo.free.starword.StarUtils;

public class StarUtilsTest {

    @Test
    @Order(1000)
    void testStarUtilsSimple() {

        String source = "12345678";
        String target = "123**678";
        Assertions.assertEquals(target, StarUtils.process(new StarMethod(3, 3), source));

        String source1 = "12345678";
        String target1 = "********";
        Assertions.assertEquals(target1, StarUtils.process(new StarMethod(0, 0), source1));

        String source2 = "12345678@hello";
        String target2 = "********@hello";
        Assertions.assertEquals(target2, StarUtils.process(new StarMethod(0, 0, '@'), source2));
    }
}
