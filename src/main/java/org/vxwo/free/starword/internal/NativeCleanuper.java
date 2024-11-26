package org.vxwo.free.starword.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NativeCleanuper {

    private static class CleanupNativeObject {
        private final int nativeType;
        private final long nativePtr;

        protected CleanupNativeObject(int nativeType, long nativePtr) {
            this.nativeType = nativeType;
            this.nativePtr = nativePtr;
        }

        public int getNativeType() {
            return nativeType;
        }

        public long getNativePtr() {
            return nativePtr;
        }
    }

    private static final ReferenceQueue<BaseNativeObject> referenceQueue = new ReferenceQueue<>();
    private static final Map<Reference<?>, CleanupNativeObject> referenceObjects =
            new ConcurrentHashMap<>();

    static {
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    Reference<?> ref = referenceQueue.remove();
                    CleanupNativeObject object = referenceObjects.remove(ref);
                    if (object != null) {
                        BaseNativeObject.nativeCleanup(object.getNativeType(),
                                object.getNativePtr());
                    }
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
        Reference<?> ref = new PhantomReference<>(referent, referenceQueue);
        referenceObjects.put(ref,
                new CleanupNativeObject(referent.getNativeType(), referent.getNativePtr()));
    }
}
