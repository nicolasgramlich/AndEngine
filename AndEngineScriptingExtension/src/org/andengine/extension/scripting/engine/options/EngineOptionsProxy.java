package org.andengine.extension.scripting.engine.options;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;


public class EngineOptionsProxy extends EngineOptions {
    private final long mAddress;

    public EngineOptionsProxy(final long pAddress, final boolean pFullscreen,
        final ScreenOrientation pScreenOrientation,
        final IResolutionPolicy pResolutionPolicy, final Camera pCamera) {
        super(pFullscreen, pScreenOrientation, pResolutionPolicy, pCamera);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
