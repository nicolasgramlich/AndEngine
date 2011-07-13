package org.anddev.andengine.entity.scene.background;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.opengl.IDrawable;
import org.anddev.andengine.util.modifier.IModifier;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public void addBackgroundModifier(final IModifier<IBackground> pBackgroundModifier);
	public boolean removeBackgroundModifier(final IModifier<IBackground> pBackgroundModifier);
	public void clearBackgroundModifiers();

	public void setColor(final float pRed, final float pGreen, final float pBlue);
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha);
}
