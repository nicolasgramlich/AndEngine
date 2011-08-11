package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributesBuilder;
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

	public static final int POSITIONCOORDINATES_PER_VERTEX = 2;
	public static final int COLORCOMPONENTS_PER_VERTEX = 4;
	public static final int TEXTURECOORDINATES_PER_VERTEX = 2;
	
	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Sprite.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX_R = Sprite.VERTEX_INDEX_Y + 1;
	public static final int COLOR_INDEX_G = Sprite.COLOR_INDEX_R + 1;
	public static final int COLOR_INDEX_B = Sprite.COLOR_INDEX_G + 1;
	public static final int COLOR_INDEX_A = Sprite.COLOR_INDEX_B + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = Sprite.COLOR_INDEX_A + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = Sprite.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = POSITIONCOORDINATES_PER_VERTEX + COLORCOMPONENTS_PER_VERTEX + TEXTURECOORDINATES_PER_VERTEX;
	public static final int VERTICES_PER_SPRITE = 4;
	public static final int SPRITE_SIZE = Sprite.VERTEX_SIZE * Sprite.VERTICES_PER_SPRITE;
	public static final int VERTEX_STRIDE = Sprite.VERTEX_SIZE * DataConstants.BYTES_PER_FLOAT;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION, POSITIONCOORDINATES_PER_VERTEX, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR, COLORCOMPONENTS_PER_VERTEX, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, TEXTURECOORDINATES_PER_VERTEX, GLES20.GL_FLOAT, false)
		.build();

	public static final String SHADERPROGRAM_VERTEXSHADER_DEFAULT =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
            "varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
            "   " + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
            "   " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String SHADERPROGRAM_FRAGMENTSHADER_DEFAULT =
			"precision mediump float;\n" + // TODO Try 'precision lowp float;\n'
		    "uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
            "varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
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
		this(pX, pY, pWidth, pHeight, pTextureRegion, new Mesh(Sprite.SPRITE_SIZE, GLES20.GL_STATIC_DRAW, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final Mesh pMesh) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pMesh);
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final Mesh pMesh) {
		super(pX, pY, pWidth, pHeight, pMesh);

		this.mTextureRegion = pTextureRegion;

		this.setBlendingEnabled(true);
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
		GLHelper.disableTextures();

		super.postDraw(pCamera);
	}
	
	@Override
	protected void onUpdateColor() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();

		final int redBits = Float.floatToRawIntBits(this.mRed);
		final int greenBits = Float.floatToRawIntBits(this.mGreen);
		final int blueBits = Float.floatToRawIntBits(this.mBlue);
		final int alphaBits = Float.floatToRawIntBits(this.mAlpha);

		final int[] bufferData = vertexBufferObject.getBufferData();

		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_R] = redBits;
		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_G] = greenBits;
		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_B] = blueBits;
		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_A] = alphaBits;

		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_R] = redBits;
		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_G] = greenBits;
		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_B] = blueBits;
		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_A] = alphaBits;

		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_R] = redBits;
		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_G] = greenBits;
		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_B] = blueBits;
		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_A] = alphaBits;

		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_R] = redBits;
		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_G] = greenBits;
		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_B] = blueBits;
		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX_A] = alphaBits;

		vertexBufferObject.setDirtyOnHardware();
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

		vertexBufferObject.setDirtyOnHardware();
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
				v = Float.floatToRawIntBits(textureRegion.getV());
				v2 = Float.floatToRawIntBits(textureRegion.getV2());
			} else {
				u = Float.floatToRawIntBits(textureRegion.getU());
				u2 = Float.floatToRawIntBits(textureRegion.getU2());
				v = Float.floatToRawIntBits(textureRegion.getV());
				v2 = Float.floatToRawIntBits(textureRegion.getV2());
			}
		}

		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

		vertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initBlendFunction() {
		if(this.mTextureRegion.getTexture().getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT); // TODO
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
