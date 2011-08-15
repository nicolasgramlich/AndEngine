package org.anddev.andengine.opengl.vbo;

import org.anddev.andengine.opengl.shader.ShaderProgram;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:22:06 - 15.08.2011
 */
public class VertexBufferObjectAttributes {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mStride;
	private final VertexBufferObjectAttribute[] mVertexBufferObjectAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VertexBufferObjectAttributes(final int pStride, final VertexBufferObjectAttribute ... pVertexBufferObjectAttributes) {
		this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;
		this.mStride = pStride;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void enableVertexBufferObjectAttributes(final ShaderProgram pShaderProgram) {
		final VertexBufferObjectAttribute[] vertexBufferObjectAttributes = this.mVertexBufferObjectAttributes;

		final int stride = this.mStride;

		final int vertexBuggerObjectAttributeCount = vertexBufferObjectAttributes.length;
		for(int i = 0; i < vertexBuggerObjectAttributeCount; i++) {
			vertexBufferObjectAttributes[i].enable(pShaderProgram, stride);
		}
	}

	public void disableVertexBufferObjectAttributes(final ShaderProgram pShaderProgram) {
		final VertexBufferObjectAttribute[] vertexBufferObjectAttributes = this.mVertexBufferObjectAttributes;
		final int vertexBuggerObjectAttributeCount = vertexBufferObjectAttributes.length;
		for(int i = 0; i < vertexBuggerObjectAttributeCount; i++) {
			vertexBufferObjectAttributes[i].disable(pShaderProgram);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}