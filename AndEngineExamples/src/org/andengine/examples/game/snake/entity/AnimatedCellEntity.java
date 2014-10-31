package org.andengine.examples.game.snake.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:13:44 - 09.07.2010
 */
public abstract class AnimatedCellEntity extends AnimatedSprite implements SnakeConstants, ICellEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mCellX;
	protected int mCellY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimatedCellEntity(final int pCellX, final int pCellY, final int pWidth, final int pHeight, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		this.mCellX = pCellX;
		this.mCellY = pCellY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCellX() {
		return this.mCellX;
	}

	public int getCellY() {
		return this.mCellY;
	}

	public void setCell(final ICellEntity pCellEntity) {
		this.setCell(pCellEntity.getCellX(), pCellEntity.getCellY());
	}

	public void setCell(final int pCellX, final int pCellY) {
		this.mCellX = pCellX;
		this.mCellY = pCellY;
		this.setPosition(this.mCellX * CELL_WIDTH, this.mCellY * CELL_HEIGHT);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	@Override
	public boolean isInSameCell(final ICellEntity pCellEntity) {
		return this.mCellX == pCellEntity.getCellX() && this.mCellY == pCellEntity.getCellY();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
