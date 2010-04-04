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

	public Shape(final float pX, final float pY, final VertexBuffer pVertexBuffer) {
		super(pX, pY);
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
	
	public abstract float getWidth();
	public abstract float getHeight();
	public abstract float getBaseWidth();
	public abstract float getBaseHeight();

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
		
		this.onApplyVertices(pGL);

		this.onPreTransformations(pGL);

		this.onApplyTransformations(pGL);

		this.onPostTransformations(pGL);

		this.drawVertices(pGL);
		pGL.glPopMatrix();
	}

	protected abstract void onPreTransformations(final GL10 pGL);
	protected abstract void applyOffset(final GL10 pGL);
	protected abstract void applyTranslation(final GL10 pGL);
	protected abstract void applyRotation(final GL10 pGL);
	protected abstract void applyScale(final GL10 pGL);
	protected abstract void drawVertices(final GL10 pGL);
	protected abstract void onPostTransformations(final GL10 pGL);

	@Override
	public void reset() {
		super.reset();
		this.mRed = 1.0f;
		this.mGreen = 1.0f;
		this.mBlue = 1.0f;
		this.mAlpha = 1.0f;
	}

	public abstract boolean contains(final float pX, final float pY);

	public abstract boolean collidesWith(final Shape pOtherShape);

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onInitDraw(final GL10 pGL) {
		GLHelper.setColor(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);
		GLHelper.enableVertexArray(pGL);
	}

	private void onApplyVertices(final GL10 pGL) {
		GLHelper.vertexPointer(pGL, this.getVertexBuffer().getByteBuffer(), GL10.GL_FLOAT);
	}

	protected void onApplyTransformations(final GL10 pGL) {
		this.applyOffset(pGL);

		this.applyTranslation(pGL);

		this.applyRotation(pGL);
		
		this.applyScale(pGL);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
