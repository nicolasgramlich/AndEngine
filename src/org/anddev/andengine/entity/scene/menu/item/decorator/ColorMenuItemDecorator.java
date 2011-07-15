package org.anddev.andengine.entity.scene.menu.item.decorator;

import org.anddev.andengine.entity.scene.menu.item.IMenuItem;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:25:35 - 07.07.2010
 */
public class ColorMenuItemDecorator extends BaseMenuItemDecorator {
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

	public ColorMenuItemDecorator(final IMenuItem pMenuItem, final float pSelectedRed, final float pSelectedGreen, final float pSelectedBlue, final float pUnselectedRed, final float pUnselectedGreen, final float pUnselectedBlue) {
		super(pMenuItem);

		this.mSelectedRed = pSelectedRed;
		this.mSelectedGreen = pSelectedGreen;
		this.mSelectedBlue = pSelectedBlue;

		this.mUnselectedRed = pUnselectedRed;
		this.mUnselectedGreen = pUnselectedGreen;
		this.mUnselectedBlue = pUnselectedBlue;

		pMenuItem.setColor(this.mUnselectedRed, this.mUnselectedGreen, this.mUnselectedBlue);
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
	public void onMenuItemSelected(final IMenuItem pMenuItem) {
		pMenuItem.setColor(this.mSelectedRed, this.mSelectedGreen, this.mSelectedBlue);
	}

	@Override
	public void onMenuItemUnselected(final IMenuItem pMenuItem) {
		pMenuItem.setColor(this.mUnselectedRed, this.mUnselectedGreen, this.mUnselectedBlue);
	}

	@Override
	public void onMenuItemReset(final IMenuItem pMenuItem) {
		pMenuItem.setColor(this.mUnselectedRed, this.mUnselectedGreen, this.mUnselectedBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
