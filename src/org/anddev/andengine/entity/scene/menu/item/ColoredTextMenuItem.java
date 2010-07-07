package org.anddev.andengine.entity.scene.menu.item;

import org.anddev.andengine.opengl.font.Font;

/**
 * @author Nicolas Gramlich
 * @since 14:25:35 - 07.07.2010
 */
public class ColoredTextMenuItem extends TextMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mSelectedRed;
	private final float mSelectedGreen;
	private final float mSelectedBlue;
	private final float mUnselectedRed;
	private final float mUnselectedGreen;
	private final float mUnselectedBlue;


	// ===========================================================
	// Constructors
	// ===========================================================

	public ColoredTextMenuItem(final int pID, final Font pFont, final String pText, final float pSelectedRed, final float pSelectedGreen, final float pSelectedBlue, final float pUnselectedRed, final float pUnselectedGreen, final float pUnselectedBlue) {
		super(pID, pFont, pText);

		this.mSelectedRed = pSelectedRed;
		this.mSelectedGreen = pSelectedGreen;
		this.mSelectedBlue = pSelectedBlue;

		this.mUnselectedRed = pUnselectedRed;
		this.mUnselectedGreen = pUnselectedGreen;
		this.mUnselectedBlue = pUnselectedBlue;
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
		this.setColor(this.mSelectedRed, this.mSelectedGreen, this.mSelectedBlue);
	}

	@Override
	public void onUnselected() {
		this.setColor(this.mUnselectedRed, this.mUnselectedGreen, this.mUnselectedBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
