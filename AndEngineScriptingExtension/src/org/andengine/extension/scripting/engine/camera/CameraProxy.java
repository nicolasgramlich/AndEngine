package org.andengine.extension.scripting.engine.camera;

import org.andengine.engine.camera.Camera;


public class CameraProxy extends Camera {
    private final long mAddress;

    public CameraProxy(final long pAddress, final float pX, final float pY,
        final float pWidth, final float pHeight) {
        super(pX, pY, pWidth, pHeight);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
