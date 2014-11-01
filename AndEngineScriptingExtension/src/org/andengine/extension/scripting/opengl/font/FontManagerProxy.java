package org.andengine.extension.scripting.opengl.font;

import org.andengine.opengl.font.FontManager;


public class FontManagerProxy extends FontManager {
    private final long mAddress;

    public FontManagerProxy(final long pAddress) {
        super();
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
