package org.andengine.extension.scripting.opengl.util;

import org.andengine.opengl.util.GLState;


public class GLStateProxy extends GLState {
    private final long mAddress;

    public GLStateProxy(final long pAddress) {
        super();
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
