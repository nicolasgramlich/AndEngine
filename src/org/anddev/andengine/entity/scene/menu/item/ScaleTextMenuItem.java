package org.anddev.andengine.entity.scene.menu.item;

import org.anddev.andengine.opengl.font.Font;

/**
 * @author Nicolas Gramlich
 * @since 14:25:35 - 07.07.2010
 */
public class ScaleTextMenuItem extends TextMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mSelectedScale;
	private final float mUnSelectedScale;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleTextMenuItem(final int pID, final Font pFont, final String pText, final float pSelectedScale, final float pUnSelectedScale) {
		super(pID, pFont, pText);

		this.mSelectedScale = pSelectedScale;
		this.mUnSelectedScale = pUnSelectedScale;

		this.setScale(pUnSelectedScale);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSelected() {
		this.setScale(this.mSelectedScale);
	}

	@Override
	public void onUnselected() {
		this.setScale(this.mUnSelectedScale);
	}

	@Override
	public void reset() {
		super.reset();
		this.setScale(this.mUnSelectedScale);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
