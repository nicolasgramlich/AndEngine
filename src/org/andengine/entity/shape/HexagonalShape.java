package org.andengine.entity.shape;

import org.andengine.collision.HexagonalShapeCollisionChecker;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.shader.ShaderProgram;

/**
 * 
 * @author Stefan Hagdahl
 *
 */
public abstract class HexagonalShape extends Shape implements IAreaShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mBaseSide;

	protected float mSide;
	protected float mWidth;
	protected float mHeight;
	protected float mR;
	protected float mH;
	
	

	// ===========================================================
	// Constructors
	// ===========================================================

	public HexagonalShape(final float pX, final float pY, final float pBaseSide, final ShaderProgram pShaderProgram) {
		super(pX, pY, pShaderProgram);

		this.mBaseSide = pBaseSide;
		
		this.mSide = pBaseSide;
		this.mR = calculateR(pBaseSide);
		this.mH = calculateH(pBaseSide);
		this.mWidth = this.mR * 2;
		this.mHeight = this.mH * 2 + pBaseSide;

		this.resetRotationCenter();
		this.resetScaleCenter();
		this.resetSkewCenter();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getWidth() {
		return this.mWidth;
	}

	@Override
	public float getHeight() {
		return this.mHeight;
	}
	
	public float getSide() {
		return this.mSide;
	}
	
	public float getR() {
		return this.mR;
	}
	
	public float getH() {
		return this.mH;
	}
	
	public float getBaseSide() {
		return this.mBaseSide;
	}
	
	@Override
	public float getBaseWidth() {
		float baseR = calculateR(mBaseSide);
		float baseWidth = baseR * 2;
		return baseWidth;
	}

	@Override
	public float getBaseHeight() {
		float baseH = calculateH(mBaseSide);
		float baseHeight = baseH * 2 + mBaseSide;
		return baseHeight;
	}

	@Override
	public float getWidthScaled() {
		return this.getWidth() * this.mScaleX;
	}

	@Override
	public float getHeightScaled() {
		return this.getHeight() * this.mScaleY;
	}

	@Override
	public void setHeight(float pHeight) {
		
	}

	@Override
	public void setWidth(float pWidth) {
		
	}

	@Override
	public void setSize(float pWidth, float pHeight) {
		
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void setBaseSize() {
		if(this.mSide != this.mBaseSide) {
			this.mSide = this.mBaseSide;
			this.mR = calculateR(this.mSide);
			this.mH = calculateH(this.mSide);
			this.mHeight = this.mH * 2 + this.mSide;
			this.mWidth = this.mR * 2;
			this.onUpdateVertices();
		}
	}

	@Override
	public boolean isCulled(final Camera pCamera) {
		return !HexagonalShapeCollisionChecker.isVisible(pCamera, this);
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
		return HexagonalShapeCollisionChecker.checkContains(this, pX, pY);
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return this.convertLocalToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f);
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof HexagonalShape) {
			return HexagonalShapeCollisionChecker.checkCollision(this, (HexagonalShape) pOtherShape);
		} else if(pOtherShape instanceof Line) {
			return HexagonalShapeCollisionChecker.checkCollision(this, (Line) pOtherShape);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void resetRotationCenter() {
		this.mRotationCenterX = mWidth * 0.5f;
		this.mRotationCenterY = mHeight * 0.5f;
	}

	public void resetScaleCenter() {
		this.mRotationCenterX = mWidth * 0.5f;
		this.mRotationCenterY = mHeight * 0.5f;
	}

	public void resetSkewCenter() {
		this.mRotationCenterX = mWidth * 0.5f;
		this.mRotationCenterY = mHeight * 0.5f;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	protected float calculateH(float pSide) {
		return (float)Math.sin(30 * Math.PI / 180) * pSide;
	}
	
	protected float calculateR(float pSide) {
		return (float)Math.cos(30 * Math.PI / 180) * pSide;
	}
}
