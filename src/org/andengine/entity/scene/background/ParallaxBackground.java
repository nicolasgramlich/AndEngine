package org.andengine.entity.scene.background;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:36:26 - 19.07.2010
 */
public class ParallaxBackground extends Background {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ParallaxEntity> mParallaxEntities = new ArrayList<ParallaxEntity>();
	private int mParallaxEntityCount;

	protected float mParallaxValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallaxBackground(final float pRed, final float pGreen, final float pBlue) {
		super(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxValue(final float pParallaxValue) {
		this.mParallaxValue = pParallaxValue;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GLState pGLState, final Camera pCamera) {
		super.onDraw(pGLState, pCamera);

		final float parallaxValue = this.mParallaxValue;
		final ArrayList<ParallaxEntity> parallaxEntities = this.mParallaxEntities;

		for (int i = 0; i < this.mParallaxEntityCount; i++) {
			parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxValue);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void attachParallaxEntity(final ParallaxEntity pParallaxEntity) {
		this.mParallaxEntities.add(pParallaxEntity);
		this.mParallaxEntityCount++;
	}

	public boolean detachParallaxEntity(final ParallaxEntity pParallaxEntity) {
		this.mParallaxEntityCount--;
		final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
		if (!success) {
			this.mParallaxEntityCount++;
		}
		return success;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ParallaxEntity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		final float mParallaxFactor;
		final IEntity mEntity;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntity(final float pParallaxFactor, final IEntity pEntity) {
			this.mParallaxFactor = pParallaxFactor;
			this.mEntity = pEntity;

			// TODO Adjust onDraw calculations, so that these assumptions aren't necessary.
			if (this.mEntity.getX() != 0) {
				Debug.w("The X position of a " + this.getClass().getSimpleName() + " is expected to be 0.");
			}

			if (this.mEntity.getOffsetCenterX() != 0) {
				Debug.w("The OffsetCenterXposition of a " + this.getClass().getSimpleName() + " is expected to be 0.");
			}
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

		public void onDraw(final GLState pGLState, final Camera pCamera, final float pParallaxValue) {
			pGLState.pushModelViewGLMatrix();
			{
				final float cameraWidth = pCamera.getWidth();
				final float entityWidthScaled = this.mEntity.getWidth() * this.mEntity.getScaleX();
				float baseOffset = (pParallaxValue * this.mParallaxFactor) % entityWidthScaled;

				while (baseOffset > 0) {
					baseOffset -= entityWidthScaled;
				}
				pGLState.translateModelViewGLMatrixf(baseOffset, 0, 0);

				float currentMaxX = baseOffset;

				do {
					this.mEntity.onDraw(pGLState, pCamera);
					pGLState.translateModelViewGLMatrixf(entityWidthScaled, 0, 0);
					currentMaxX += entityWidthScaled;
				} while (currentMaxX < cameraWidth);
			}
			pGLState.popModelViewGLMatrix();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
