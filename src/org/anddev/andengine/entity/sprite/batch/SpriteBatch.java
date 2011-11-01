package org.anddev.andengine.entity.sprite.batch;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttribute;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributesBuilder;
import org.anddev.andengine.util.color.Color;
import org.anddev.andengine.util.transformation.Transformation;

import android.opengl.GLES20;

/**
 * TODO Might extend Shape
 * TODO TRY DEGENERATE TRIANGLES!
 * TODO Check if there is this multiple-of-X-byte(?) alignment optimization?
 * TODO Add constructors taking x/y !
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

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = SpriteBatch.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = SpriteBatch.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = SpriteBatch.COLOR_INDEX + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = SpriteBatch.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = 2 + 1 + 2;
	public static final int VERTICES_PER_SPRITE = 6;
	public static final int SPRITE_SIZE = SpriteBatch.VERTEX_SIZE * SpriteBatch.VERTICES_PER_SPRITE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITexture mTexture;
	protected final int mCapacity;

	protected int mIndex;
	protected int mVertices;

	private int mSourceBlendFunction;
	private int mDestinationBlendFunction;

	protected final SpriteBatchMesh mSpriteBatchMesh;
	protected ShaderProgram mShaderProgram;

	private boolean mBlendingEnabled;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link SpriteBatch#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public SpriteBatch(final ITexture pTexture, final int pCapacity) {
		this(pTexture, pCapacity, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link SpriteBatch#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public SpriteBatch(final ITexture pTexture, final int pCapacity, final DrawType pDrawType) {
		this(pTexture, pCapacity, new SpriteBatchMesh(pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
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
			GLState.enableBlend();
			GLState.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
		}

		GLState.enableTextures();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		this.mTexture.bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.begin();

		final ShaderProgram shaderProgram = (this.mShaderProgram == null) ? PositionColorTextureCoordinatesShaderProgram.getInstance() : this.mShaderProgram; // TODO Add 'mFallbackShaderProgram' instead of permanent getInstance calls...
		this.mSpriteBatchMesh.draw(shaderProgram, GLES20.GL_TRIANGLES, this.mVertices);

		this.end();
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		if(this.mBlendingEnabled) {
			GLState.disableBlend();
		}

		GLState.disableTextures();

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
//		GLState.disableDepthMask(pGL); // TODO Test effect of this
	}

	protected void end() {
//		GLState.enableDepthMask(pGL);
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

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pPackedColor);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float)}.
	 */
	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pPackedColor);
		
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
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float)} {@link SpriteBatchMesh#add(ITextureRegion, float, float, Transformation, float)}.
	 */
	public void draw(final Sprite pSprite) {
		this.draw(pSprite, pSprite.getColor().getPacked());
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float, float, float, float)} {@link SpriteBatchMesh#add(ITextureRegion, float, float, Transformation, float, float, float, float)}.
	 */
	public void draw(final Sprite pSprite, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float, float)} {@link SpriteBatchMesh#add(ITextureRegion, float, float, Transformation, float, float, float, float)}.
	 */
	public void draw(final Sprite pSprite, final float pPackedColor) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.mSpriteBatchMesh.addWithPackedColor(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pPackedColor);
			} else {
				this.mSpriteBatchMesh.addWithPackedColor(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pPackedColor);
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite) {
		this.drawWithoutChecks(pSprite, pSprite.getColor().getPacked());
	}

	public void drawWithoutChecks(final Sprite pSprite, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite, final float pPackedColor) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.mSpriteBatchMesh.addWithPackedColor(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pPackedColor);
			} else {
				this.mSpriteBatchMesh.addWithPackedColor(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pPackedColor);
			}

			this.mIndex++;
		}
	}

	public void submit() {
		this.onSubmit();
	}

	protected void onSubmit() {
		this.mVertices = this.mIndex * SpriteBatch.VERTICES_PER_SPRITE;

		this.mSpriteBatchMesh.submit();

		this.mIndex = 0;
		this.mSpriteBatchMesh.setIndex(0);
	}

	private void initBlendFunction() {
		if(this.mTexture.getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		} else {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_DEFAULT);
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

		protected int mIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SpriteBatchMesh(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
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
		 * @param pPackedColor
		 */
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pPackedColor) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchMesh.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatchMesh.TRANSFORATION_TMP, pPackedColor);
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
		 * @param pPackedColor
		 */
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pPackedColor) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchMesh.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatchMesh.TRANSFORATION_TMP, pPackedColor);
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
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pScaleX around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pScaleY around the center (pWidth * 0.5f, pHeight * 0.5f)
		 * @param pPackedColor
		 */
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pPackedColor) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchMesh.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchMesh.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchMesh.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatchMesh.TRANSFORATION_TMP, pPackedColor);
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
		 * @param pWidth
		 * @param pHeight
		 * @param pTransformation
		 * @param pPackedColor
		 */
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pPackedColor) {
			SpriteBatchMesh.VERTICES_TMP[0] = 0;
			SpriteBatchMesh.VERTICES_TMP[1] = 0;

			SpriteBatchMesh.VERTICES_TMP[2] = 0;
			SpriteBatchMesh.VERTICES_TMP[3] = pHeight;

			SpriteBatchMesh.VERTICES_TMP[4] = pWidth;
			SpriteBatchMesh.VERTICES_TMP[5] = 0;

			SpriteBatchMesh.VERTICES_TMP[6] = pWidth;
			SpriteBatchMesh.VERTICES_TMP[7] = pHeight;

			pTransformation.transform(SpriteBatchMesh.VERTICES_TMP);

			this.addInnerWithPackedColor(pTextureRegion, SpriteBatchMesh.VERTICES_TMP[0], SpriteBatchMesh.VERTICES_TMP[1], SpriteBatchMesh.VERTICES_TMP[2], SpriteBatchMesh.VERTICES_TMP[3],  SpriteBatchMesh.VERTICES_TMP[4], SpriteBatchMesh.VERTICES_TMP[5], SpriteBatchMesh.VERTICES_TMP[6], SpriteBatchMesh.VERTICES_TMP[7], pPackedColor);
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
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pPackedColor
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
			this.addInnerWithPackedColor(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pPackedColor);
		}

		/**
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 * @param pPackedColor
		 */
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
			this.addInnerWithPackedColor(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pPackedColor);
		}

		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			this.addInnerWithPackedColor(pTextureRegion, pX1, pY1, pX2, pY2, Color.pack(pRed, pGreen, pBlue, pAlpha));
		}

		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		private void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pPackedColor) {
			final float x1 = pX1;
			final float y1 = pY1;
			final float x2 = pX2;
			final float y2 = pY2;
			final float u = pTextureRegion.getU();
			final float v = pTextureRegion.getV();
			final float u2 = pTextureRegion.getU2();
			final float v2 = pTextureRegion.getV2();

			final float[] bufferData = this.mVertexBufferObject.getBufferData();
			final int index = this.mIndex;

			if(pTextureRegion.isRotated()) {
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			this.mIndex += SpriteBatch.SPRITE_SIZE;
		}

		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			this.addInnerWithPackedColor(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, Color.pack(pRed, pGreen, pBlue, pAlpha));
		}

		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		private void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pPackedColor) {
			final float x1 = pX1;
			final float y1 = pY1;
			final float x2 = pX2;
			final float y2 = pY2;
			final float x3 = pX3;
			final float y3 = pY3;
			final float x4 = pX4;
			final float y4 = pY4;
			final float u = pTextureRegion.getU();
			final float v = pTextureRegion.getV();
			final float u2 = pTextureRegion.getU2();
			final float v2 = pTextureRegion.getV2();

			final float[] bufferData = this.mVertexBufferObject.getBufferData();
			final int index = this.mIndex;

			if(pTextureRegion.isRotated()) {
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			}
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
