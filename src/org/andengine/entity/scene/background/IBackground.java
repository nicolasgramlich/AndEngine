package org.andengine.entity.scene.background;

import org.andengine.engine.handler.IDrawHandler;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:47:41 - 19.07.2010
 */
public interface IBackground extends IDrawHandler, IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerBackgroundModifier(final IModifier<IBackground> pBackgroundModifier);
	public boolean unregisterBackgroundModifier(final IModifier<IBackground> pBackgroundModifier);
	public void clearBackgroundModifiers();

	/**
	 * @return Whether this background is allowed to show a color
	 */
	public boolean isColorEnabled();
	
	/**
	 * @param pColorEnabled Whether this background is allowed to show a color
	 */
	public void setColorEnabled(final boolean pColorEnabled);

	/**
	 * Sets the color of this background
	 * @param pColor The {@link Color} this background should have
	 */
	public void setColor(final Color pColor);
	/**
	 * Sets this background to an opaque color
	 * @param pRed Red value between 0f and 1f
	 * @param pGreen Green value between 0f and 1f
	 * @param pBlue Blue value between 0f and 1f
	 * @see #setColor(Color)
	 * @see #setColor(float, float, float, float)
	 */
	public void setColor(final float pRed, final float pGreen, final float pBlue);
	/**
	 * Sets this background to a transparant color
	 * @param pRed Red value between 0f and 1f
	 * @param pGreen Green value between 0f and 1f
	 * @param pBlue Blue value between 0f and 1f
	 * @param pAlpha Transparency value between 0f and 1f
	 */
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha);
}
