package org.andengine.extension.scripting.opengl.vbo;

import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class VertexBufferObjectManagerProxy extends VertexBufferObjectManager {
    private final long mAddress;

    public VertexBufferObjectManagerProxy(final long pAddress) {
        super();
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
