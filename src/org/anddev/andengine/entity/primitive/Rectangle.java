package org.anddev.andengine.entity.primitive;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Rectangle extends BaseRectangle {
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
		super(pX, pY, pWidth, pHeight);
	}

	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pWidth, pHeight, pRectangleVertexBuffer);
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

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
