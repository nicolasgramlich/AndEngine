package org.andengine.engine.handler.collision;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.util.adt.list.ListUtils;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:19:35 - 11.03.2010
 */
public class CollisionHandler implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ICollisionCallback mCollisionCallback;
	private final IShape mCheckShape;
	private final ArrayList<? extends IShape> mTargetStaticEntities;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CollisionHandler(final ICollisionCallback pCollisionCallback, final IShape pCheckShape, final IShape pTargetShape) throws IllegalArgumentException {
		this(pCollisionCallback, pCheckShape, ListUtils.toList(pTargetShape));
	}

	public CollisionHandler(final ICollisionCallback pCollisionCallback, final IShape pCheckShape, final ArrayList<? extends IShape> pTargetStaticEntities) throws IllegalArgumentException {
		if (pCollisionCallback == null) {
			throw new IllegalArgumentException( "pCollisionCallback must not be null!");
		}
		if (pCheckShape == null) {
			throw new IllegalArgumentException( "pCheckShape must not be null!");
		}
		if (pTargetStaticEntities == null) {
			throw new IllegalArgumentException( "pTargetStaticEntities must not be null!");
		}

		this.mCollisionCallback = pCollisionCallback;
		this.mCheckShape = pCheckShape;
		this.mTargetStaticEntities = pTargetStaticEntities;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final IShape checkShape = this.mCheckShape;
		final ArrayList<? extends IShape> staticEntities = this.mTargetStaticEntities;
		final int staticEntityCount = staticEntities.size();

		for(int i = 0; i < staticEntityCount; i++){
			if(checkShape.collidesWith(staticEntities.get(i))){
				final boolean proceed = this.mCollisionCallback.onCollision(checkShape, staticEntities.get(i));
				if(!proceed) {
					return;
				}
			}
		}
	}

	@Override
	public void reset() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
