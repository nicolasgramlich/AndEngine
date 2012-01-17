package org.andengine.entity.scene.menu.item;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:44:39 - 07.07.2010
 */
public class AnimatedSpriteMenuItem extends AnimatedSprite implements IMenuItem {
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

	public AnimatedSpriteMenuItem(final int pID, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager);
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
