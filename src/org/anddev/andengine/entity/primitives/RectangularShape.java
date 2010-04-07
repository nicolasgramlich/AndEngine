package org.anddev.andengine.entity.primitives;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.vertex.VertexBuffer;
import org.anddev.andengine.physics.collision.CollisionChecker;

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

	protected final float mBaseWidth;
	protected final float mBaseHeight;

	protected float mWidth;
	protected float mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangularShape(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBuffer pVertexBuffer) {
		super(pX, pY, pVertexBuffer);

		this.mBaseWidth = pWidth;
		this.mBaseHeight = pHeight;

		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getWidth() {
		return this.mWidth * this.getScale();
	}

	@Override
	public float getHeight() {
		return this.mHeight * this.getScale();
	}

	@Override
	public float getBaseWidth() {
		return this.mBaseWidth;
	}

	@Override
	public float getBaseHeight() {
		return this.mBaseHeight;
	}

	public void setWidth(final int pWidth) {
		this.mWidth = pWidth;
		this.onPositionChanged();
		this.updateVertexBuffer();
	}

	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.onPositionChanged();
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getCenterX() {
		return this.mX + this.getWidth() / 2;
	}

	@Override
	public float getCenterY() {
		return this.mY + this.getHeight() / 2;
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
		final float angle = this.getAngle();
		if(angle != 0) {
			final float halfWidth = this.getBaseWidth() / 2;
			final float halfHeight = this.getBaseHeight() / 2;

			pGL.glTranslatef(halfWidth, halfHeight, 0);
			pGL.glRotatef(angle, 0, 0, 1);
			pGL.glTranslatef(-halfWidth, -halfHeight, 0);
		}
	}

	@Override
	protected void applyScale(final GL10 pGL) {
		final float scale = this.getScale();
		if(scale != 1) {
			final float halfWidth = this.getBaseWidth() / 2;
			final float halfHeight = this.getBaseHeight() / 2;

			pGL.glTranslatef(halfWidth, halfHeight, 0);
			pGL.glScalef(scale, scale, 1);
			pGL.glTranslatef(-halfWidth, -halfHeight, 0);
		}
	}

	@Override
	protected void onPostTransformations(final GL10 pGL) {

	}

	@Override
	public void reset() {
		super.reset();
		this.setBaseSize();
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return pX >= this.mX
		&& pY >= this.mY
		&& pX <= this.mX + this.mWidth
		&& pY <= this.mY + this.mHeight;
	}

	@Override
	public boolean collidesWith(final Shape pOtherShape) {
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
