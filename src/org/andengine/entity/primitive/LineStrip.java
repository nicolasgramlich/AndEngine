package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.vbo.ILineChainVertexBufferObject;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 13:51:36 - 01.06.2013
 */
public class LineStrip extends LineChain {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LineStrip(final float pX, final float pY, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pCapacity, pVertexBufferObjectManager);
	}

	public LineStrip(final float pX, final float pY, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pCapacity, pVertexBufferObjectManager, pDrawType);
	}

	public LineStrip(final float pX, final float pY, final float pLineWidth, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pLineWidth, pCapacity, pVertexBufferObjectManager);
	}

	public LineStrip(final float pX, final float pY, final float pLineWidth, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pLineWidth, pCapacity, pVertexBufferObjectManager, pDrawType);
	}

	public LineStrip(final float pX, final float pY, final float pLineWidth, final int pCapacity, final ILineChainVertexBufferObject pLineChainVertexBufferObject) {
		super(pX, pY, pLineWidth, pCapacity, pLineChainVertexBufferObject);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mLineChainVertexBufferObject.draw(GLES20.GL_LINE_STRIP, this.mIndex);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
