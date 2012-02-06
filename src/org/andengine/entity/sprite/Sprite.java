package org.andengine.entity.sprite;

import java.nio.FloatBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

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

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Sprite.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Sprite.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = Sprite.COLOR_INDEX + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = Sprite.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = 2 + 1 + 2;
	public static final int VERTICES_PER_SPRITE = 4;
	public static final int SPRITE_SIZE = Sprite.VERTEX_SIZE * Sprite.VERTICES_PER_SPRITE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITextureRegion mTextureRegion;
	protected final ISpriteVertexBufferObject mSpriteVertexBufferObject;

	protected boolean mFlippedVertical;
	protected boolean mFlippedHorizontal;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}
	
	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pVertexBufferObject) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObject);
	}
	
	public Sprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObject, pShaderProgram);
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}
	
	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceSpriteVertexBufferObject(pVertexBufferObjectManager, Sprite.SPRITE_SIZE, pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pShaderProgram);
		
		this.mTextureRegion = pTextureRegion;
		this.mSpriteVertexBufferObject = pSpriteVertexBufferObject;
		
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

	public boolean isFlippedHorizontal() {
		return this.mFlippedHorizontal;
	}

	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		this.mFlippedHorizontal = pFlippedHorizontal;
		this.onUpdateTextureCoordinates();
	}

	public boolean isFlippedVertical() {
		return this.mFlippedVertical;
	}

	public void setFlippedVertical(final boolean pFlippedVertical) {
		this.mFlippedVertical = pFlippedVertical;
		this.onUpdateTextureCoordinates();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ISpriteVertexBufferObject getVertexBufferObject() {
		return this.mSpriteVertexBufferObject;
	}

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction(this.getTextureRegion().getTexture());
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.getTextureRegion().getTexture().bind(pGLState);

		this.mSpriteVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Sprite.VERTICES_PER_SPRITE);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateVertices() {
		this.mSpriteVertexBufferObject.onUpdateVertices(this);
	}

	@Override
	protected void onUpdateColor() {
		this.mSpriteVertexBufferObject.onUpdateColor(this);
	}

	protected void onUpdateTextureCoordinates() {
		this.mSpriteVertexBufferObject.onUpdateTextureCoordinates(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ISpriteVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Sprite pSprite);
		public void onUpdateVertices(final Sprite pSprite);
		public void onUpdateTextureCoordinates(final Sprite pSprite);
	}

	public static class HighPerformanceSpriteVertexBufferObject extends HighPerformanceVertexBufferObject implements ISpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceSpriteVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Sprite pSprite) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pSprite.getColor().getFloatPacked();

			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Sprite pSprite) {
			final float[] bufferData = this.mBufferData;

			final float x = 0;
			final float y = 0;
			final float x2 = pSprite.getWidth(); // TODO Optimize with field access?
			final float y2 = pSprite.getHeight(); // TODO Optimize with field access?

			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final Sprite pSprite) {
			final float[] bufferData = this.mBufferData;

			final ITextureRegion textureRegion = pSprite.getTextureRegion(); // TODO Optimize with field access?

			final float u;
			final float v;
			final float u2;
			final float v2;

			if(pSprite.isFlippedVertical()) { // TODO Optimize with field access?
				if(pSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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
				if(pSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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

			if(textureRegion.isRotated()) {
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class LowMemorySpriteVertexBufferObject extends LowMemoryVertexBufferObject implements ISpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemorySpriteVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Sprite pSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float packedColor = pSprite.getColor().getFloatPacked();

			bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Sprite pSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float x = 0;
			final float y = 0;
			final float x2 = pSprite.getWidth(); // TODO Optimize with field access?
			final float y2 = pSprite.getHeight(); // TODO Optimize with field access?

			bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x);
			bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y);

			bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x);
			bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y2);

			bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x2);
			bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y);

			bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x2);
			bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y2);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final Sprite pSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final ITextureRegion textureRegion = pSprite.getTextureRegion(); // TODO Optimize with field access?

			final float u;
			final float v;
			final float u2;
			final float v2;

			if(pSprite.isFlippedVertical()) { // TODO Optimize with field access?
				if(pSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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
				if(pSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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

			if(textureRegion.isRotated()) {
				bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);
			} else {
				bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);
			}

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
