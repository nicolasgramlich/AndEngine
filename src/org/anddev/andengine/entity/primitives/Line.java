package org.anddev.andengine.entity.primitives;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.collision.CollisionChecker;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.vertex.LineVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 09:50:36 - 04.04.2010
 */
public class Line extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mX2;
	private float mY2;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Line(final float pX1, final float pY1, final float pX2, final float pY2) {
		super(pX1, pY1, new LineVertexBuffer(GL11.GL_DYNAMIC_DRAW));
		this.mX2 = pX2;
		this.mY2 = pY2;
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getBaseHeight() {
		return this.mY2 - this.mY;
	}

	@Override
	public float getBaseWidth() {
		return this.mX2 - this.mX;
	}

	@Override
	public float getHeight() {
		return (this.mY2 - this.mY);
	}

	@Override
	public float getWidth() {
		return (this.mX2 - this.mX);
	}


	/** Instead use {@link Line#setPosition(float, float, float, float)}.
	 * @see org.anddev.andengine.entity.DynamicEntity#setPosition(float, float)
	 */
	@Deprecated
	@Override
	public void setPosition(final float pX, final float pY) {
		final float dX = this.mX - pX;
		final float dY = this.mY - pY;

		super.setPosition(pX, pY);

		this.mX2 += dX;
		this.mY2 += dY;
	}

	public void setPosition(final float pX1, final float pY1, final float pX2, final float pY2) {
		this.mX2 = pX2;
		this.mY2 = pY2;

		super.setPosition(pX1, pY1);

		this.updateVertexBuffer();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getCenterX() {
		return (this.mX + this.mX2) / 2;
	}

	@Override
	public float getCenterY() {
		return (this.mY + this.mY2) / 2;
	}

	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.disableTextures(pGL);
		GLHelper.disableTexCoordArray(pGL);
	}

	@Override
	public LineVertexBuffer getVertexBuffer() {
		return (LineVertexBuffer)super.getVertexBuffer();
	}

	@Override
	protected void onUpdateVertexBuffer() {
		this.getVertexBuffer().update(0, 0, this.mX2 - this.mX, this.mY2 - this.mY);
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
		pGL.glDrawArrays(GL10.GL_LINES, 0, 2);
	}

	@Override
	protected void applyRotation(final GL10 pGL) {
		// TODO Offset needs to be taken into account.
		final float angle = this.mAngle;
		if(angle != 0) {
			final float halfDeltaX = this.getBaseWidth() / 2;
			final float halfDeltaY = this.getBaseHeight() / 2;

			pGL.glTranslatef(halfDeltaX, halfDeltaY, 0);
			pGL.glRotatef(angle, 0, 0, 1);
			pGL.glTranslatef(-halfDeltaX, -halfDeltaY, 0);
		}
	}

	@Override
	protected void applyScale(final GL10 pGL) {
		final float scale = this.mScale;
		if(scale != 1) {
			final float halfDeltaX = this.getBaseWidth() / 2;
			final float halfDeltaY = this.getBaseHeight() / 2;

			pGL.glTranslatef(halfDeltaX, halfDeltaY, 0);
			pGL.glScalef(scale, scale, 1);
			pGL.glTranslatef(-halfDeltaX, -halfDeltaY, 0);
		}
	}

	@Override
	protected void onPostTransformations(final GL10 pGL) {

	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return false;
	}

	@Override
	public boolean collidesWith(final Shape pOtherShape) {
		if(pOtherShape instanceof Line) {
			final Line otherLine = (Line)pOtherShape;
			return CollisionChecker.checkLineCollision(this.mX, this.mY, this.mX2, this.mY2, otherLine.mX, otherLine.mY, otherLine.mX2, otherLine.mY2);
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
