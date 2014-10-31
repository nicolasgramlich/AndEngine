package org.andengine.extension.scripting.input.touch;

import org.andengine.input.touch.TouchEvent;


public class TouchEventProxy extends TouchEvent {
    private final long mAddress;

    public TouchEventProxy(final long pAddress) {
        super();
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
