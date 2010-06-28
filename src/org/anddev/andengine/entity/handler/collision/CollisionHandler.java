package org.anddev.andengine.entity.handler.collision;

import java.util.ArrayList;

import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.util.ListUtils;

/**
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
	private final Shape mCheckShape;
	private final ArrayList<? extends Shape> mTargetStaticEntities;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CollisionHandler(final ICollisionCallback pCollisionCallback, final Shape pCheckShape, final Shape pTargetShape) throws IllegalArgumentException {
		this(pCollisionCallback, pCheckShape, ListUtils.toList(pTargetShape));
	}

	public CollisionHandler(final ICollisionCallback pCollisionCallback, final Shape pCheckShape, final ArrayList<? extends Shape> pTargetStaticEntities) throws IllegalArgumentException {
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
		final Shape checkShape = this.mCheckShape;
		final ArrayList<? extends Shape> staticEntities = this.mTargetStaticEntities;
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
