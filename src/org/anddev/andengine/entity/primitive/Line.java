package org.anddev.andengine.entity.primitive;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.collision.CollisionChecker;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.LineVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 09:50:36 - 04.04.2010
 */
public class Line extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float LINEWIDTH_DEFAULT = 1.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	private float mX2;
	private float mY2;
	
	private float mLineWidth;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Line(final float pX1, final float pY1, final float pX2, final float pY2) {
		this(pX1, pY1, pX2, pY2, LINEWIDTH_DEFAULT);
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth) {
		super(pX1, pY1, new LineVertexBuffer(GL11.GL_STATIC_DRAW));
		
		this.mX2 = pX2;
		this.mY2 = pY2;
		
		this.mLineWidth = pLineWidth;
		
		this.updateVertexBuffer();

		final float width = this.getWidth();
		final float height = this.getHeight();
		
		this.mRotationCenterX = width * 0.5f;
		this.mRotationCenterY = height * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getLineWidth() {
		return this.mLineWidth;
	}

	public void setLineWidth(final float pLineWidth) {
		this.mLineWidth = pLineWidth;
	}

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
		return (this.mX + this.mX2) * 0.5f;
	}

	@Override
	public float getCenterY() {
		return (this.mY + this.mY2) * 0.5f;
	}

	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.disableTextures(pGL);
		GLHelper.disableTexCoordArray(pGL);
		GLHelper.lineWidth(pGL, this.mLineWidth);
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
		final float rotation = this.mRotation;
		
		if(rotation != 0) {
			final float rotationCenterX = this.getBaseWidth() * 0.5f;
			final float rotationCenterY = this.getBaseHeight() * 0.5f;

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
			final float scaleCenterX = this.getBaseWidth() * 0.5f;
			final float scaleCenterY = this.getBaseHeight() * 0.5f;

			pGL.glTranslatef(scaleCenterX, scaleCenterY, 0);
			pGL.glScalef(scaleX, scaleY, 1);
			pGL.glTranslatef(-scaleCenterX, -scaleCenterY, 0);
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
	public boolean collidesWith(final IShape pOtherShape) {
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
