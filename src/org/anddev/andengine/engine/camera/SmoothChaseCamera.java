package org.anddev.andengine.engine.camera;

import org.anddev.andengine.entity.IDynamicEntity;
import org.anddev.andengine.entity.IStaticEntity;

/**
 * @author Nicolas Gramlich
 * @since 15:57:13 - 27.03.2010
 */
public class SmoothChaseCamera extends SmoothCamera {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private IStaticEntity mChaseEntity;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SmoothChaseCamera(final float pX, final float pY, final float pWidth, final float pHeight, float pMaxVelocityX, float pMaxVelocityY, float pMaxZoomFactorChange, final IStaticEntity pChaseEntity) {
		super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);
		this.mChaseEntity = pChaseEntity;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setChaseEntity(final IDynamicEntity pDynamicEntity) {
		this.mChaseEntity = pDynamicEntity;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
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
