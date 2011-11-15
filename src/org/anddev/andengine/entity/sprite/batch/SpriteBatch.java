package org.anddev.andengine.entity.sprite.batch;

import java.nio.FloatBuffer;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.BufferUtils;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.opengl.vbo.IVertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.anddev.andengine.util.color.Color;
import org.anddev.andengine.util.transformation.Transformation;

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
	protected final ISpriteBatchVertexBufferObject mSpriteBatchVertexBufferObject;

	protected int mIndex;
	protected int mVertices;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatch(final ITexture pTexture, final int pCapacity) {
		this(pTexture, pCapacity, DrawType.STATIC);
	}
	
	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity) {
		this(pX, pY, pTexture, pCapacity, DrawType.STATIC);
	}
	
	public SpriteBatch(final ITexture pTexture, final int pCapacity, final DrawType pDrawType) {
		this(pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}
	
	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final DrawType pDrawType) {
		this(pX, pY, pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}
	
	public SpriteBatch(final ITexture pTexture, final int pCapacity, final ShaderProgram pShaderProgram) {
		this(pTexture, pCapacity, DrawType.STATIC, pShaderProgram);
	}
	
	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTexture, pCapacity, DrawType.STATIC, pShaderProgram);
	}
	
	public SpriteBatch(final ITexture pTexture, final int pCapacity, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}
	
	public SpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTexture, pCapacity, new HighPerformanceSpriteBatchVertexBufferObject(pCapacity * SpriteBatch.SPRITE_SIZE, pDrawType, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
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

	public void setIndex(final int pIndex) {
		this.assertCapacity(pIndex);

		this.mIndex = pIndex;

		final int bufferDataOffset = pIndex * SpriteBatch.VERTICES_PER_SPRITE;

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
	protected boolean isCulled(final Camera pCamera) {
		return false;
	}

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		if(this.mBlendingEnabled) {
			GLState.enableBlend();
			GLState.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
		}

		this.mTexture.bind();

		this.mSpriteBatchVertexBufferObject.bind(this.mShaderProgram);
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.begin();

		this.mSpriteBatchVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mVertices);

		this.end();
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		this.mSpriteBatchVertexBufferObject.unbind(this.mShaderProgram);

		if(this.mBlendingEnabled) {
			GLState.disableBlend();
		}

		super.postDraw(pCamera);
	}

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction(this.mTexture);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if(this.mSpriteBatchVertexBufferObject.isManaged()) {
			this.mSpriteBatchVertexBufferObject.unloadFromActiveBufferObjectManager();
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

		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pPackedColor);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)}.
	 */
	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pPackedColor);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#addInner(ITextureRegion, float, float, float, float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBufferObject.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mSpriteBatchVertexBufferObject.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pRed, pGreen, pBlue, pAlpha);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)} {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, Transformation, float)}.
	 */
	public void draw(final Sprite pSprite) {
		this.draw(pSprite, pSprite.getColor().getPacked());
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
				this.mSpriteBatchVertexBufferObject.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.mSpriteBatchVertexBufferObject.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	/**
	 * @see {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, float, float, float)} {@link SpriteBatchVertexBufferObject#add(ITextureRegion, float, float, Transformation, float, float, float, float)}.
	 */
	public void draw(final Sprite pSprite, final float pPackedColor) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.mSpriteBatchVertexBufferObject.addWithPackedColor(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pPackedColor);
			} else {
				this.mSpriteBatchVertexBufferObject.addWithPackedColor(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pPackedColor);
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
				this.mSpriteBatchVertexBufferObject.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pRed, pGreen, pBlue, pAlpha);
			} else {
				this.mSpriteBatchVertexBufferObject.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pRed, pGreen, pBlue, pAlpha);
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite, final float pPackedColor) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.isRotatedOrScaledOrSkewed()) {
				this.mSpriteBatchVertexBufferObject.addWithPackedColor(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation(), pPackedColor);
			} else {
				this.mSpriteBatchVertexBufferObject.addWithPackedColor(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight(), pPackedColor);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ISpriteBatchVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void setBufferDataOffset(final int pBufferDataOffset);

		public void add(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pRed, final float pGreen, final float pBlue, final float pAlpha);
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor);
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRed, final float pGreen, final float pBlue, final float pAlpha);
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha);
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha);
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha);

		public void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pRed, final float pGreen, final float pBlue, final float pAlpha);
		public void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha);

		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pPackedColor);
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor);
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pPackedColor);
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pPackedColor);
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pPackedColor);

		public void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pPackedColor);
		public void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pPackedColor);
	}

	public static abstract class SpriteBatchVertexBufferObject extends VertexBufferObject implements ISpriteBatchVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final float[] VERTICES_TMP = new float[8];

		private static final Transformation TRANSFORATION_TMP = new Transformation();

		// ===========================================================
		// Fields
		// ===========================================================

		protected int mBufferDataOffset;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SpriteBatchVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		@Override
		public void setBufferDataOffset(final int pBufferDataOffset) {
			this.mBufferDataOffset = pBufferDataOffset;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
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
		@Override
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.add(pTextureRegion, pWidth, pHeight, SpriteBatchVertexBufferObject.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
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
		@Override
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pPackedColor) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatchVertexBufferObject.TRANSFORATION_TMP, pPackedColor);
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
		@Override
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.add(pTextureRegion, pWidth, pHeight, SpriteBatchVertexBufferObject.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
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
		@Override
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY, final float pPackedColor) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatchVertexBufferObject.TRANSFORATION_TMP, pPackedColor);
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
		@Override
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.add(pTextureRegion, pWidth, pHeight, SpriteBatchVertexBufferObject.TRANSFORATION_TMP, pRed, pGreen, pBlue, pAlpha);
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
		@Override
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY, final float pPackedColor) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.setToIdentity();

			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postRotate(pRotation);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			SpriteBatchVertexBufferObject.TRANSFORATION_TMP.postTranslate(pX, pY);

			this.addWithPackedColor(pTextureRegion, pWidth, pHeight, SpriteBatchVertexBufferObject.TRANSFORATION_TMP, pPackedColor);
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
		@Override
		public void add(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			SpriteBatchVertexBufferObject.VERTICES_TMP[0] = 0;
			SpriteBatchVertexBufferObject.VERTICES_TMP[1] = 0;

			SpriteBatchVertexBufferObject.VERTICES_TMP[2] = 0;
			SpriteBatchVertexBufferObject.VERTICES_TMP[3] = pHeight;

			SpriteBatchVertexBufferObject.VERTICES_TMP[4] = pWidth;
			SpriteBatchVertexBufferObject.VERTICES_TMP[5] = 0;

			SpriteBatchVertexBufferObject.VERTICES_TMP[6] = pWidth;
			SpriteBatchVertexBufferObject.VERTICES_TMP[7] = pHeight;

			pTransformation.transform(SpriteBatchVertexBufferObject.VERTICES_TMP);

			this.addInner(pTextureRegion, SpriteBatchVertexBufferObject.VERTICES_TMP[0], SpriteBatchVertexBufferObject.VERTICES_TMP[1], SpriteBatchVertexBufferObject.VERTICES_TMP[2], SpriteBatchVertexBufferObject.VERTICES_TMP[3],  SpriteBatchVertexBufferObject.VERTICES_TMP[4], SpriteBatchVertexBufferObject.VERTICES_TMP[5], SpriteBatchVertexBufferObject.VERTICES_TMP[6], SpriteBatchVertexBufferObject.VERTICES_TMP[7], pRed, pGreen, pBlue, pAlpha);
		}

		/**
		 * @param pTextureRegion
		 * @param pWidth
		 * @param pHeight
		 * @param pTransformation
		 * @param pPackedColor
		 */
		@Override
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation, final float pPackedColor) {
			SpriteBatchVertexBufferObject.VERTICES_TMP[0] = 0;
			SpriteBatchVertexBufferObject.VERTICES_TMP[1] = 0;

			SpriteBatchVertexBufferObject.VERTICES_TMP[2] = 0;
			SpriteBatchVertexBufferObject.VERTICES_TMP[3] = pHeight;

			SpriteBatchVertexBufferObject.VERTICES_TMP[4] = pWidth;
			SpriteBatchVertexBufferObject.VERTICES_TMP[5] = 0;

			SpriteBatchVertexBufferObject.VERTICES_TMP[6] = pWidth;
			SpriteBatchVertexBufferObject.VERTICES_TMP[7] = pHeight;

			pTransformation.transform(SpriteBatchVertexBufferObject.VERTICES_TMP);

			this.addInnerWithPackedColor(pTextureRegion, SpriteBatchVertexBufferObject.VERTICES_TMP[0], SpriteBatchVertexBufferObject.VERTICES_TMP[1], SpriteBatchVertexBufferObject.VERTICES_TMP[2], SpriteBatchVertexBufferObject.VERTICES_TMP[3],  SpriteBatchVertexBufferObject.VERTICES_TMP[4], SpriteBatchVertexBufferObject.VERTICES_TMP[5], SpriteBatchVertexBufferObject.VERTICES_TMP[6], SpriteBatchVertexBufferObject.VERTICES_TMP[7], pPackedColor);
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
		@Override
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
		@Override
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
		@Override
		public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pPackedColor) {
			this.addInnerWithPackedColor(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight, pPackedColor);
		}

		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		@Override
		public void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			this.addInnerWithPackedColor(pTextureRegion, pX1, pY1, pX2, pY2, Color.pack(pRed, pGreen, pBlue, pAlpha));
		}

		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		@Override
		public void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
			this.addInnerWithPackedColor(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, Color.pack(pRed, pGreen, pBlue, pAlpha));
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class HighPerformanceSpriteBatchVertexBufferObject extends SpriteBatchVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		protected final float[] mBufferData;

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceSpriteBatchVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);

			this.mBufferData = new float[pCapacity];
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public float[] getBufferData() {
			return this.mBufferData;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected void onBufferData() {
			// TODO On honeycomb the nio buffers are significantly faster, and below native call might not be needed!
//			this.mFloatBuffer.position(0);
//			this.mFloatBuffer.put(this.mBufferData);
//			this.mFloatBuffer.position(0);
			BufferUtils.put(this.mByteBuffer, this.mBufferData, this.mBufferData.length, 0);

			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.limit(), this.mByteBuffer, this.mUsage);
		}

		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		@Override
		public void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pPackedColor) {
			final float[] bufferData = this.getBufferData();
			final int bufferDataOffset = this.mBufferDataOffset;

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

			if(pTextureRegion.isRotated()) {
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
		}


		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		@Override
		public void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pPackedColor) {
			final float[] bufferData = this.getBufferData();
			final int bufferDataOffset = this.mBufferDataOffset;

			final float x1 = pX1;
			final float y1 = pY1;
			final float x2 = pX2;
			final float y2 = pY2;
			final float u = pTextureRegion.getU();
			final float v = pTextureRegion.getV();
			final float u2 = pTextureRegion.getU2();
			final float v2 = pTextureRegion.getV2();

			if(pTextureRegion.isRotated()) {
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pPackedColor;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class LowMemorySpriteBatchVertexBufferObject extends SpriteBatchVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		protected final FloatBuffer mFloatBuffer;

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemorySpriteBatchVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);

			this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public FloatBuffer getFloatBuffer() {
			return this.mFloatBuffer;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected void onBufferData() {
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.limit(), this.mByteBuffer, this.mUsage);
		}

		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		@Override
		public void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pPackedColor) {
			final FloatBuffer bufferData = this.getFloatBuffer();
			final int bufferDataOffset = this.mBufferDataOffset;

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

			if(pTextureRegion.isRotated()) {
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x3);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y3);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x3);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y3);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x4);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y4);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
			} else {
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x3);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y3);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x3);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y3);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x4);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y4);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
			}

			this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
		}


		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		@Override
		public void addInnerWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pPackedColor) {
			final FloatBuffer bufferData = this.getFloatBuffer();
			final int bufferDataOffset = this.mBufferDataOffset;

			final float x1 = pX1;
			final float y1 = pY1;
			final float x2 = pX2;
			final float y2 = pY2;
			final float u = pTextureRegion.getU();
			final float v = pTextureRegion.getV();
			final float u2 = pTextureRegion.getU2();
			final float v2 = pTextureRegion.getV2();

			if(pTextureRegion.isRotated()) {
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
			} else {
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y1);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x1);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, y2);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pPackedColor);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
			}

			this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
