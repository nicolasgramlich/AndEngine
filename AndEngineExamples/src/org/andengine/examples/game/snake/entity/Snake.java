package org.andengine.examples.game.snake.entity;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.examples.game.snake.adt.Direction;
import org.andengine.examples.game.snake.adt.SnakeSuicideException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:11:44 - 09.07.2010
 */
public class Snake extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SnakeHead mHead;
	private final LinkedList<SnakeTailPart> mTail = new LinkedList<SnakeTailPart>();

	private Direction mDirection;
	private boolean mGrow;
	private final ITextureRegion mTailPartTextureRegion;
	private Direction mLastMoveDirection;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Snake(final Direction pInitialDirection, final int pCellX, final int pCellY, final TiledTextureRegion pHeadTextureRegion, final ITextureRegion pTailPartTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0);
		this.mTailPartTextureRegion = pTailPartTextureRegion;
		this.mHead = new SnakeHead(pCellX, pCellY, pHeadTextureRegion, pVertexBufferObjectManager);
		this.attachChild(this.mHead);
		this.setDirection(pInitialDirection);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Direction getDirection() {
		return this.mDirection;
	}

	public void setDirection(final Direction pDirection) {
		if(this.mLastMoveDirection != Direction.opposite(pDirection)) {
			this.mDirection = pDirection;
			this.mHead.setRotation(pDirection);
		}
	}

	public int getTailLength() {
		return this.mTail.size();
	}

	public SnakeHead getHead() {
		return this.mHead;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void grow() {
		this.mGrow = true;
	}

	public int getNextX() {
		return Direction.addToX(this.mDirection, this.mHead.getCellX());
	}

	public int getNextY() {
		return Direction.addToY(this.mDirection, this.mHead.getCellY());
	}

	public void move() throws SnakeSuicideException {
		this.mLastMoveDirection = this.mDirection;
		if(this.mGrow) {
			this.mGrow = false;
			/* If the snake should grow,
			 * simply add a new part in the front of the tail,
			 * where the head currently is. */
			final SnakeTailPart newTailPart = new SnakeTailPart(this.mHead, this.mTailPartTextureRegion, this.mHead.getVertexBufferObjectManager());
			this.attachChild(newTailPart);
			this.mTail.addFirst(newTailPart);
		} else {
			if(this.mTail.isEmpty() == false) {
				/* First move the end of the tail to where the head currently is. */
				final SnakeTailPart tailEnd = this.mTail.removeLast();
				tailEnd.setCell(this.mHead);
				this.mTail.addFirst(tailEnd);
			}
		}

		/* The move the head into the direction of the snake. */
		this.mHead.setCell(this.getNextX(), this.getNextY());
		
		/* Check if head collides with tail. */
		for(int i = this.mTail.size() - 1; i >= 0; i--) {
			if(this.mHead.isInSameCell(this.mTail.get(i))) {
				throw new SnakeSuicideException();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
