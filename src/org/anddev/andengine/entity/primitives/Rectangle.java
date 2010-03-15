package org.anddev.andengine.entity.primitives;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Rectangle extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight) {
		super(pX, pY, pWidth, pHeight, new RectangleVertexBuffer());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.disableTextures(pGL); // TODO Maybe costly when this is always set...
		GLHelper.disableTexCoordArray(pGL);
	}
	
	@Override
	public RectangleVertexBuffer getVertexBuffer() {
		return (RectangleVertexBuffer)super.getVertexBuffer();
	}
	
	protected void updateVertexBuffer(){
		this.getVertexBuffer().update(this.getOffsetX(), this.getOffsetY(), this.getWidth(), this.getHeight());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
