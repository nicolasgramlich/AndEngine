package org.anddev.andengine.entity.scene.background;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.opengl.IDrawable;

/**
 * @author Nicolas Gramlich
 * @since 13:47:41 - 19.07.2010
 */
public interface IBackground extends IDrawable, IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void setColor(final float pRed, final float pGreen, final float pBlue);
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha);
}
