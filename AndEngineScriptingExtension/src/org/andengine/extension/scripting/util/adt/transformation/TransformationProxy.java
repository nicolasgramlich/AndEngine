package org.andengine.extension.scripting.util.adt.transformation;

import org.andengine.util.adt.transformation.Transformation;


public class TransformationProxy extends Transformation {
    private final long mAddress;

    public TransformationProxy(final long pAddress) {
        super();
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
