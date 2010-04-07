package org.anddev.andengine.entity.menu;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 20:15:20 - 01.04.2010
 */
public class MenuItem extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mMenuID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuItem(final int pMenuID, final TextureRegion pTextureRegion) {
		super(0, 0, pTextureRegion);
		this.mMenuID = pMenuID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getMenuID() {
		return this.mMenuID;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
