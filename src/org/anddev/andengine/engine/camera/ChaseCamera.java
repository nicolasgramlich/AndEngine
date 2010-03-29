package org.anddev.andengine.engine.camera;

import org.anddev.andengine.entity.DynamicEntity;

/**
 * @author Nicolas Gramlich
 * @since 15:57:13 - 27.03.2010
 */
public class ChaseCamera extends Camera {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private DynamicEntity mChaseEntity;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ChaseCamera(float pX, float pY, float pWidth, float pHeight, final DynamicEntity pChaseEntity) {
		super(pX, pY, pWidth, pHeight);
		this.mChaseEntity = pChaseEntity;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setChaseEntity(DynamicEntity pDynamicEntity) {
		this.mChaseEntity = pDynamicEntity;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		
		if(this.mChaseEntity != null) {
			this.setCenter(this.mChaseEntity.getCenterX(), this.mChaseEntity.getCenterY());
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
