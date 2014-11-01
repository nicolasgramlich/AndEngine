package org.andengine.examples.game.snake.entity;

import org.andengine.examples.game.snake.adt.Direction;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:44:59 - 09.07.2010
 */
public class SnakeHead extends AnimatedCellEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SnakeHead(final int pCellX, final int pCellY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCellX, pCellY, CELL_WIDTH, 2 * CELL_HEIGHT, pTiledTextureRegion, pVertexBufferObjectManager);

		this.setRotationCenterY(CELL_HEIGHT / 2);
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

	public void setRotation(final Direction pDirection) {
		switch(pDirection) {
			case UP:
				this.setRotation(180);
				break;
			case DOWN:
				this.setRotation(0);
				break;
			case LEFT:
				this.setRotation(90);
				break;
			case RIGHT:
				this.setRotation(270);
				break;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
