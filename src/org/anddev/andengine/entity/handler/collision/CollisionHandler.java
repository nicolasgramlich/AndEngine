package org.anddev.andengine.entity.handler.collision;

import java.util.ArrayList;

import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.StaticEntity;
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
	private final StaticEntity mCheckStaticEntity;
	private final ArrayList<? extends StaticEntity> mTargetStaticEntities;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CollisionHandler(final ICollisionCallback pCollisionCallback, final StaticEntity pCheckStaticEntity, final StaticEntity pTargetStaticEntity) {
		this(pCollisionCallback, pCheckStaticEntity, ListUtils.toList(pTargetStaticEntity));
	}
	
	public CollisionHandler(final ICollisionCallback pCollisionCallback, final StaticEntity pCheckStaticEntity, final ArrayList<? extends StaticEntity> pTargetStaticEntities) {
		assert (pCollisionCallback != null);
		assert (pCheckStaticEntity != null);
		assert (pTargetStaticEntities != null);
		
		this.mCollisionCallback = pCollisionCallback;
		this.mCheckStaticEntity = pCheckStaticEntity;
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
		final StaticEntity checkStaticEntity = this.mCheckStaticEntity;
		final ArrayList<? extends StaticEntity> staticEntities = this.mTargetStaticEntities;
		final int staticEntityCount = staticEntities.size();
		
		for(int i = 0; i < staticEntityCount; i++){
			if(checkStaticEntity.collidesWith(staticEntities.get(i))){
				final boolean proceed = this.mCollisionCallback.onCollision(checkStaticEntity, staticEntities.get(i));
				if(!proceed)
					return;
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
