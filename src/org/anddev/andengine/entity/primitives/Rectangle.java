package org.anddev.andengine.entity.primitives;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Rectangle extends RectangularShape {
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
		this.updateVertexBuffer();
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
		GLHelper.disableTextures(pGL);
		GLHelper.disableTexCoordArray(pGL);
	}

	@Override
	public RectangleVertexBuffer getVertexBuffer() {
		return (RectangleVertexBuffer)super.getVertexBuffer();
	}

	@Override
	protected void onUpdateVertexBuffer(){
		this.getVertexBuffer().update(0, 0, this.getBaseWidth(), this.getBaseHeight());
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
