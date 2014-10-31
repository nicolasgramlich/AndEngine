package org.andengine.entity.scene.background;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ModifierList;

import android.opengl.GLES20;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:08:17 - 19.07.2010
 */
public class Background implements IBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int BACKGROUNDMODIFIERS_CAPACITY_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private ModifierList<IBackground> mBackgroundModifiers = null;

	private final Color mColor = new Color(0, 0, 0, 1);
	private boolean mColorEnabled = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	protected Background() {

	}

	public Background(final float pRed, final float pGreen, final float pBlue) {
		this.mColor.set(pRed, pGreen, pBlue);
	}

	public Background(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mColor.set(pRed, pGreen, pBlue, pAlpha);
	}

	public Background(final Color pColor) {
		this.mColor.set(pColor);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Sets the color using the arithmetic scheme (0.0f - 1.0f RGB triple).
	 * @param pRed The red color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pGreen The green color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pBlue The blue color value. Should be between 0.0 and 1.0, inclusive.
	 */
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		this.mColor.set(pRed, pGreen, pBlue);
	}

	/**
	 * Sets the color using the arithmetic scheme (0.0f - 1.0f RGBA quadruple).
	 * @param pRed The red color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pGreen The green color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pBlue The blue color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pAlpha The alpha color value. Should be between 0.0 and 1.0, inclusive.
	 */
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mColor.set(pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public void setColor(final Color pColor) {
		this.mColor.set(pColor);
	}

	@Override
	public boolean isColorEnabled() {
		return this.mColorEnabled;
	}

	@Override
	public void setColorEnabled(final boolean pColorEnabled) {
		this.mColorEnabled = pColorEnabled;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void registerBackgroundModifier(final IModifier<IBackground> pBackgroundModifier) {
		if(this.mBackgroundModifiers == null) {
			this.allocateBackgroundModifiers();
		}
		this.mBackgroundModifiers.add(pBackgroundModifier);
	}

	@Override
	public boolean unregisterBackgroundModifier(final IModifier<IBackground> pBackgroundModifier) {
		if(this.mBackgroundModifiers != null) {
			return this.mBackgroundModifiers.remove(pBackgroundModifier);
		} else {
			return false;
		}
	}

	@Override
	public void clearBackgroundModifiers() {
		if(this.mBackgroundModifiers != null) {
			this.mBackgroundModifiers.clear();
		}
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mBackgroundModifiers != null) {
			this.mBackgroundModifiers.onUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void onDraw(final GLState pGLState, final Camera pCamera) {
		if(this.mColorEnabled) {
			GLES20.glClearColor(this.mColor.getRed(), this.mColor.getGreen(), this.mColor.getBlue(), this.mColor.getAlpha());
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); // TODO Does this cause problems when multisampling?
		}
	}

	@Override
	public void reset() {
		this.mBackgroundModifiers.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void allocateBackgroundModifiers() {
		this.mBackgroundModifiers = new ModifierList<IBackground>(this, Background.BACKGROUNDMODIFIERS_CAPACITY_DEFAULT);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
