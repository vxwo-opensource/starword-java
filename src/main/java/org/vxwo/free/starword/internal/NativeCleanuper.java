package org.vxwo.free.starword.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class NativeCleanuper {
    public static int TYPE_JSON = 1;
    public static int TYPE_TEXT = 2;

    private static class CleanupReference extends PhantomReference<BaseNativeObject> {
        private int nativeType;
        private long nativePtr;

        public CleanupReference(BaseNativeObject referent,
                ReferenceQueue<? super BaseNativeObject> q) {
            super(referent, q);
            this.nativeType = referent.getNativeType();
            this.nativePtr = referent.getNativePtr();
        }

        public void cleanup() {
            if (nativeType == TYPE_JSON) {
                NativeEngine.starJsonCleanup(nativePtr);
            } else if (nativeType == TYPE_TEXT) {
                NativeEngine.starTextCleanup(nativePtr);
            }
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
