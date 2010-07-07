package org.anddev.andengine.entity.primitive;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.collision.CollisionChecker;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

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
	protected void onPreTransformations(final GL10 pGL) {

	}

	@Override
	protected void applyOffset(final GL10 pGL) {
		pGL.glTranslatef(this.mOffsetX, this.mOffsetY, 0);
	}

	@Override
	protected void applyTranslation(final GL10 pGL) {
		pGL.glTranslatef(this.mX, this.mY, 0);
	}

	@Override
	protected void drawVertices(final GL10 pGL) {
		pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	protected void applyRotation(final GL10 pGL) {
		// TODO Offset needs to be taken into account.
		final float rotation = this.mRotation;
		
		if(rotation != 0) {
			final float rotationCenterX = this.mRotationCenterX;
			final float rotationCenterY = this.mRotationCenterY;

			pGL.glTranslatef(rotationCenterX, rotationCenterY, 0);
			pGL.glRotatef(rotation, 0, 0, 1);
			pGL.glTranslatef(-rotationCenterX, -rotationCenterY, 0);
		}
	}

	@Override
	protected void applyScale(final GL10 pGL) {
		final float scaleX = this.mScaleX;
		final float scaleY = this.mScaleY;
		
		if(scaleX != 1 || scaleY != 1) {
			final float scaleCenterX = this.mScaleCenterX;
			final float scaleCenterY = this.mScaleCenterY;

			pGL.glTranslatef(scaleCenterX, scaleCenterY, 0);
			pGL.glScalef(scaleX, scaleY, 1);
			pGL.glTranslatef(-scaleCenterX, -scaleCenterY, 0);
		}
	}

	@Override
	protected void onPostTransformations(final GL10 pGL) {

	}

	@Override
	public void reset() {
		super.reset();
		this.setBaseSize();

		final float baseWidth = this.getBaseWidth();
		this.mRotationCenterX = baseWidth * 0.5f;
		final float baseHeight = this.getBaseHeight();
		this.mRotationCenterY = baseHeight * 0.5f;

		this.mScaleCenterX = baseWidth * 0.5f;
		this.mScaleCenterY = baseHeight * 0.5f;
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return pX >= this.mX
			&& pY >= this.mY
			&& pX <= this.mX + this.mWidth
			&& pY <= this.mY + this.mHeight;
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof RectangularShape) {
			final RectangularShape pOtherRectangularShape = (RectangularShape) pOtherShape;

			final float left = this.mX;
			final float top = this.mY;
			final float right = this.mWidth + left;
			final float bottom = this.mHeight + top;

			final float otherLeft = pOtherRectangularShape.mX;
			final float otherTop = pOtherRectangularShape.mY;
			final float otherRight = pOtherRectangularShape.mWidth + otherLeft;
			final float otherBottom = pOtherRectangularShape.mHeight + otherTop;

			return CollisionChecker.checkAxisAlignedBoxCollision(left, top, right, bottom, otherLeft, otherTop, otherRight, otherBottom);
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
