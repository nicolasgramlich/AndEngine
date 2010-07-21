package org.anddev.andengine.engine.camera;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import org.anddev.andengine.entity.shape.IShape;

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

	private IShape mChaseShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ChaseCamera(final float pX, final float pY, final float pWidth, final float pHeight, final IShape pChaseShape) {
		super(pX, pY, pWidth, pHeight);
		this.mChaseShape = pChaseShape;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setChaseEntity(final IShape pChaseShape) {
		this.mChaseShape = pChaseShape;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);

		if(this.mChaseShape != null) {
			final float[] centerCoordinates = this.mChaseShape.getSceneCenterCoordinates();
			this.setCenter(centerCoordinates[VERTEX_INDEX_X], centerCoordinates[VERTEX_INDEX_Y]);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
