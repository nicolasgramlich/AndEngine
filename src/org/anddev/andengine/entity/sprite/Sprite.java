package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttribute;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.constants.DataConstants;
import org.anddev.andengine.util.constants.MathConstants;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:22:38 - 09.03.2010
 */
public class Sprite extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_SIZE = 2 + 2;
	public static final int VERTICES_PER_SPRITE = 4;
	public static final int SPRITE_SIZE = Sprite.VERTEX_SIZE * Sprite.VERTICES_PER_SPRITE;
	public static final int VERTEX_STRIDE = Sprite.VERTEX_SIZE * DataConstants.BYTES_PER_FLOAT;

	public static final int TEXTURECOORDINATES_INDEX_U = 2;
	public static final int TEXTURECOORDINATES_INDEX_V = 3;

	public static final VertexBufferObjectAttribute[] VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = {
		new VertexBufferObjectAttribute(ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false, Sprite.VERTEX_STRIDE, 0),
		new VertexBufferObjectAttribute(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false, Sprite.VERTEX_STRIDE, 2 * DataConstants.BYTES_PER_FLOAT)
	};

	private static final String SHADERPROGRAM_VERTEXSHADER_DEFAULT =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
            "varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
            "   " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	private static final String SHADERPROGRAM_FRAGMENTSHADER_DEFAULT =
			"precision mediump float;\n" +
		    "uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"uniform vec4 " + ShaderProgramConstants.UNIFORM_COLOR + ";\n" +
            "varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderProgramConstants.UNIFORM_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
			"}";

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITextureRegion mTextureRegion;

	private boolean mFlippedVertical;
	private boolean mFlippedHorizontal;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion);
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, new Mesh(Sprite.SPRITE_SIZE, GLES20.GL_STATIC_DRAW, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), null);
	}

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final Mesh pMesh, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pMesh, pShaderProgram);
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final Mesh pMesh, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pMesh);

		this.mTextureRegion = pTextureRegion;
		this.initBlendFunction();

		this.onUpdateTextureCoordinates();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ITextureRegion getTextureRegion() {
		return this.mTextureRegion;
	}

	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		this.mFlippedHorizontal = pFlippedHorizontal;
		this.onUpdateTextureCoordinates();
	}

	public void setFlippedVertical(final boolean pFlippedVertical) {
		this.mFlippedVertical = pFlippedVertical;
		this.onUpdateTextureCoordinates();
	}

	@Override
	public Sprite setShaderProgram(final ShaderProgram pShaderProgram) {
		return (Sprite) super.setShaderProgram(pShaderProgram);
	}

	@Override
	public Sprite setDefaultShaderProgram() {
		return this.setShaderProgram(new ShaderProgram(Sprite.SHADERPROGRAM_VERTEXSHADER_DEFAULT, Sprite.SHADERPROGRAM_FRAGMENTSHADER_DEFAULT) {
			@Override
			public void bind() {
				super.bind();

				this.setUniform(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX, GLHelper.getModelViewProjectionMatrix());
				this.setTexture(ShaderProgramConstants.UNIFORM_TEXTURE_0, 0);
				this.setUniform(ShaderProgramConstants.UNIFORM_COLOR, Sprite.this.mRed, Sprite.this.mGreen, Sprite.this.mBlue, Sprite.this.mAlpha);
			}
		});
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction();
	}

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		GLHelper.enableTextures();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		this.mTextureRegion.getTexture().bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLE_STRIP, Sprite.VERTICES_PER_SPRITE);
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		GLHelper.enableTextures();

		super.postDraw(pCamera);
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();

		final int x = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;
		final int y = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;
		final int x2 = Float.floatToRawIntBits(this.mWidth);
		final int y2 = Float.floatToRawIntBits(this.mHeight);

		final int[] bufferData = vertexBufferObject.getBufferData();
		bufferData[0 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x;
		bufferData[0 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y;

		bufferData[1 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x;
		bufferData[1 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
		bufferData[2 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y;

		bufferData[3 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
		bufferData[3 * Sprite.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

		final FastFloatBuffer buffer = vertexBufferObject.getFloatBuffer();
		buffer.position(0);
		buffer.put(bufferData);
		buffer.position(0);
	}

	protected void onUpdateTextureCoordinates() {
		final ITextureRegion textureRegion = this.mTextureRegion;
		final ITexture texture = textureRegion.getTexture();

		if(texture == null) { // TODO Check really needed?
			return;
		}

		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final int[] bufferData = vertexBufferObject.getBufferData();

		final int u;
		final int v;
		final int u2;
		final int v2;

		if(this.mFlippedVertical) {
			if(this.mFlippedHorizontal) {
				u = Float.floatToRawIntBits(textureRegion.getU2());
				u2 = Float.floatToRawIntBits(textureRegion.getU());
				v = Float.floatToRawIntBits(textureRegion.getV2());
				v2 = Float.floatToRawIntBits(textureRegion.getV());
			} else {
				u = Float.floatToRawIntBits(textureRegion.getU());
				u2 = Float.floatToRawIntBits(textureRegion.getU2());
				v = Float.floatToRawIntBits(textureRegion.getV2());
				v2 = Float.floatToRawIntBits(textureRegion.getV());
			}
		} else {
			if(this.mFlippedHorizontal) {
				u = Float.floatToRawIntBits(textureRegion.getU2());
				u2 = Float.floatToRawIntBits(textureRegion.getU());
				v = Float.floatToRawIntBits(textureRegion.getU2());
				v2 = Float.floatToRawIntBits(textureRegion.getU());
			} else {
				u = Float.floatToRawIntBits(textureRegion.getU());
				u2 = Float.floatToRawIntBits(textureRegion.getU2());
				v = Float.floatToRawIntBits(textureRegion.getU2());
				v2 = Float.floatToRawIntBits(textureRegion.getU());
			}
		}

		final FastFloatBuffer buffer = vertexBufferObject.getFloatBuffer();

		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

		buffer.position(0);
		buffer.put(bufferData);
		buffer.position(0);

		vertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initBlendFunction() {
		if(this.mTextureRegion.getTexture().getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(Shape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, Shape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
