package org.vxwo.jni.starjson;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import org.vxwo.jni.starjson.internal.NativeLoader;

public class StarJsonEngine {
    private static native long nativeCreate(String[] keywords, boolean skipNumber, int border);

    private static native void nativeCleanup(long ptr);

    private static native String nativeProcess(long ptr, String content);

    private static class CleanupReference extends PhantomReference<StarJsonEngine> {
        private final long nativePtr;

        public CleanupReference(StarJsonEngine referent, ReferenceQueue<? super StarJsonEngine> q,
                long nativePtr) {
            super(referent, q);
            this.nativePtr = nativePtr;
        }

        public void cleanup() {
            StarJsonEngine.nativeCleanup(nativePtr);
        }
    }

    private static final Thread cleanupThread;
    private static final ReferenceQueue<StarJsonEngine> referenceQueue = new ReferenceQueue<>();
    static {
        NativeLoader.loadLibrary("starjson");

        cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    CleanupReference ref = (CleanupReference) referenceQueue.remove();
                    ref.cleanup();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    private long nativePtr;

    private StarJsonEngine(long nativePtr) {
        this.nativePtr = nativePtr;
        new CleanupReference(this, referenceQueue, nativePtr);
    }

    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return nativeProcess(nativePtr, content);
    }

    public static StarJsonEngine create(String[] keywords, boolean skipNumber, int border) {
        return new StarJsonEngine(nativeCreate(keywords, skipNumber, border));
    }
}
