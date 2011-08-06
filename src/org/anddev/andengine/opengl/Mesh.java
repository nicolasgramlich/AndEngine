package org.anddev.andengine.opengl;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;

import android.opengl.GLES20;

/**
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

	final VertexBufferObject mVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================


	public Mesh (final int pCapacity, final int pDrawType, final boolean pManaged) {
		this.mVertexBufferObject = new VertexBufferObject(pCapacity, pDrawType, pManaged);
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

	public void bind (final ShaderProgram pShaderProgram) {
		this.mVertexBufferObject.bind(pShaderProgram);
	}

	public void unbind (final ShaderProgram pShaderProgram) {
		this.mVertexBufferObject.unbind(pShaderProgram);
	}

	public void render (final ShaderProgram pShaderProgram, final int pPrimitiveType, final int pCount) {
		this.render(pShaderProgram, pPrimitiveType, 0, pCount);
	}

	public void render (final ShaderProgram pShaderProgram, final int pPrimitiveType, final int pOffset, final int pCount) {
		this.bind(pShaderProgram);

		GLES20.glDrawArrays(pPrimitiveType, pOffset, pCount);

		this.unbind(pShaderProgram);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}