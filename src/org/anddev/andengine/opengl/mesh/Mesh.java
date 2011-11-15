package org.anddev.andengine.opengl.mesh;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.vbo.IVertexBufferObject;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:11:34 - 05.09.2011
 */
public abstract class Mesh<V extends IVertexBufferObject> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final V mVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Mesh(final V pVertexBufferObject) {
		this.mVertexBufferObject = pVertexBufferObject;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public V getVertexBufferObject() {
		return this.mVertexBufferObject;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void preDraw(final ShaderProgram pShaderProgram) {
		this.mVertexBufferObject.bind(pShaderProgram);
	}

	public void draw(final int pPrimitiveType, final int pCount) {
		GLES20.glDrawArrays(pPrimitiveType, 0, pCount);
	}
	
	public void draw(final int pPrimitiveType, final int pOffset, final int pCount) {
		GLES20.glDrawArrays(pPrimitiveType, pOffset, pCount);
	}
	
	public void postDraw(final ShaderProgram pShaderProgram) {
		this.mVertexBufferObject.unbind(pShaderProgram);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}