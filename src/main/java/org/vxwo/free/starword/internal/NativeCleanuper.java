package org.vxwo.free.starword.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class NativeCleanuper {

    private static class CleanupReference extends PhantomReference<BaseNativeObject> {
        private final int nativeType;
        private final long nativePtr;

        public CleanupReference(BaseNativeObject referent,
                ReferenceQueue<? super BaseNativeObject> q) {
            super(referent, q);
            this.nativeType = referent.getNativeType();
            this.nativePtr = referent.getNativePtr();
        }

        public void cleanup() {
            BaseNativeObject.nativeCleanup(nativeType, nativePtr);
        }
    }

    private static final ReferenceQueue<BaseNativeObject> referenceQueue = new ReferenceQueue<>();

    static {
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    CleanupReference ref = (CleanupReference) referenceQueue.remove();
                    ref.cleanup();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "starword-reference-cleanup");
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    public static void register(BaseNativeObject referent) {
        new CleanupReference(referent, referenceQueue);
    }
}
