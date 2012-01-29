/**
 * 
 */
package org.andengine.entity.shape;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.shader.ShaderProgram;

/**
 * 
 * @author Rodrigo Castro
 * @since 22:10:11 - 28.01.2012
 */
public abstract class PolygonShape extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public PolygonShape(float pX, float pY, ShaderProgram pShaderProgram) {
		super(pX, pY, pShaderProgram);
		// TODO Auto-generated constructor stub
		
		this.resetRotationCenter();
		this.resetScaleCenter();
		this.resetSkewCenter();
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	public void setBaseSize() {
		// TODO
	}
	
	@Override
	public boolean isCulled(final Camera pCamera) {
		// TODO
		return true;
	}

	@Override
	public void reset() {
		super.reset();
		this.setBaseSize();

		this.resetRotationCenter();
		this.resetSkewCenter();
		this.resetScaleCenter();
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		// TODO
		return false;
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		// TODO test this
		return this.convertLocalToSceneCoordinates(mX, mY);
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof RectangularShape) {
			// TODO
			return false;
		} else if(pOtherShape instanceof Line) {
			// TODO
			return false;
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void resetRotationCenter() {
		this.mRotationCenterX = mX;
		this.mRotationCenterY = mY;
	}

	public void resetScaleCenter() {
		// TODO test this
		this.mScaleCenterX = mX;
		this.mScaleCenterY = mY;
	}

	public void resetSkewCenter() {
		// TODO test this
		this.mSkewCenterX = mX;
		this.mSkewCenterY = mY;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
