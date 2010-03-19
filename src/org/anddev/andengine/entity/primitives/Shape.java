package org.anddev.andengine.entity.primitives;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.DynamicEntity;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:51:27 - 13.03.2010
 */
public abstract class Shape extends DynamicEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final VertexBuffer mVertexBuffer;

	private float mRed = 1;
	private float mGreen = 1;
	private float mBlue = 1;
	private float mAlpha = 1f;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Shape(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBuffer pVertexBuffer) {
		super(pX, pY, pWidth, pHeight);
		this.mVertexBuffer = pVertexBuffer;
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getRed() {
		return this.mRed;
	}

	public float getGreen() {
		return this.mGreen;
	}

	public float getBlue() {
		return this.mBlue;
	}

	public float getAlpha() {
		return this.mAlpha;
	}

	public VertexBuffer getVertexBuffer() {
		return this.mVertexBuffer;
	}

	public void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;
	}

	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onPositionChanged() {
		this.updateVertexBuffer();
	}

	protected abstract void updateVertexBuffer();

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		this.onInitDraw(pGL);

		pGL.glPushMatrix();
		GLHelper.vertexPointer(pGL, this.getVertexBuffer().getByteBuffer(), GL10.GL_FLOAT);

		this.onPreTransformations(pGL);

		this.onApplyTransformations(pGL);

		this.onPostTransformations(pGL);

		pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		pGL.glPopMatrix();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void reset() {
		super.reset();
		this.mRed = 1.0f;
		this.mGreen = 1.0f;
		this.mBlue = 1.0f;
		this.mAlpha = 1.0f;
	}

	protected void onInitDraw(final GL10 pGL) {
		GLHelper.color4f(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);
		GLHelper.enableVertexArray(pGL);
	}

	protected void onPreTransformations(final GL10 pGL) {

	}

	protected void onApplyTransformations(final GL10 pGL) {
		/* Translate */
		this.applyTranslation(pGL);

		/* Rotate */
		this.applyRotation(pGL);

		/* Scale */
		this.applyScale(pGL);
	}

	protected void applyTranslation(final GL10 pGL) {
		pGL.glTranslatef(this.getX(), this.getY(), 0);
	}

	protected void applyRotation(final GL10 pGL) {
		// TODO Offset needs to be taken into account.
		final float rotationAngleClockwise = this.getRotationAngleClockwise();
		if(rotationAngleClockwise != 0) {
			final float halfWidth = this.getWidth() / 2;
			final float halfHeight = this.getHeight() / 2;

			pGL.glTranslatef(halfWidth, halfHeight, 0);
			pGL.glRotatef(rotationAngleClockwise, 0, 0, 1);
			pGL.glTranslatef(-halfWidth, -halfHeight / 2, 0);
		}
	}

	protected void applyScale(final GL10 pGL) {
		final float scale = this.getScale();
		if(scale != 1) {
			pGL.glScalef(scale, scale, 1);
		}
	}

	protected void onPostTransformations(final GL10 pGL) {

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
