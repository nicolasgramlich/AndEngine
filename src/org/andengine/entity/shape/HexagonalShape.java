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
	protected float mBaseWidth;
	protected float mBaseHeight;

	protected float mSide;
	protected float mWidth;
	protected float mHeight;
	protected float mR;
	protected float mH;
	
	

	// ===========================================================
	// Constructors
	// ===========================================================

	public HexagonalShape(final float pX, final float pY, final float pBaseSide, final float pBaseWidth, final float pBaseHeight, final ShaderProgram pShaderProgram) {
		super(pX, pY, pShaderProgram);

		this.mBaseSide = pBaseSide;
		this.mBaseWidth = pBaseWidth;
		this.mBaseHeight = pBaseHeight;
		
		this.mSide = pBaseSide;
		this.mWidth = pBaseWidth;
		this.mHeight = pBaseHeight;

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

	@Override
	public float getBaseWidth() {
		return this.mBaseWidth;
	}

	@Override
	public float getBaseHeight() {
		return this.mBaseHeight;
	}
	
	public float getBaseSide() {
		return this.mBaseSide;
	}

	@Override
	public void setWidth(final float pWidth) {
		this.mWidth = pWidth;
		this.onUpdateVertices();
	}

	@Override
	public void setHeight(final float pHeight) {
		this.mHeight = pHeight;
		this.onUpdateVertices();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.onUpdateVertices();
	}

	@Override
	public float getWidthScaled() {
		return this.getWidth() * this.mScaleX;
	}

	@Override
	public float getHeightScaled() {
		return this.getHeight() * this.mScaleY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void setBaseSize() {
		if(this.mWidth != this.mBaseWidth || this.mHeight != this.mBaseHeight) {
			this.mWidth = this.mBaseWidth;
			this.mHeight = this.mBaseHeight;
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
		this.mH = calculateH(mSide);
		this.mR = calculateR(mSide);
		float totalWidth = mR * 2;
		float totalHeight = mSide + mH * 2;
		this.mRotationCenterX = totalWidth * 0.5f;
		this.mRotationCenterY = totalHeight * 0.5f;
	}

	public void resetScaleCenter() {
		this.mH = calculateH(mSide);
		this.mR = calculateR(mSide);
		float totalWidth = mR * 2;
		float totalHeight = mSide + mH * 2;
		this.mRotationCenterX = totalWidth * 0.5f;
		this.mRotationCenterY = totalHeight * 0.5f;
	}

	public void resetSkewCenter() {
		this.mH = calculateH(mSide);
		this.mR = calculateR(mSide);
		float totalWidth = mR * 2;
		float totalHeight = mSide + mH * 2;
		this.mRotationCenterX = totalWidth * 0.5f;
		this.mRotationCenterY = totalHeight * 0.5f;
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
