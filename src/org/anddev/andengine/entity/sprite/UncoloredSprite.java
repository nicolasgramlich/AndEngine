package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.anddev.andengine.opengl.vbo.IVertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:22:38 - 09.03.2010
 */
public class UncoloredSprite extends RectangularShape<HighPerformanceVertexBufferObject> {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = UncoloredSprite.VERTEX_INDEX_X + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = UncoloredSprite.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = UncoloredSprite.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = 2 + 2;
	public static final int VERTICES_PER_SPRITE = 4;
	public static final int SPRITE_SIZE = UncoloredSprite.VERTEX_SIZE * UncoloredSprite.VERTICES_PER_SPRITE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITextureRegion mTextureRegion;

	private boolean mFlippedVertical;
	private boolean mFlippedHorizontal;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link IVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link UncoloredSprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public UncoloredSprite(final float pX, final float pY, final ITextureRegion pTextureRegion) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link IVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link UncoloredSprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public UncoloredSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final DrawType pDrawType) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pDrawType);
	}

	/**
	 * Uses a default {@link IVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link UncoloredSprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link IVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link UncoloredSprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceVertexBufferObject(UncoloredSprite.SPRITE_SIZE, pDrawType, true, UncoloredSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public UncoloredSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final HighPerformanceVertexBufferObject pVertexBufferObject) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObject);
	}

	public UncoloredSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final HighPerformanceVertexBufferObject pVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObject, PositionTextureCoordinatesShaderProgram.getInstance());

		this.mTextureRegion = pTextureRegion;

		this.setBlendingEnabled(true);
		this.initBlendFunction(pTextureRegion);

		this.onUpdateVertices();
		this.onUpdateColor();
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction(this.mTextureRegion);
	}

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		this.mTextureRegion.getTexture().bind();

		this.mVertexBufferObject.bind(this.mShaderProgram);
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, UncoloredSprite.VERTICES_PER_SPRITE);
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		this.mVertexBufferObject.unbind(this.mShaderProgram);

		super.postDraw(pCamera);
	}

	@Override
	protected void onUpdateVertices() {
		final float[] bufferData = this.mVertexBufferObject.getBufferData();

		final float x = 0;
		final float y = 0;
		final float x2 = this.mWidth;
		final float y2 = this.mHeight;

		bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x;
		bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y;

		bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x;
		bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y2;

		bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x2;
		bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y;

		bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_X] = x2;
		bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.VERTEX_INDEX_Y] = y2;

		this.mVertexBufferObject.setDirtyOnHardware();
	}

	protected void onUpdateTextureCoordinates() {
		final float[] bufferData = this.mVertexBufferObject.getBufferData();

		final ITextureRegion textureRegion = this.mTextureRegion;

		final float u;
		final float v;
		final float u2;
		final float v2;

		if(this.mFlippedVertical) {
			if(this.mFlippedHorizontal) {
				u = textureRegion.getU2();
				u2 = textureRegion.getU();
				v = textureRegion.getV2();
				v2 = textureRegion.getV();
			} else {
				u = textureRegion.getU();
				u2 = textureRegion.getU2();
				v = textureRegion.getV2();
				v2 = textureRegion.getV();
			}
		} else {
			if(this.mFlippedHorizontal) {
				u = textureRegion.getU2();
				u2 = textureRegion.getU();
				v = textureRegion.getV();
				v2 = textureRegion.getV2();
			} else {
				u = textureRegion.getU();
				u2 = textureRegion.getU2();
				v = textureRegion.getV();
				v2 = textureRegion.getV2();
			}
		}

		bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u;
		bufferData[0 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v;

		bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u;
		bufferData[1 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v2;

		bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u2;
		bufferData[2 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v;

		bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_U] = u2;
		bufferData[3 * UncoloredSprite.VERTEX_SIZE + UncoloredSprite.TEXTURECOORDINATES_INDEX_V] = v2;

		this.mVertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
