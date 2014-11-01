package org.andengine.extension.scripting.entity.shape;

import org.andengine.entity.shape.RectangularShape;

import org.andengine.opengl.shader.ShaderProgram;


public abstract class RectangularShapeProxy extends RectangularShape {
    private final long mAddress;

    public RectangularShapeProxy(final long pAddress, final float pX,
        final float pY, final float pWidth, final float pHeight,
        final ShaderProgram pShaderProgram) {
        super(pX, pY, pWidth, pHeight, pShaderProgram);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
