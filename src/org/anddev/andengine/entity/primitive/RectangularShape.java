package org.anddev.andengine.entity.primitive;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.collision.CollisionChecker;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.vertex.VertexBuffer;
import org.anddev.andengine.util.MathUtils;

/**
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

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangularShape(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBuffer pVertexBuffer) {
		super(pX, pY, pVertexBuffer);

		this.mBaseWidth = pWidth;
		this.mBaseHeight = pHeight;
		
		this.mRotationCenterX = pWidth * 0.5f;
		this.mRotationCenterY = pHeight * 0.5f;
			
		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;

		this.mWidth = pWidth;
		this.mHeight = pHeight;
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
		this.onPositionChanged();
		this.updateVertexBuffer();
	}

	public void setHeight(final float pHeight) {
		this.mHeight = pHeight;
		this.onPositionChanged();
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getCenterX() {
		return this.mX + this.getWidth() * 0.5f;
	}

	@Override
	public float getCenterY() {
		return this.mY + this.getHeight() * 0.5f;
	}

	public void setBaseSize() {
		if(this.mWidth != this.mBaseWidth && this.mHeight != this.mBaseHeight) {
			this.mWidth = this.mBaseWidth;
			this.mHeight = this.mBaseHeight;
			this.onPositionChanged();
			this.updateVertexBuffer();
		}
	}

	@Override
	protected void drawVertices(final GL10 pGL) {
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
		final float left = this.mX;
		final float top = this.mY;
		final float right = this.mWidth + left;
		final float bottom = this.mHeight + top;

		final float[] vertex1 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, left, top, left + this.mRotationCenterX, top + this.mRotationCenterY, 		this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);
		final float[] vertex2 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, right, top, left + this.mRotationCenterX, top + this.mRotationCenterY, 	this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);
		final float[] vertex3 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, right, bottom, left + this.mRotationCenterX, top + this.mRotationCenterY,	this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);
		final float[] vertex4 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, left, bottom, left + this.mRotationCenterX, top + this.mRotationCenterY,	this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);

		return CollisionChecker.checkBoxContains(vertex1, vertex2, vertex3, vertex4, pX, pY); 
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof RectangularShape) {
			final RectangularShape pOtherRectangularShape = (RectangularShape) pOtherShape;
			
			final float left = this.mX;
			final float top = this.mY;
			final float right = this.mWidth + left;
			final float bottom = this.mHeight + top;

			final float[] vertex1 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, left, top, left + this.mRotationCenterX, top + this.mRotationCenterY, 		this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);
			final float[] vertex2 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, right, top, left + this.mRotationCenterX, top + this.mRotationCenterY, 	this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);
			final float[] vertex3 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, right, bottom, left + this.mRotationCenterX, top + this.mRotationCenterY,	this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);
			final float[] vertex4 = MathUtils.rotateAndScaleAroundCenter(this.mRotation, left, bottom, left + this.mRotationCenterX, top + this.mRotationCenterY,	this.mScaleX, this.mScaleY, left + this.mScaleCenterX, top + this.mScaleCenterY, new float[2]);


			final float otherLeft = pOtherRectangularShape.mX;
			final float otherTop = pOtherRectangularShape.mY;
			final float otherRight = pOtherRectangularShape.mWidth + otherLeft;
			final float otherBottom = pOtherRectangularShape.mHeight + otherTop;
			

			final float[] otherVertex1 = MathUtils.rotateAndScaleAroundCenter(pOtherRectangularShape.mRotation, otherLeft, otherTop, otherLeft + pOtherRectangularShape.mRotationCenterX, otherTop + pOtherRectangularShape.mRotationCenterY,		pOtherRectangularShape.mScaleX, pOtherRectangularShape.mScaleY, otherLeft + pOtherRectangularShape.mScaleCenterX, otherTop + pOtherRectangularShape.mScaleCenterY, new float[2]);
			final float[] otherVertex2 = MathUtils.rotateAndScaleAroundCenter(pOtherRectangularShape.mRotation, otherRight, otherTop, otherLeft + pOtherRectangularShape.mRotationCenterX, otherTop + pOtherRectangularShape.mRotationCenterY,		pOtherRectangularShape.mScaleX, pOtherRectangularShape.mScaleY, otherLeft + pOtherRectangularShape.mScaleCenterX, otherTop + pOtherRectangularShape.mScaleCenterY, new float[2]);
			final float[] otherVertex3 = MathUtils.rotateAndScaleAroundCenter(pOtherRectangularShape.mRotation, otherRight, otherBottom, otherLeft + pOtherRectangularShape.mRotationCenterX, otherTop + pOtherRectangularShape.mRotationCenterY,	pOtherRectangularShape.mScaleX, pOtherRectangularShape.mScaleY, otherLeft + pOtherRectangularShape.mScaleCenterX, otherTop + pOtherRectangularShape.mScaleCenterY, new float[2]);
			final float[] otherVertex4 = MathUtils.rotateAndScaleAroundCenter(pOtherRectangularShape.mRotation, otherLeft, otherBottom, otherLeft + pOtherRectangularShape.mRotationCenterX, otherTop + pOtherRectangularShape.mRotationCenterY,	pOtherRectangularShape.mScaleX, pOtherRectangularShape.mScaleY, otherLeft + pOtherRectangularShape.mScaleCenterX, otherTop + pOtherRectangularShape.mScaleCenterY, new float[2]);

			return CollisionChecker.checkBoxCollision(vertex1, vertex2, vertex3, vertex4, otherVertex1, otherVertex2, otherVertex3, otherVertex4);
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
