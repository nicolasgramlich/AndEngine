package org.anddev.andengine.entity.primitive;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:05:49 - 11.04.2010
 */
public abstract class BaseRectangle extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseRectangle(final float pX, final float pY, final float pWidth, final float pHeight) {
		super(pX, pY, pWidth, pHeight, new RectangleVertexBuffer(GL11.GL_STATIC_DRAW, true));
		this.updateVertexBuffer();
	}

	public BaseRectangle(final float pX, final float pY, final float pWidth, final float pHeight, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pWidth, pHeight, pRectangleVertexBuffer);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public RectangleVertexBuffer getVertexBuffer() {
		return (RectangleVertexBuffer)this.mVertexBuffer;
	}

	@Override
	protected void onUpdateVertexBuffer(){
		this.getVertexBuffer().update(this.mWidth, this.mHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
