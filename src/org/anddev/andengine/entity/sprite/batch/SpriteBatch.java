package org.anddev.andengine.entity.sprite.batch;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttribute;
import org.anddev.andengine.util.Transformation;
import org.anddev.andengine.util.constants.Constants;
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

	private static final float[] VERTICES_TMP = new float[8];

	private static final Transformation TRANSFORATION_TMP = new Transformation();

	public static final int VERTEX_SIZE = 2 + 2;
	public static final int VERTICES_PER_SPRITE = 6;
	public static final int SPRITE_SIZE = SpriteBatch.VERTEX_SIZE * SpriteBatch.VERTICES_PER_SPRITE;
	public static final int VERTEX_STRIDE = SpriteBatch.VERTEX_SIZE * DataConstants.BYTES_PER_FLOAT;

	public static final int TEXTURECOORDINATES_INDEX_U = 2;
	public static final int TEXTURECOORDINATES_INDEX_V = 3;

	public static final VertexBufferObjectAttribute[] VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = {
		new VertexBufferObjectAttribute(ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false, SpriteBatch.VERTEX_STRIDE, 0),
		new VertexBufferObjectAttribute(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false, SpriteBatch.VERTEX_STRIDE, 2 * DataConstants.BYTES_PER_FLOAT)
	};

	public static final String SHADERPROGRAM_VERTEXSHADER_DEFAULT =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"   " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String SHADERPROGRAM_FRAGMENTSHADER_DEFAULT =
			"precision mediump float;\n" + // TODO Try 'precision lowp float;\n'
			"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"uniform vec4 " + ShaderProgramConstants.UNIFORM_COLOR + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderProgramConstants.UNIFORM_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
			"}";

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
		this(pTexture, pCapacity, new SpriteBatchMesh(pCapacity, GLES20.GL_STATIC_DRAW, true, SpriteBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public SpriteBatch(final ITexture pTexture, final int pCapacity, SpriteBatchMesh pSpriteBatchMesh) {
		this.mTexture = pTexture;
		this.mCapacity = pCapacity;

		this.mSpriteBatchMesh = pSpriteBatchMesh; // TODO Measure: GLES20.GL_STATIC_DRAW against GLES20.GL_STREAM_DRAW and GLES20.GL_DYNAMIC_DRAW

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

	public void setIndex(final int pIndex) {
		this.assertCapacity(pIndex);

		this.mIndex = pIndex;

		final int vertexIndex = pIndex * SpriteBatch.VERTICES_PER_SPRITE;

		this.mSpriteBatchMesh.setIndex(vertexIndex);
	}

	public SpriteBatch setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mShaderProgram = pShaderProgram;
		return this;
	}

	public SpriteBatch setDefaultShaderProgram() {
		return this.setShaderProgram(new ShaderProgram(SpriteBatch.SHADERPROGRAM_VERTEXSHADER_DEFAULT, SpriteBatch.SHADERPROGRAM_FRAGMENTSHADER_DEFAULT) {
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
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		GLHelper.blendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);

		GLHelper.enableTextures();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		this.mTexture.bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.begin();

		this.mSpriteBatchMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLES, this.mVertices);

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

		if(this.mSpriteBatchMesh.getVertexBufferObject().isManaged()) {
			this.mSpriteBatchMesh.getVertexBufferObject().unloadFromActiveBufferObjectManager();
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
	 * @see {@link SpriteBatchMesh#add(float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pScaleX, pScaleY);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		this.mSpriteBatchMesh.add(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#addInner(float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchMesh.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);

		this.mIndex++;
	}

	public void drawWithoutChecks(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this.mSpriteBatchMesh.addInner(pTextureRegion, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);

		this.mIndex++;
	}

	/**
	 * @see {@link SpriteBatchMesh#add(ITextureRegion, float, float, float, float)} {@link SpriteBatchMesh#add(ITextureRegion, float, float, Transformation)}.
	 */
	public void draw(final Sprite pSprite) {
		if(pSprite.isVisible()) {
			this.assertCapacity();

			final ITextureRegion textureRegion = pSprite.getTextureRegion();
			this.assertTexture(textureRegion);

			if(pSprite.getRotation() == 0 && !pSprite.isScaled()) {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
			} else {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation());
			}

			this.mIndex++;
		}
	}

	public void drawWithoutChecks(final Sprite pSprite) {
		if(pSprite.isVisible()) {
			final ITextureRegion textureRegion = pSprite.getTextureRegion();

			if(pSprite.getRotation() == 0 && !pSprite.isScaled()) {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
			} else {
				this.mSpriteBatchMesh.add(textureRegion, pSprite.getWidth(), pSprite.getHeight(), pSprite.getLocalToParentTransformation());
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
//			this.setBlendFunction(Shape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, Shape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT); // TODO
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

		// ===========================================================
		// Fields
		// ===========================================================

		private int mIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SpriteBatchMesh(final int pCapacity, final int pDrawType, final boolean pManaged, final VertexBufferObjectAttribute... pVertexBufferObjectAttributes) {
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
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			TRANSFORATION_TMP.setToIdentity();
			
			TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			TRANSFORATION_TMP.postRotate(pRotation);
			TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			TRANSFORATION_TMP.postTranslate(pX, pY);
			
			this.add(pTextureRegion, pWidth, pHeight, TRANSFORATION_TMP);
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
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;
			
			TRANSFORATION_TMP.setToIdentity();
			
			TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			TRANSFORATION_TMP.postTranslate(pX, pY);
			
			this.add(pTextureRegion, pWidth, pHeight, TRANSFORATION_TMP);
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
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
			final float widthHalf = pWidth * 0.5f;
			final float heightHalf = pHeight * 0.5f;

			TRANSFORATION_TMP.setToIdentity();
			
			TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
			TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
			TRANSFORATION_TMP.postRotate(pRotation);
			TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
			TRANSFORATION_TMP.postTranslate(pX, pY);
			
			this.add(pTextureRegion, pWidth, pHeight, TRANSFORATION_TMP);
		}

		/**
		 * @param pTextureRegion
		 * @param pWidth
		 * @param pHeight
		 * @param pTransformation
		 */
		public void add(final ITextureRegion pTextureRegion, final float pWidth, final float pHeight, final Transformation pTransformation) {
			VERTICES_TMP[0] = 0;
			VERTICES_TMP[1] = 0;

			VERTICES_TMP[2] = 0;
			VERTICES_TMP[3] = pHeight;

			VERTICES_TMP[4] = pWidth;
			VERTICES_TMP[5] = 0;

			VERTICES_TMP[6] = pWidth;
			VERTICES_TMP[7] = pHeight;

			pTransformation.transform(VERTICES_TMP);

			this.addInner(pTextureRegion, VERTICES_TMP[0], VERTICES_TMP[1], VERTICES_TMP[2], VERTICES_TMP[3],  VERTICES_TMP[4], VERTICES_TMP[5], VERTICES_TMP[6], VERTICES_TMP[7]);
		}

		/**
		 * @param pTextureRegion
		 * @param pX
		 * @param pY
		 * @param pWidth
		 * @param pHeight
		 */
		public void add(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
			this.addInner(pTextureRegion, pX, pY, pX + pWidth, pY + pHeight);
		}

		/**
		 * 1-+
		 * |X|
		 * +-2
		 */
		private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2) {
			final int x1 = Float.floatToRawIntBits(pX1);
			final int y1 = Float.floatToRawIntBits(pY1);
			final int x2 = Float.floatToRawIntBits(pX2);
			final int y2 = Float.floatToRawIntBits(pY2);

			final int[] bufferData = this.mVertexBufferObject.getBufferData();
			int index = this.mIndex;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x1;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y1;

			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x1;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y1;

			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y1;

			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x1;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

			this.addInner(pTextureRegion);

			this.mIndex += SpriteBatch.SPRITE_SIZE;
		}
		
		/**
		 * 1-3
		 * |X|
		 * 2-4
		 */
		private void addInner(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
			final int x1 = Float.floatToRawIntBits(pX1);
			final int y1 = Float.floatToRawIntBits(pY1);
			final int x2 = Float.floatToRawIntBits(pX2);
			final int y2 = Float.floatToRawIntBits(pY2);
			final int x3 = Float.floatToRawIntBits(pX3);
			final int y3 = Float.floatToRawIntBits(pY3);
			final int x4 = Float.floatToRawIntBits(pX4);
			final int y4 = Float.floatToRawIntBits(pY4);

			final int[] bufferData = this.mVertexBufferObject.getBufferData();
			int index = this.mIndex;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x1;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y1;

			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x3;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y3;

			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x3;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y3;

			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x4;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y4;

			this.addInner(pTextureRegion);

			this.mIndex += SpriteBatch.SPRITE_SIZE;
		}

		private void addInner(final ITextureRegion pTextureRegion) {
			final ITexture texture = pTextureRegion.getTexture();

			if(texture == null) { // TODO Check really needed?
				return;
			}

			final int u = Float.floatToRawIntBits(pTextureRegion.getU());
			final int v = Float.floatToRawIntBits(pTextureRegion.getV());
			final int u2 = Float.floatToRawIntBits(pTextureRegion.getU2());
			final int v2 = Float.floatToRawIntBits(pTextureRegion.getV2());

			final int[] bufferData = this.mVertexBufferObject.getBufferData();

			final int index = this.mIndex;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[index + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[index + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
		}

		public void submit() {
			final VertexBufferObject vertexBufferObject = this.mVertexBufferObject;

			final FastFloatBuffer buffer = vertexBufferObject.getFloatBuffer();

			buffer.position(0);
			buffer.put(vertexBufferObject.getBufferData());
			buffer.position(0);

			vertexBufferObject.setDirtyOnHardware();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
