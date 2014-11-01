package org.andengine.extension.scripting.entity.shape;

import org.andengine.entity.shape.Shape;

import org.andengine.opengl.shader.ShaderProgram;


public abstract class ShapeProxy extends Shape {
    private final long mAddress;

    public ShapeProxy(final long pAddress, final float pX, final float pY,
        final ShaderProgram pShaderProgram) {
        super(pX, pY, pShaderProgram);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
