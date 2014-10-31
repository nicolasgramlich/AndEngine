package org.andengine.examples.game.snake.entity;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:39:05 - 11.07.2010
 */
public interface ICellEntity {
	// ===========================================================
	// Constants
	// ===========================================================
	
	public abstract int getCellX();
	public abstract int getCellY();

	public abstract void setCell(final ICellEntity pCellEntity);
	public abstract void setCell(final int pCellX, final int pCellY);

	public abstract boolean isInSameCell(final ICellEntity pCellEntity);
}