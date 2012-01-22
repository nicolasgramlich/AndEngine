package org.andengine.entity.sprite;

import java.nio.FloatBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.HexagonalShape;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
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
	public static final int VERTICES_PER_HEXAGONSPRITE = 8;
	public static final int HEXAGONSPRITE_SIZE = HexagonSprite.VERTEX_SIZE * HexagonSprite.VERTICES_PER_HEXAGONSPRITE;

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
	/**
	 * Uses a default {@link HighPerformanceHexagonVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Hexagon#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public HexagonSprite(final float pX, final float pY, final float pSide, final ITextureRegion pTextureRegion) {
		this(pX, pY, pSide, pTextureRegion, DrawType.STATIC);
	}
	
	/**
	 * Uses a default {@link HighPerformanceHexagonVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Hexagon#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public HexagonSprite(final float pX, final float pY, final float pSide, final ITextureRegion pTextureRegion, final DrawType pDrawType) {
		this(pX, pY, pSide, pTextureRegion, new HighPerformanceHexagonSpriteVertexBufferObject(HexagonSprite.HEXAGONSPRITE_SIZE, pDrawType, true, HexagonSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public HexagonSprite(final float pX, final float pY, final float pSide, final ITextureRegion pTextureRegion, final IHexagonSpriteVertexBufferObject pHexagonSpriteVertexBufferObject, final ShaderProgram pShaderProgram ) {
		this(pX, pY, pSide, pTextureRegion, pHexagonSpriteVertexBufferObject, pShaderProgram, true);
	}
	
	public HexagonSprite(final float pX, final float pY, final float pSide, final ITextureRegion pTextureRegion, final IHexagonSpriteVertexBufferObject pHexagonSpriteVertexBufferObject, final ShaderProgram pShaderProgram, final boolean pInit ) {
		super(pX, pY, pSide, pShaderProgram);
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
		this.mHexagonSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_FAN, HexagonSprite.VERTICES_PER_HEXAGONSPRITE);
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
			bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;
			bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;
			bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;
			bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX] = packedColor;

			this.setDirtyOnHardware();
		}
		
		@Override
		public void onUpdateVertices(final HexagonSprite pHexagonSprite) {
			final float[] bufferData = this.mBufferData;

			final float side = pHexagonSprite.mSide;
			final float h = pHexagonSprite.mH;
			final float r = pHexagonSprite.mR;
			final float height = pHexagonSprite.mHeight;
			final float width = pHexagonSprite.mWidth;
			final float x = 0;
			final float y = 0;
			
			// Origin is the left upper corner of the hexagon
			//point 1, Middle of the hexagon
			bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = r;
			bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = height / 2;
			//point 2, Top of the hexagon
			bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = r;
			bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = y;
			//point 3, UpperLeft of the hexagon
			bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = x;
			bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = h;
			//point 4, LowerLeft of the hexagon
			bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = x;
			bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = side + h;
			//point 5, Bottom of the hexagon
			bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = r;
			bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = height;
			//point 6, LowerRight of the hexagon
			bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = width;
			bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = side + h;
			//point 7, UpperRight of the hexagon
			bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = width;
			bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = h;
			//point 8, Top again of the hexagon
			bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X] = r;
			bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y] = y;
			
			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final HexagonSprite pHexagonSprite) {
			final float[] bufferData = this.mBufferData;

			final ITextureRegion textureRegion = pHexagonSprite.getTextureRegion();

			final float u = textureRegion.getU();
			final float v = textureRegion.getV();
			final float u2 = textureRegion.getU2();
			final float v2 = textureRegion.getV2();
			
			/* Flipping the texture does not work at the current stage of code
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
			*/

			final float width = u + u2;
			final float height = v + v2;
			final float r = width / 2;
			final float side = 1 / ((float)Math.cos(30 * Math.PI / 180) / r);
			final float h = pHexagonSprite.calculateH(side);

			if(textureRegion.isRotated()) {
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = height / 2;

				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = width;
				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = h;

				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = width;
				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = side + h;
				
				bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = height;
				
				bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = side + h;
				
				bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = h;
				
				bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v;
			} else {
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = height / 2;

				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = h;

				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = side + h;
				
				bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = height;
				
				bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = width;
				bufferData[5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = side + h;
				
				bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = width;
				bufferData[6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = h;
				
				bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U] = r;
				bufferData[7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V] = v;
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
			bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);
			bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);
			bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);
			bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.COLOR_INDEX, packedColor);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final HexagonSprite pHexagonSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float side = pHexagonSprite.mSide;
			final float h = pHexagonSprite.mH;
			final float r = pHexagonSprite.mR;
			final float height = pHexagonSprite.mHeight;
			final float width = pHexagonSprite.mWidth;
			final float x = 0;
			final float y = 0;
			
			//point 1, Middle of the hexagon
			bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, r);
			bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, height / 2);
			//point 2, Top of the hexagon
			bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, r);
			bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, y);
			//point 7, UpperLeft of the hexagon
			bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, x);
			bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, h);
			//point 6, LowerLeft of the hexagon
			bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, x);
			bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, side + h);
			//point 5, Bottom of the hexagon
			bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, r);
			bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, height);
			//point 4, LowerRight of the hexagon
			bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, width);
			bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, side + h);
			//point 3, UpperRight of the hexagon
			bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, width);
			bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, h);
			//point 8, Top again of the hexagon
			bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_X, r);
			bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.VERTEX_INDEX_Y, y);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final HexagonSprite pHexagonSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final ITextureRegion textureRegion = pHexagonSprite.getTextureRegion();

			final float u = textureRegion.getU();
			final float v = textureRegion.getV();
			final float u2 = textureRegion.getU2();
			final float v2 = textureRegion.getV2();

			/* Flipping the texture does not work at the current stage of code
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
			*/

			final float width = u + u2;
			final float height = v + v2;
			final float r = width / 2;
			final float side = (float)Math.cos(30 * Math.PI / 180) * r;
			final float h = pHexagonSprite.calculateH(side);

			if(textureRegion.isRotated()) {
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, height / 2);

				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, width);
				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, h);

				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, width);
				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, side + h);
				
				bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, height);
				
				bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, side + h);
				
				bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, h);
				
				bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v);
			} else {
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(0 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, height / 2);

				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(1 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(2 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, h);

				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(3 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, side + h);
				
				bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(4 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, height);
				
				bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, width);
				bufferData.put(5 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, side + h);
				
				bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, width);
				bufferData.put(6 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, h);
				
				bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_U, r);
				bufferData.put(7 * HexagonSprite.VERTEX_SIZE + HexagonSprite.TEXTURECOORDINATES_INDEX_V, v);
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
