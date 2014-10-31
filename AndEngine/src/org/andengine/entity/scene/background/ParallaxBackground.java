package org.andengine.entity.scene.background;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.util.GLState;

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

		for(int i = 0; i < this.mParallaxEntityCount; i++) {
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
		if(!success) {
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
		final IAreaShape mAreaShape;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntity(final float pParallaxFactor, final IAreaShape pAreaShape) {
			this.mParallaxFactor = pParallaxFactor;
			this.mAreaShape = pAreaShape;
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
				final float shapeWidthScaled = this.mAreaShape.getWidthScaled();
				float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeWidthScaled;

				while(baseOffset > 0) {
					baseOffset -= shapeWidthScaled;
				}
				pGLState.translateModelViewGLMatrixf(baseOffset, 0, 0);

				float currentMaxX = baseOffset;
				
				do {
					this.mAreaShape.onDraw(pGLState, pCamera);
					pGLState.translateModelViewGLMatrixf(shapeWidthScaled, 0, 0);
					currentMaxX += shapeWidthScaled;
				} while(currentMaxX < cameraWidth);
			}
			pGLState.popModelViewGLMatrix();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
