package org.anddev.andengine.entity.sprite.batch;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttribute;
import org.anddev.andengine.util.constants.DataConstants;

import android.opengl.GLES20;

/**
 * TODO Might extend Shape
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:45:48 - 14.06.2011
 */
public class SpriteBatch extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITexture mTexture;
	protected final int mCapacity;

	protected int mIndex;
	private int mVertices;

	private int mSourceBlendFunction;
	private int mDestinationBlendFunction;

	private final SpriteBatchMesh mSpriteBatchMesh;
	protected ShaderProgram mShaderProgram;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatch(final ITexture pTexture, final int pCapacity) {
		this.mTexture = pTexture;
		this.mCapacity = pCapacity;
		
		this.mSpriteBatchMesh = new SpriteBatch(pCapacity, GLES20.GL_STATIC_DRAW, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT); // TODO Measure: GLES20.GL_STATIC_DRAW against GLES20.GL_STREAM_DRAW and GLES20.GL_DYNAMIC_DRAW 

		this.initBlendFunction();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	public int getIndex() {
		return this.mIndex;
	}

	public ShaderProgram getShaderProgram() {
		return this.mShaderProgram;
	}

	public SpriteBatch setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mShaderProgram = pShaderProgram;
		return this;
	}
	
	public SpriteBatch setDefaultShaderProgram() {
		return this.setShaderProgram(new ShaderProgram(Sprite.SHADERPROGRAM_VERTEXSHADER_DEFAULT, Sprite.SHADERPROGRAM_FRAGMENTSHADER_DEFAULT) {
			@Override
			public void bind() {
				super.bind();

				this.setUniform(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX, GLHelper.getModelViewProjectionMatrix());
				this.setTexture(ShaderProgramConstants.UNIFORM_TEXTURE_0, 0);
				this.setUniform(ShaderProgramConstants.UNIFORM_COLOR, SpriteBatch.this.mRed, SpriteBatch.this.mGreen, SpriteBatch.this.mBlue, SpriteBatch.this.mAlpha);
			}
		});
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	protected void preDraw(Camera pCamera) {
		super.preDraw(pCamera);
		
		GLHelper.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);

		GLHelper.enableTextures();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		this.mTexture.bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.begin();
		
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLES, mVertices);

		this.end();
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		GLHelper.enableTextures();

		super.postDraw(pCamera);
	}
	
	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if(this.mMesh.getVertexBufferObject().isManaged()) {
			this.mMesh.getVertexBufferObject().unloadFromActiveBufferObjectManager();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void begin() {
//		GLHelper.disableDepthMask(pGL);
	}

	protected void end() {
//		GLHelper.enableDepthMask(pGL);
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	/**
	 * {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.addInner(pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this.mSpriteBatchVertexBuffer.addInner(pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
	}

	/**
	 * {@link SpriteBatchVertexBuffer#add(Sprite)}.
	 */
	public void draw(final Sprite pSprite) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.getRotation() == 0 && !pSprite.isScaled()) {
				this.mSpriteBatchVertexBuffer.add(pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
			} else {
				this.mSpriteBatchVertexBuffer.add(pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation());
			}

			this.mSpriteBatchTextureRegionBuffer.add(textureRegion);

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.getRotation() == 0 && !pSprite.isScaled()) {
				this.mSpriteBatchVertexBuffer.add(pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
			} else {
				this.mSpriteBatchVertexBuffer.add(pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation());
			}

			this.mSpriteBatchTextureRegionBuffer.add(textureRegion);

			this.mIndex++;
		}
	}

	public void submit() {
		this.onSubmit();
	}

	private void onSubmit() {
		this.mVertices = this.mIndex * SpriteBatchVertexBuffer.VERTICES_PER_RECTANGLE;

		this.mSpriteBatchVertexBuffer.submit();
		this.mSpriteBatchTextureRegionBuffer.submit();

		this.mIndex = 0;
		this.mSpriteBatchVertexBuffer.setIndex(0);
		this.mSpriteBatchTextureRegionBuffer.setIndex(0);
	}

	private void initBlendFunction() {
		if(this.mTexture.getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(Shape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, Shape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	private void assertCapacity(final int pIndex) {
		if(pIndex >= this.mCapacity) {
			throw new IllegalStateException("This supplied pIndex: '" + pIndex + "' is exceeding the capacity: '" + this.mCapacity + "' of this SpriteBatch!");
		}
	}

	private void assertCapacity() {
		if(this.mIndex == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}
	}

	protected void assertTexture(final ITextureRegion pTextureRegion) {
		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException("The supplied Texture does match the Texture of this SpriteBatch!");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
