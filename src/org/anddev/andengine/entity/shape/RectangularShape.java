package org.anddev.andengine.entity.shape;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:37:50 - 04.04.2010
 */
public abstract class RectangularShape extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mBaseWidth;
	protected float mBaseHeight;

	protected float mWidth;
	protected float mHeight;

	protected final VertexBuffer mVertexBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangularShape(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBuffer pVertexBuffer) {
		super(pX, pY);

		this.mBaseWidth = pWidth;
		this.mBaseHeight = pHeight;

		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.mVertexBuffer = pVertexBuffer;

		this.mRotationCenterX = pWidth * 0.5f;
		this.mRotationCenterY = pHeight * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public VertexBuffer getVertexBuffer() {
		return this.mVertexBuffer;
	}

	@Override
	public float getWidth() {
		return this.mWidth;
	}

	@Override
	public float getHeight() {
		return this.mHeight;
	}

	@Override
	public float getBaseWidth() {
		return this.mBaseWidth;
	}

	@Override
	public float getBaseHeight() {
		return this.mBaseHeight;
	}

	public void setWidth(final float pWidth) {
		this.mWidth = pWidth;
		this.updateVertexBuffer();
	}

	public void setHeight(final float pHeight) {
		this.mHeight = pHeight;
		this.updateVertexBuffer();
	}

	public void setSize(final float pWidth, final float pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void setBaseSize() {
		if(this.mWidth != this.mBaseWidth || this.mHeight != this.mBaseHeight) {
			this.mWidth = this.mBaseWidth;
			this.mHeight = this.mBaseHeight;
			this.updateVertexBuffer();
		}
	}

	@Override
	protected boolean isCulled(final Camera pCamera) { // TODO Advanced culling!
		final float x = this.mX;
		final float y = this.mY;
		return x > pCamera.getMaxX()
			|| y > pCamera.getMaxY()
			|| x + this.getWidth() < pCamera.getMinX()
			|| y + this.getHeight() < pCamera.getMinY();
	}

	@Override
	protected void drawVertices(final GL10 pGL, final Camera pCamera) {
		pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void reset() {
		super.reset();
		this.setBaseSize();

		final float baseWidth = this.getBaseWidth();
		final float baseHeight = this.getBaseHeight();

		this.mRotationCenterX = baseWidth * 0.5f;
		this.mRotationCenterY = baseHeight * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return RectangularShapeCollisionChecker.checkContains(this, pX, pY);
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return this.convertLocalToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f);
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof RectangularShape) {
			final RectangularShape pOtherRectangularShape = (RectangularShape) pOtherShape;
			return RectangularShapeCollisionChecker.checkCollision(this, pOtherRectangularShape);
		} else if(pOtherShape instanceof Line) {
			final Line line = (Line) pOtherShape;
			return RectangularShapeCollisionChecker.checkCollision(this, line);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
