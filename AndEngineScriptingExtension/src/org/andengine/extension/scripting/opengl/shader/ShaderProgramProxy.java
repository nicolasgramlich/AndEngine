package org.andengine.extension.scripting.opengl.shader;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.source.IShaderSource;


public class ShaderProgramProxy extends ShaderProgram {
    private final long mAddress;

    public ShaderProgramProxy(final long pAddress,
        final String pVertexShaderSource, final String pFragmentShaderSource) {
        super(pVertexShaderSource, pFragmentShaderSource);
        this.mAddress = pAddress;
    }

    public ShaderProgramProxy(final long pAddress,
        final IShaderSource pVertexShaderSource,
        final IShaderSource pFragmentShaderSource) {
        super(pVertexShaderSource, pFragmentShaderSource);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
