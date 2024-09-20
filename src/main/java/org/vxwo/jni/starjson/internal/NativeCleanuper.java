package org.vxwo.jni.starjson.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import org.vxwo.jni.starjson.StarJsonEngine;

public class NativeCleanuper {
    private static class CleanupReference extends PhantomReference<StarJsonEngine> {
        private long nativePtr;

        public CleanupReference(StarJsonEngine referent, ReferenceQueue<? super StarJsonEngine> q,
                long nativePtr) {
            super(referent, q);
            this.nativePtr = nativePtr;
        }

        public void cleanup() {
            NativeEngine.cleanup(nativePtr);
        }
    }

    private static final ReferenceQueue<StarJsonEngine> referenceQueue = new ReferenceQueue<>();

    static {
        Thread cleanupThread = new Thread(() -> {
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

    public static void register(StarJsonEngine referent, long nativePtr) {
        new CleanupReference(referent, referenceQueue, nativePtr);
    }
}
