package org.anddev.andengine.opengl;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttribute;

import android.opengl.GLES20;

/**
 * TODO Support indexed Meshes.
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 9:11:34 PM - Aug 5, 2011
 */
public class Mesh {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final VertexBufferObject mVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Mesh(final int pCapacity, final int pDrawType, final boolean pManaged, final VertexBufferObjectAttribute ... pVertexBufferObjectAttributes) {
		this.mVertexBufferObject = new VertexBufferObject(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public VertexBufferObject getVertexBufferObject() {
		return this.mVertexBufferObject;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void draw(final ShaderProgram pShaderProgram, final int pPrimitiveType, final int pCount) {
		this.draw(pShaderProgram, pPrimitiveType, 0, pCount);
	}

	public void draw(final ShaderProgram pShaderProgram, final int pPrimitiveType, final int pOffset, final int pCount) {
		this.mVertexBufferObject.bind(pShaderProgram);

		GLES20.glDrawArrays(pPrimitiveType, pOffset, pCount);

		this.mVertexBufferObject.unbind(pShaderProgram);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}