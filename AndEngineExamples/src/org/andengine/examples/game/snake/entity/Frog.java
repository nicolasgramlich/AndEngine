package org.andengine.examples.game.snake.entity;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:08:58 - 11.07.2010
 */
public class Frog extends AnimatedCellEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public Frog(final int pCellX, final int pCellY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCellX, pCellY, CELL_WIDTH, CELL_HEIGHT, pTiledTextureRegion, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
