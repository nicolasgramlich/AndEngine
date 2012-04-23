package org.andengine.entity.sprite.batch;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.vbo.HighPerformanceSpriteBatchVertexBufferObject;
import org.andengine.entity.sprite.batch.vbo.ISpriteBatchVertexBufferObject;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.adt.transformation.Transformation;
import org.andengine.util.color.ColorUtils;

import android.opengl.GLES20;

/**
 * TODO TRY DEGENERATE TRIANGLES!
 * TODO Check if there is this multiple-of-X-byte(?) alignment optimization?
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:45:48 - 14.06.2011
 */
public class SpriteBatch extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float[] VERTICES_TMP = new float[8];

	private static final Transformation TRANSFORATION_TMP = new Transformation();

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

	protected ITexture mTexture;
	protected final int mCapacity;
	protected final ISpriteBatchVertexBufferObject mSpriteBatchVertexBufferObject;

	protected int mIndex;
	protected int mVertices;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
		this(pTexture, pCapacity, pSpriteBatchVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
		this(pX, pY, pTexture, pCapacity, pSpriteBatchVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(0, 0, pTexture, pCapacity, pSpriteBatchVertexBufferObject, pShaderProgram);
	}

	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pShaderProgram);

		this.mTexture = pTexture;
		this.mCapacity = pCapacity;
		this.mSpriteBatchVertexBufferObject = pSpriteBatchVertexBufferObject;

		this.setBlendingEnabled(true);
		this.initBlendFunction(this.mTexture);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getIndex() {
		return this.mIndex;
	}

	public ITexture getTexture() {
		return this.mTexture;
	}

	public void setTexture(final ITexture pTexture) {
		this.mTexture = pTexture;
	}

	public void setIndex(final int pIndex) {
		this.assertCapacity(pIndex);

		this.mIndex = pIndex;

		final int bufferDataOffset = pIndex * SpriteBatch.SPRITE_SIZE;

		this.mSpriteBatchVertexBufferObject.setBufferDataOffset(bufferDataOffset);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ISpriteBatchVertexBufferObject getVertexBufferObject() {
		return this.mSpriteBatchVertexBufferObject;
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		return false;
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return false;
	}

	@Override
	protected void onUpdateVertices() {
		/* Nothing. */
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		if(this.mBlendingEnabled) {
			pGLState.enableBlend();
			pGLState.blendFunction(this.mBlendFunctionSource, this.mBlendFunctionDestination);
		}

		this.mTexture.bind(pGLState);

		this.mSpriteBatchVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.begin();

		this.mSpriteBatchVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mVertices);

		this.end();
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteBatchVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		if(this.mBlendingEnabled) {
			pGLState.disableBlend();
		}

		super.postDraw(pGLState, pCamera);
	}

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction(this.mTexture);
	}

	@Override
	public void dispose() {
		super.dispose();

		if(this.mSpriteBatchVertexBufferObject != null && this.mSpriteBatchVertexBufferObject.isAutoDispose() && !this.mSpriteBatchVertexBufferObject.isDisposed()) {
			this.mSpriteBatchVertexBufferObject.dispose();
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
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pColorABGRPackedInt) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pColorABGRPackedInt);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)}.
	 */
	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pColorABGRPackedInt) {
		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pColorABGRPackedInt);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#addInner(ITextureRegion, float, float, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)} {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, Transformation, float)}.
	 */
	public void draw(final Sprite pSprite) {
		this.draw(pSprite, pSprite.getColor().getABGRPackedFloat());
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float)} {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, Transformation, float, float, float, float)}.
	 */
	public void draw(final Sprite pSprite, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)} {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, Transformation, float, float, float, float)}.
	 */
	public void draw(final Sprite pSprite, final float pColorABGRPackedInt) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.addWithPackedColor(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pColorABGRPackedInt);
			} else {
				this.addWithPackedColor(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pColorABGRPackedInt);
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite) {
		this.drawWithoutChecks(pSprite, pSprite.getColor().getABGRPackedFloat());
	}

	public void drawWithoutChecks(final Sprite pSprite, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite, final float pColorABGRPackedInt) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.addWithPackedColor(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pColorABGRPackedInt);
			} else {
				this.addWithPackedColor(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pColorABGRPackedInt);
			}

			this.mIndex++;
		}
	}

	public void submit() {
		this.onSubmit();
	}

	protected void onSubmit() {
		this.mVertices = this.mIndex * SpriteBatch.VERTICES_PER_SPRITE;

		this.mSpriteBatchVertexBufferObject.setDirtyOnHardware();

		this.mIndex = 0;
		this.mSpriteBatchVertexBufferObject.setBufferDataOffset(0);
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
	protected void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		SpriteBatch.TRANSFORATION_TMP.setToIdentity();

		SpriteBatch.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postRotate(pRotation);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(pX, pY);

		this.add(pTextureRegion, pWidth, pHeight, SpriteBatch.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
	}

	/**
	 * @param pTextureRegion
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
	 * @param pColorABGRPackedInt
	 */
	protected void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pColorABGRPackedInt) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		SpriteBatch.TRANSFORATION_TMP.setToIdentity();

		SpriteBatch.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postRotate(pRotation);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(pX, pY);

		this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatch.TRANSFORATION_TMP, pColorABGRPackedInt);
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
	protected void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		SpriteBatch.TRANSFORATION_TMP.setToIdentity();

		SpriteBatch.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(pX, pY);

		this.add(pTextureRegion, pWidth, pHeight, SpriteBatch.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
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
	 * @param pColorABGRPackedInt
	 */
	protected void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pColorABGRPackedInt) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		SpriteBatch.TRANSFORATION_TMP.setToIdentity();

		SpriteBatch.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(pX, pY);

		this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatch.TRANSFORATION_TMP, pColorABGRPackedInt);
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
	protected void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		SpriteBatch.TRANSFORATION_TMP.setToIdentity();

		SpriteBatch.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
		SpriteBatch.TRANSFORATION_TMP.postRotate(pRotation);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(pX, pY);

		this.add(pTextureRegion, pWidth, pHeight, SpriteBatch.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
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
	 * @param pColorABGRPackedInt
	 */
	protected void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pColorABGRPackedInt) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		SpriteBatch.TRANSFORATION_TMP.setToIdentity();

		SpriteBatch.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
		SpriteBatch.TRANSFORATION_TMP.postRotate(pRotation);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		SpriteBatch.TRANSFORATION_TMP.postTranslate(pX, pY);

		this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatch.TRANSFORATION_TMP, pColorABGRPackedInt);
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
	protected void add(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		SpriteBatch.VERTICES_TMP[0] = 0;
		SpriteBatch.VERTICES_TMP[1] = 0;

		SpriteBatch.VERTICES_TMP[2] = 0;
		SpriteBatch.VERTICES_TMP[3] = pHeight;

		SpriteBatch.VERTICES_TMP[4] = pWidth;
		SpriteBatch.VERTICES_TMP[5] = 0;

		SpriteBatch.VERTICES_TMP[6] = pWidth;
		SpriteBatch.VERTICES_TMP[7] = pHeight;

		pTransformation.transform(SpriteBatch.VERTICES_TMP);

		this.addInner(pTextureRegion, SpriteBatch.VERTICES_TMP[0], SpriteBatch.VERTICES_TMP[1], SpriteBatch.VERTICES_TMP[2], SpriteBatch.VERTICES_TMP[3],  SpriteBatch.VERTICES_TMP[4], SpriteBatch.VERTICES_TMP[5], SpriteBatch.VERTICES_TMP[6], SpriteBatch.VERTICES_TMP[7], pRed, pGreen, pBlue, pAlpha);
	}

	/**
	 * @param pTextureRegion
	 * @param pWidth
	 * @param pHeight
	 * @param pTransformation
	 * @param pColorABGRPackedInt
	 */
	protected void addWithPackedColor(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pColorABGRPackedInt) {
		SpriteBatch.VERTICES_TMP[0] = 0;
		SpriteBatch.VERTICES_TMP[1] = 0;

		SpriteBatch.VERTICES_TMP[2] = 0;
		SpriteBatch.VERTICES_TMP[3] = pHeight;

		SpriteBatch.VERTICES_TMP[4] = pWidth;
		SpriteBatch.VERTICES_TMP[5] = 0;

		SpriteBatch.VERTICES_TMP[6] = pWidth;
		SpriteBatch.VERTICES_TMP[7] = pHeight;

		pTransformation.transform(SpriteBatch.VERTICES_TMP);

		this.mSpriteBatchVertexBufferObject.addWithPackedColor(pTextureRegion, SpriteBatch.VERTICES_TMP[0], SpriteBatch.VERTICES_TMP[1], SpriteBatch.VERTICES_TMP[2], SpriteBatch.VERTICES_TMP[3],  SpriteBatch.VERTICES_TMP[4], SpriteBatch.VERTICES_TMP[5], SpriteBatch.VERTICES_TMP[6], SpriteBatch.VERTICES_TMP[7], pColorABGRPackedInt);
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
	protected void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.addInner(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pRed, pGreen, pBlue, pAlpha);
	}

	/**
	 * @param pTextureRegion
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pColorABGRPackedInt
	 */
	protected void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pColorABGRPackedInt) {
		this.mSpriteBatchVertexBufferObject.addWithPackedColor(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pColorABGRPackedInt);
	}

	/**
	 * @param pTextureRegion
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pColorABGRPackedInt
	 */
	protected void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pColorABGRPackedInt) {
		this.mSpriteBatchVertexBufferObject.addWithPackedColor(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pColorABGRPackedInt);
	}

	/**
	 * 1-+
	 * |X|
	 * +-2
	 */
	private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.addWithPackedColor(pTextureRegion, pX1, pY1, pX2, pY2, ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
	}

	/**
	 * 1-3
	 * |X|
	 * 2-4
	 */
	private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.addWithPackedColor(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
