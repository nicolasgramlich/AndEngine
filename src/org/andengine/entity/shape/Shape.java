package org.anddev.andengine.entity.shape;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.opengl.vbo.IVertexBufferObject;

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

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mSourceBlendFunction = IShape.BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mDestinationBlendFunction = IShape.BLENDFUNCTION_DESTINATION_DEFAULT;

	protected boolean mBlendingEnabled = false;
	protected boolean mCullingEnabled = false;

	protected ShaderProgram mShaderProgram;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Shape(final float pX, final float pY, final ShaderProgram pShaderProgram) {
		super(pX, pY);

		this.mShaderProgram = pShaderProgram;
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

	@Override
	public ShaderProgram getShaderProgram() {
		return this.mShaderProgram;
	}

	@Override
	public void setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mShaderProgram = pShaderProgram;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onUpdateVertices();

	@Override
	protected void preDraw(final Camera pCamera) {
		if(this.mBlendingEnabled) {
			GLState.enableBlend();
			GLState.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
		}
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		if(this.mBlendingEnabled) {
			GLState.disableBlend();
		}
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

		this.mSourceBlendFunction = IShape.BLENDFUNCTION_SOURCE_DEFAULT;
		this.mDestinationBlendFunction = IShape.BLENDFUNCTION_DESTINATION_DEFAULT;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		final IVertexBufferObject vertexBufferObject = this.getVertexBufferObject();
		if(vertexBufferObject != null) {
			if(vertexBufferObject.isManaged()) {
				vertexBufferObject.unloadFromActiveBufferObjectManager();
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void initBlendFunction(final ITextureRegion pTextureRegion) {
		this.initBlendFunction(pTextureRegion.getTexture());
	}

	protected void initBlendFunction(final ITexture pTexture) {
		this.initBlendFunction(pTexture.getTextureOptions());
	}

	protected void initBlendFunction(final TextureOptions pTextureOptions) {
		if(pTextureOptions.mPreMultipyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
