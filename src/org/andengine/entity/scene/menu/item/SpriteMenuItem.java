package org.andengine.entity.scene.menu.item;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:15:20 - 01.04.2010
 */
public class SpriteMenuItem extends Sprite implements IMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getID() {
		return this.mID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSelected() {
		/* Nothing. */
	}

	@Override
	public void onUnselected() {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
