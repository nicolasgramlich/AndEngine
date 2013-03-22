package org.andengine.entity.shape;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
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

	protected int mBlendFunctionSource = IShape.BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mBlendFunctionDestination = IShape.BLENDFUNCTION_DESTINATION_DEFAULT;

	protected boolean mBlendingEnabled;

	protected ShaderProgram mShaderProgram;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Shape(final float pX, final float pY, final ShaderProgram pShaderProgram) {
		super(pX, pY);

		this.mShaderProgram = pShaderProgram;
	}

	public Shape(final float pX, final float pY, final float pWidth, final float pHeight, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight);

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
	public int getBlendFunctionSource() {
		return this.mBlendFunctionSource;
	}

	@Override
	public void setBlendFunctionSource(final int pBlendFunctionSource) {
		this.mBlendFunctionSource = pBlendFunctionSource;
	}

	@Override
	public int getBlendFunctionDestination() {
		return this.mBlendFunctionDestination;
	}

	@Override
	public void setBlendFunctionDestination(final int pBlendFunctionDestination) {
		this.mBlendFunctionDestination = pBlendFunctionDestination;
	}

	@Override
	public void setBlendFunction(final int pBlendFunctionSource, final int pBlendFunctionDestination) {
		this.mBlendFunctionSource = pBlendFunctionSource;
		this.mBlendFunctionDestination = pBlendFunctionDestination;
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
		return this.getVertexBufferObject().getVertexBufferObjectManager();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onUpdateVertices();

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		if (this.mBlendingEnabled) {
			pGLState.enableBlend();
			pGLState.blendFunction(this.mBlendFunctionSource, this.mBlendFunctionDestination);
		}
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		if (this.mBlendingEnabled) {
			pGLState.disableBlend();
		}
	}

	@Override
	public void setWidth(final float pWidth) {
		super.setWidth(pWidth);

		this.onUpdateVertices();
	}

	@Override
	public void setHeight(final float pHeight) {
		super.setHeight(pHeight);

		this.onUpdateVertices();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		super.setSize(pWidth, pHeight);

		this.onUpdateVertices();
	}

	@Override
	public void reset() {
		super.reset();

		this.mBlendFunctionSource = IShape.BLENDFUNCTION_SOURCE_DEFAULT;
		this.mBlendFunctionDestination = IShape.BLENDFUNCTION_DESTINATION_DEFAULT;
	}

	@Override
	public void dispose() {
		super.dispose();

		final IVertexBufferObject vertexBufferObject = this.getVertexBufferObject();
		if ((vertexBufferObject != null) && vertexBufferObject.isAutoDispose() && !vertexBufferObject.isDisposed()) {
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
		if (pTextureOptions.mPreMultiplyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
