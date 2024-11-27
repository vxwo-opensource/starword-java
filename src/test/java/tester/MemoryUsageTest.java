package tester;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.vxwo.free.starword.StarJsonEngine;
import org.vxwo.free.starword.StarOptions;

@Disabled
public class MemoryUsageTest {
    private static final int GC_COUNT = 100;
    private static final int THREAD_COUNT = 100;
    private static final int TEXT_LENGTH = 10000;

    private static final String[] keywords = new String[] {"test", repeatString("a", TEXT_LENGTH),
            repeatString("b", TEXT_LENGTH), repeatString("c", TEXT_LENGTH)};
    private static final String source = "{\"test\":\"" + repeatString("a", TEXT_LENGTH) + "\"}";
    private static final String target = "{\"test\":\"" + repeatString("*", TEXT_LENGTH) + "\"}";

    private static String repeatString(String str, int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result = result + str;
        }

        return result;
    }

    private StarJsonEngine getDefaultEngine() {
        return StarJsonEngine.create(keywords, new StarOptions(false, 0, 0));
    }

    @Test
    void testProcess() throws InterruptedException {
        StarJsonEngine starEngine = getDefaultEngine();
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    for (int i = 0; i < GC_COUNT; ++i) {
                        Assertions.assertEquals(target, starEngine.process(source));
                        Thread.sleep(50);
                    }

                    System.gc();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    void testInstance() throws InterruptedException {
        List<StarJsonEngine> cached = new ArrayList<>();
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    for (int i = 0; i < GC_COUNT; ++i) {
                        StarJsonEngine starEngine = getDefaultEngine();
                        cached.add(starEngine);

                        starEngine.process(source);
                        Thread.sleep(50);
                    }

                    cached.clear();
                    System.gc();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
            }
        });
        thread.start();
        thread.join();
    }


    @Test
    void testThreads() throws InterruptedException {
        StarJsonEngine starEngine = getDefaultEngine();
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(() -> {
                try {
                    while (true) {
                        Assertions.assertEquals(target, starEngine.process(source));
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (Exception ex) {
                }
            });
        }

        for (int i = 0; i < threads.length; ++i) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; ++i) {
            threads[i].join();
        }
    }
}
