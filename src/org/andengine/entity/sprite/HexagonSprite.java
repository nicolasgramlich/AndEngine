package org.andengine.entity.sprite;

import java.nio.FloatBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.HexagonalShape;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * 
 * @author Stefan Hagdahl
 *
 */
public class HexagonSprite extends HexagonalShape {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = HexagonSprite.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = HexagonSprite.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = HexagonSprite.COLOR_INDEX + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = HexagonSprite.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = 2 + 1 + 2;
	public static final int VERTICES_PER_SPRITE = 8;
	public static final int HEXAGONSPRITE_SIZE = HexagonSprite.VERTEX_SIZE * HexagonSprite.VERTICES_PER_SPRITE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected final ITextureRegion mTextureRegion;
	protected final IHexagonSpriteVertexBufferObject mHexagonSpriteVertexBufferObject;

	protected boolean mFlippedVertical;
	protected boolean mFlippedHorizontal;
	// ===========================================================
	// Constructors
	// ===========================================================


	public HexagonSprite(final float pX, final float pY, final float pSide, final float pHeight, final float pWidth, final ITextureRegion pTextureRegion, final IHexagonSpriteVertexBufferObject pHexagonSpriteVertexBufferObject, final ShaderProgram pShaderProgram ) {
		this(pX, pY, pSide, pHeight, pWidth, pTextureRegion, pHexagonSpriteVertexBufferObject, pShaderProgram, true);
	}
	
	public HexagonSprite(final float pX, final float pY, final float pSide, final float pHeight, final float pWidth, final ITextureRegion pTextureRegion, final IHexagonSpriteVertexBufferObject pHexagonSpriteVertexBufferObject, final ShaderProgram pShaderProgram, final boolean pInit ) {
		super(pX, pY, pSide, pHeight, pWidth, pShaderProgram);
		this.mTextureRegion = pTextureRegion;
		this.mHexagonSpriteVertexBufferObject = pHexagonSpriteVertexBufferObject;
		
		this.setBlendingEnabled(true);
		this.initBlendFunction(pTextureRegion);
		
		if(pInit) {
			this.onUpdateVertices();
			this.onUpdateColor();
			this.onUpdateTextureCoordinates();
		}
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
	public IHexagonSpriteVertexBufferObject getVertexBufferObject() {
		return this.mHexagonSpriteVertexBufferObject;
	}

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction(this.mTextureRegion);
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mTextureRegion.getTexture().bind(pGLState);

		this.mHexagonSpriteVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mHexagonSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, HexagonSprite.VERTICES_PER_SPRITE);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mHexagonSpriteVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateVertices() {
		this.mHexagonSpriteVertexBufferObject.onUpdateVertices(this);
	}

	@Override
	protected void onUpdateColor() {
		this.mHexagonSpriteVertexBufferObject.onUpdateColor(this);
	}

	protected void onUpdateTextureCoordinates() {
		this.mHexagonSpriteVertexBufferObject.onUpdateTextureCoordinates(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IHexagonSpriteVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		public void onUpdateColor(final HexagonSprite pHexagonSprite);
		public void onUpdateVertices(final HexagonSprite pHexagonSprite);
		public void onUpdateTextureCoordinates(final HexagonSprite pHexagonSprite);
	}

	public static class HighPerformanceHexagonSpriteVertexBufferObject extends HighPerformanceVertexBufferObject implements IHexagonSpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceHexagonSpriteVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		
		@Override
		public void onUpdateColor(final HexagonSprite pHexagonSprite) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pHexagonSprite.getColor().getPacked();

			bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;
			bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;
			bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;
			bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;

			this.setDirtyOnHardware();
		}
		
		@Override
		public void onUpdateVertices(final HexagonSprite pHexagonSprite) {
			final float[] bufferData = this.mBufferData;

			final float x = 0;
			final float y = 0;
			final float x2 = pHexagonSprite.getWidth(); // TODO Optimize with field access?
			final float y2 = pHexagonSprite.getHeight(); // TODO Optimize with field access?

			final float xCenter = (x + x2) * 0.5f;
			final float yCenter = (y + y2) * 0.5f;

			bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = x;
			bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = yCenter;

			bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = xCenter;
			bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = y2;

			bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = xCenter;
			bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = y;

			bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = x2;
			bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = yCenter;

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final HexagonSprite pHexagonSprite) {
			final float[] bufferData = this.mBufferData;

			final ITextureRegion textureRegion = pHexagonSprite.getTextureRegion();

			final float u;
			final float v;
			final float u2;
			final float v2;

			if(pHexagonSprite.isFlippedVertical()) { // TODO Optimize with field access?
				if(pHexagonSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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
				if(pHexagonSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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

			final float uCenter = (u + u2) * 0.5f;
			final float vCenter = (v + v2) * 0.5f;

			if(textureRegion.isRotated()) {
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = vCenter;

				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = vCenter;

				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = vCenter;

				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = vCenter;
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

	public static class LowMemoryHexagonSpriteVertexBufferObject extends LowMemoryVertexBufferObject implements IHexagonSpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemoryHexagonSpriteVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		
		@Override
		public void onUpdateColor(final HexagonSprite pHexagonSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float packedColor = pHexagonSprite.getColor().getPacked();

			bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);
			bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);
			bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);
			bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final HexagonSprite pHexagonSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float x = 0;
			final float y = 0;
			final float x2 = pHexagonSprite.getWidth(); // TODO Optimize with field access?
			final float y2 = pHexagonSprite.getHeight(); // TODO Optimize with field access?

			final float xCenter = (x + x2) * 0.5f;
			final float yCenter = (y + y2) * 0.5f;

			bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, x);
			bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, yCenter);

			bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, xCenter);
			bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, y2);

			bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, xCenter);
			bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, y);

			bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, x2);
			bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, yCenter);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final HexagonSprite pHexagonSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final ITextureRegion textureRegion = pHexagonSprite.getTextureRegion();

			final float u;
			final float v;
			final float u2;
			final float v2;

			if(pHexagonSprite.isFlippedVertical()) { // TODO Optimize with field access?
				if(pHexagonSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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
				if(pHexagonSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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

			final float uCenter = (u + u2) * 0.5f;
			final float vCenter = (v + v2) * 0.5f;

			if(textureRegion.isRotated()) {
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, uCenter);
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, vCenter);

				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, vCenter);

				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, uCenter);
				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v2);
			} else {
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, vCenter);

				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, uCenter);
				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, uCenter);
				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, vCenter);
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
