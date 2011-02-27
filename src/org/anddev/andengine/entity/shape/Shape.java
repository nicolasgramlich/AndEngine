package org.anddev.andengine.entity.shape;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:51:27 - 13.03.2010
 */
public abstract class Shape extends Entity implements IShape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_SRC_ALPHA;
	public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	public static final int BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GL10.GL_ONE;
	public static final int BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;

	private boolean mCullingEnabled = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Shape(final float pX, final float pY) {
		super(pX, pY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	@Override
	public boolean isCullingEnabled() {
		return this.mCullingEnabled;
	}

	@Override
	public void setCullingEnabled(final boolean pCullingEnabled) {
		this.mCullingEnabled = pCullingEnabled;
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

	protected abstract void onUpdateVertexBuffer();
	protected abstract VertexBuffer getVertexBuffer();

	protected abstract void drawVertices(final GL10 pGL, final Camera pCamera);

	@Override
	protected void doDraw(final GL10 pGL, final Camera pCamera) {
		this.onInitDraw(pGL);
		this.onApplyVertices(pGL);
		this.drawVertices(pGL, pCamera);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return false;
	}

	/**
	 * Will only be performed if {@link Shape#isCullingEnabled()} is true.
	 * @param pCamera
	 * @return <code>true</code> when this object is visible by the {@link Camera}, <code>false</code> otherwise.
	 */
	protected abstract boolean isCulled(final Camera pCamera);

	@Override
	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		if(this.mCullingEnabled == false || this.isCulled(pCamera) == false) {
			super.onManagedDraw(pGL, pCamera);
		}
	}

	@Override
	public void reset() {
		super.reset();
		this.mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
		this.mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onInitDraw(final GL10 pGL) {
		GLHelper.setColor(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);

		GLHelper.enableVertexArray(pGL);
		GLHelper.blendFunction(pGL, this.mSourceBlendFunction, this.mDestinationBlendFunction);
	}

	protected void onApplyVertices(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.getVertexBuffer().selectOnHardware(gl11);
			GLHelper.vertexZeroPointer(gl11);
		} else {
			GLHelper.vertexPointer(pGL, this.getVertexBuffer().getFloatBuffer());
		}
	}

	protected void updateVertexBuffer() {
		this.onUpdateVertexBuffer();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
