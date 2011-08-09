package org.anddev.andengine.entity.shape;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:51:27 - 13.03.2010
 */
public abstract class Shape extends Entity implements IShape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BLENDFUNCTION_SOURCE_DEFAULT = GLES20.GL_SRC_ALPHA;
	public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	public static final int BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE;
	public static final int BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mSourceBlendFunction = Shape.BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mDestinationBlendFunction = Shape.BLENDFUNCTION_DESTINATION_DEFAULT;

	protected boolean mBlendingEnabled = false;
	protected boolean mCullingEnabled = false;

	protected Mesh mMesh;
	protected ShaderProgram mShaderProgram;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Shape(final float pX, final float pY, final Mesh pMesh) {
		super(pX, pY);

		this.mMesh = pMesh;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public boolean isBlendingEnabled() {
		return this.mBlendingEnabled;
	}

	@Override
	public void setBlendingEnabled(final boolean pBlendingEnabled) {
		this.mBlendingEnabled = pBlendingEnabled;
	}

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

	public Mesh getMesh() {
		return this.mMesh;
	}

	public ShaderProgram getShaderProgram() {
		return this.mShaderProgram;
	}

	public IShape setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mShaderProgram = pShaderProgram;
		return this;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onUpdateVertices();

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		if(this.mBlendingEnabled) {
			GLHelper.enableBlend();
			GLHelper.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
		}
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		if(this.mBlendingEnabled) {
			GLHelper.disableBlend();
		}

		super.postDraw(pCamera);
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
	protected void onManagedDraw(final Camera pCamera) {
		if(!this.mCullingEnabled || !this.isCulled(pCamera)) {
			super.onManagedDraw(pCamera);
		}
	}

	@Override
	public void reset() {
		super.reset();
		this.mSourceBlendFunction = Shape.BLENDFUNCTION_SOURCE_DEFAULT;
		this.mDestinationBlendFunction = Shape.BLENDFUNCTION_DESTINATION_DEFAULT;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		if(vertexBufferObject.isManaged()) {
			vertexBufferObject.unloadFromActiveBufferObjectManager();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
