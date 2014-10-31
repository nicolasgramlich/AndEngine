package org.andengine.extension.scripting.util.color;

import org.andengine.util.color.Color;


public class ColorProxy extends Color {
    private final long mAddress;

    public ColorProxy(final long pAddress, final Color pColor) {
        super(pColor);
        this.mAddress = pAddress;
    }

    public ColorProxy(final long pAddress, final float pRed,
        final float pGreen, final float pBlue) {
        super(pRed, pGreen, pBlue);
        this.mAddress = pAddress;
    }

    public ColorProxy(final long pAddress, final float pRed,
        final float pGreen, final float pBlue, final float pAlpha) {
        super(pRed, pGreen, pBlue, pAlpha);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
