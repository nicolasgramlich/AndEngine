package org.anddev.andengine.entity.primitives;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
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
		super(pX, pY, pWidth, pHeight, new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW));
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
		return (RectangleVertexBuffer)super.getVertexBuffer();
	}

	@Override
	protected void onUpdateVertexBuffer(){
		this.getVertexBuffer().onUpdate(0, 0, this.getBaseWidth(), this.getBaseHeight());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
