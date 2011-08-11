package org.anddev.andengine.entity.sprite.batch;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.DefaultShaderPrograms;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributesBuilder;
import org.anddev.andengine.util.Transformation;

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

	public static final int POSITIONCOORDINATES_PER_VERTEX = 2;
	public static final int COLORCOMPONENTS_PER_VERTEX = 4;
	public static final int TEXTURECOORDINATES_PER_VERTEX = 2;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = SpriteBatch.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX_R = SpriteBatch.VERTEX_INDEX_Y + 1;
	public static final int COLOR_INDEX_G = SpriteBatch.COLOR_INDEX_R + 1;
	public static final int COLOR_INDEX_B = SpriteBatch.COLOR_INDEX_G + 1;
	public static final int COLOR_INDEX_A = SpriteBatch.COLOR_INDEX_B + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = SpriteBatch.COLOR_INDEX_A + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = SpriteBatch.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = SpriteBatch.POSITIONCOORDINATES_PER_VERTEX + SpriteBatch.COLORCOMPONENTS_PER_VERTEX + SpriteBatch.TEXTURECOORDINATES_PER_VERTEX;
	public static final int VERTICES_PER_SPRITE = 6;
	public static final int SPRITE_SIZE = SpriteBatch.VERTEX_SIZE * SpriteBatch.VERTICES_PER_SPRITE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION, SpriteBatch.POSITIONCOORDINATES_PER_VERTEX, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR, SpriteBatch.COLORCOMPONENTS_PER_VERTEX, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, SpriteBatch.TEXTURECOORDINATES_PER_VERTEX, GLES20.GL_FLOAT, false)
		.build();

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

	private boolean mBlendingEnabled;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatch(final ITexture pTexture, final int pCapacity) {
		this(pTexture, pCapacity, new SpriteBatchMesh(pCapacity, GLES20.GL_STATIC_DRAW, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT)); // TODO Measure: GLES20.GL_STATIC_DRAW against GLES20.GL_STREAM_DRAW and GLES20.GL_DYNAMIC_DRAW
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final SpriteBatchMesh pSpriteBatchMesh) {
		this.mTexture = pTexture;
		this.mCapacity = pCapacity;

		this.mSpriteBatchMesh = pSpriteBatchMesh;

		this.setBlendingEnabled(true);
		this.initBlendFunction();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isBlendingEnabled() {
		return this.mBlendingEnabled;
	}

	public void setBlendingEnabled(final boolean pBlendingEnabled) {
		this.mBlendingEnabled = pBlendingEnabled;
	}

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

	public void setIndex(final int pIndex) {
		this.assertCapacity(pIndex);

		this.mIndex = pIndex;

		final int vertexIndex = pIndex * SpriteBatch.VERTICES_PER_SPRITE;

		this.mSpriteBatchMesh.setIndex(vertexIndex);
	}

	public void setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mShaderProgram = pShaderProgram;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		if(this.mBlendingEnabled) {
			GLHelper.enableBlend();
			GLHelper.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
		}

		GLHelper.enableTextures();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		this.mTexture.bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.begin();

		final ShaderProgram shaderProgram = (this.mShaderProgram == null) ? DefaultShaderPrograms.SHADERPROGRAM_POSITION_COLOR_TEXTURECOORDINATES : this.mShaderProgram;
		this.mSpriteBatchMesh.draw(shaderProgram, GLES20.GL_TRIANGLES, this.mVertices);

		this.end();
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		if(this.mBlendingEnabled) {
			GLHelper.disableBlend();
		}

		GLHelper.disableTextures();

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

		if(this.mSpriteBatchMesh.getVertexBufferObject().isManaged()) {
			this.mSpriteBatchMesh.getVertexBufferObject().unloadFromActiveBufferObjectManager();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void begin() {
//		GLHelper.disableDepthMask(pGL); // TODO Test effect of this
	}

	protected void end() {
//		GLHelper.enableDepthMask(pGL);
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#addInner(ITextureRegion, float, float, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchMesh.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float)} {@link SpriteBatchMesh#add(ITextureRegion, float, float, Transformation, float, float, float)}.
	 */
	public void draw(final Sprite pSprite) {
		this.draw(pSprite, pSprite.getRed(), pSprite.getGreen(), pSprite.getBlue(), pSprite.getAlpha());
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float)} {@link SpriteBatchMesh#add(ITextureRegion, float, float, Transformation, float, float, float)}.
	 */
	public void draw(final Sprite pSprite, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.getRotation() == 0 && !pSprite.isScaled()) {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite) {
		this.drawWithoutChecks(pSprite, pSprite.getRed(), pSprite.getGreen(), pSprite.getBlue(), pSprite.getAlpha());
	}

	public void drawWithoutChecks(final Sprite pSprite, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.getRotation() == 0 && !pSprite.isScaled()) {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	public void submit() {
		this.onSubmit();
	}

	private void onSubmit() {
		this.mVertices = this.mIndex * Sprite.VERTICES_PER_SPRITE;

		this.mSpriteBatchMesh.submit();

		this.mIndex = 0;
		this.mSpriteBatchMesh.setIndex(0);
	}

	private void initBlendFunction() {
		if(this.mTexture.getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT); // TODO
		} else {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_DEFAULT); // TODO
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

	public static class SpriteBatchMesh extends Mesh {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final float[] VERTICES_TMP = new float[8];

		private static final Transformation TRANSFORATION_TMP = new Transformation();

		// ===========================================================
		// Fields
		// ===========================================================

		private int mIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SpriteBatchMesh(final int pCapacity, final int pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setIndex(final int pIndex) {
			this.mIndex = pIndex;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		/**
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pRed
		 * @param pGreen
		 * @param pBlue
		 * @param pAlpha
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchMesh.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.add(pTextureRegion, pWidth, pHeight, SpriteBatchMesh.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
		}

		/**
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pScaleX around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pScaleY around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pRed
		 * @param pGreen
		 * @param pBlue
		 * @param pAlpha
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchMesh.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.add(pTextureRegion, pWidth, pHeight, SpriteBatchMesh.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
		}

		/**
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pScaleX around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pScaleY around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pRed
		 * @param pGreen
		 * @param pBlue
		 * @param pAlpha
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchMesh.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchMesh.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.add(pTextureRegion, pWidth, pHeight, SpriteBatchMesh.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
		}

		/**
		 * @param pTextureRegion
		 * @param pWidth
		 * @param pHeight
		 * @param pTransformation
		 * @param pRed
		 * @param pGreen
		 * @param pBlue
		 * @param pAlpha
		 */
		public void add(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			SpriteBatchMesh.VERTICES_TMP[0] = 0;
			SpriteBatchMesh.VERTICES_TMP[1] = 0;

			SpriteBatchMesh.VERTICES_TMP[2] = 0;
			SpriteBatchMesh.VERTICES_TMP[3] = pHeight;

			SpriteBatchMesh.VERTICES_TMP[4] = pWidth;
			SpriteBatchMesh.VERTICES_TMP[5] = 0;

			SpriteBatchMesh.VERTICES_TMP[6] = pWidth;
			SpriteBatchMesh.VERTICES_TMP[7] = pHeight;

			pTransformation.transform(SpriteBatchMesh.VERTICES_TMP);

			this.addInner(pTextureRegion, SpriteBatchMesh.VERTICES_TMP[0], SpriteBatchMesh.VERTICES_TMP[1], SpriteBatchMesh.VERTICES_TMP[2], SpriteBatchMesh.VERTICES_TMP[3],  SpriteBatchMesh.VERTICES_TMP[4], SpriteBatchMesh.VERTICES_TMP[5], SpriteBatchMesh.VERTICES_TMP[6], SpriteBatchMesh.VERTICES_TMP[7], pRed, pGreen, pBlue, pAlpha);
		}

		/**
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pRed
		 * @param pGreen
		 * @param pBlue
		 * @param pAlpha
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			this.addInner(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pRed, pGreen, pBlue, pAlpha);
		}

		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final int x1 = Float.floatToRawIntBits(pX1);
			final int y1 = Float.floatToRawIntBits(pY1);
			final int x2 = Float.floatToRawIntBits(pX2);
			final int y2 = Float.floatToRawIntBits(pY2);
			final int redBits = Float.floatToRawIntBits(pRed);
			final int greenBits = Float.floatToRawIntBits(pGreen);
			final int blueBits = Float.floatToRawIntBits(pBlue);
			final int alphaBits = Float.floatToRawIntBits(pAlpha);
			final int u = Float.floatToRawIntBits(pTextureRegion.getU());
			final int v = Float.floatToRawIntBits(pTextureRegion.getV());
			final int u2 = Float.floatToRawIntBits(pTextureRegion.getU2());
			final int v2 = Float.floatToRawIntBits(pTextureRegion.getV2());

			final int[] bufferData = this.mVertexBufferObject.getBufferData();
			final int index = this.mIndex;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			this.mIndex += SpriteBatch.SPRITE_SIZE;
		}

		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final int x1 = Float.floatToRawIntBits(pX1);
			final int y1 = Float.floatToRawIntBits(pY1);
			final int x2 = Float.floatToRawIntBits(pX2);
			final int y2 = Float.floatToRawIntBits(pY2);
			final int x3 = Float.floatToRawIntBits(pX3);
			final int y3 = Float.floatToRawIntBits(pY3);
			final int x4 = Float.floatToRawIntBits(pX4);
			final int y4 = Float.floatToRawIntBits(pY4);
			final int redBits = Float.floatToRawIntBits(pRed);
			final int greenBits = Float.floatToRawIntBits(pGreen);
			final int blueBits = Float.floatToRawIntBits(pBlue);
			final int alphaBits = Float.floatToRawIntBits(pAlpha);
			final int u = Float.floatToRawIntBits(pTextureRegion.getU());
			final int v = Float.floatToRawIntBits(pTextureRegion.getV());
			final int u2 = Float.floatToRawIntBits(pTextureRegion.getU2());
			final int v2 = Float.floatToRawIntBits(pTextureRegion.getV2());

			final int[] bufferData = this.mVertexBufferObject.getBufferData();
			final int index = this.mIndex;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_R] = redBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_G] = greenBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_B] = blueBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX_A] = alphaBits;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			this.mIndex += SpriteBatch.SPRITE_SIZE;
		}

		public void submit() {
			this.getVertexBufferObject().setDirtyOnHardware();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
