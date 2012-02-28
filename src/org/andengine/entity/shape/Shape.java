package org.andengine.entity.shape;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
	public int getSourceBlendFunction() {
		return this.mSourceBlendFunction;
	}

	@Override
	public int getDestinationBlendFunction() {
		return this.mDestinationBlendFunction;
	}

	@Override
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	@Override
	public ShaderProgram getShaderProgram() {
		return this.mShaderProgram;
	}

	@Override
	public void setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mShaderProgram = pShaderProgram;
	}

	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return getVertexBufferObject().getVertexBufferObjectManager();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onUpdateVertices();

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		if(this.mBlendingEnabled) {
			pGLState.enableBlend();
			pGLState.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
		}
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		if(this.mBlendingEnabled) {
			pGLState.disableBlend();
		}
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return false;
	}

	@Override
	public void reset() {
		super.reset();

		this.mSourceBlendFunction = IShape.BLENDFUNCTION_SOURCE_DEFAULT;
		this.mDestinationBlendFunction = IShape.BLENDFUNCTION_DESTINATION_DEFAULT;
	}

	@Override
	public void dispose() {
		super.dispose();

		final IVertexBufferObject vertexBufferObject = this.getVertexBufferObject();
		if(vertexBufferObject != null && vertexBufferObject.isAutoDispose() && !vertexBufferObject.isDisposed()) {
			vertexBufferObject.dispose();
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
		if(pTextureOptions.mPreMultiplyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
