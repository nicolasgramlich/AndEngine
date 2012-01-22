package org.andengine.entity.primitive;

import java.nio.FloatBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.HexagonalShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
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
public class Hexagon extends HexagonalShape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Hexagon.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Hexagon.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_HEXAGON = 8;
	public static final int HEXAGON_SIZE = Hexagon.VERTEX_SIZE * Hexagon.VERTICES_PER_HEXAGON;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IHexagonVertexBufferObject mHexagonVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceHexagonVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Hexagon#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Hexagon(final float pX, final float pY, final float pSide) {
		this(pX, pY, pSide, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceHexagonVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Hexagon#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Hexagon(final float pX, final float pY, final float pSide, final DrawType pDrawType) {
		this(pX, pY, pSide, new HighPerformanceHexagonVertexBufferObject(Hexagon.HEXAGON_SIZE, pDrawType, true, Hexagon.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Hexagon(final float pX, final float pY, final float pSide, final IHexagonVertexBufferObject pHexagonVertexBufferObject) {
		super(pX, pY, pSide, PositionColorShaderProgram.getInstance());

		this.mHexagonVertexBufferObject = pHexagonVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IHexagonVertexBufferObject getVertexBufferObject() {
		return this.mHexagonVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mHexagonVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mHexagonVertexBufferObject.draw(GLES20.GL_TRIANGLE_FAN, Hexagon.VERTICES_PER_HEXAGON);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mHexagonVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mHexagonVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mHexagonVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IHexagonVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Hexagon pHexagon);
		public void onUpdateVertices(final Hexagon pHexagon);
	}

	public static class HighPerformanceHexagonVertexBufferObject extends HighPerformanceVertexBufferObject implements IHexagonVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceHexagonVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Hexagon pHexagon) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pHexagon.getColor().getPacked();

			bufferData[0 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[1 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[2 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[3 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[4 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[5 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[6 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			bufferData[7 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX] = packedColor;
			
			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Hexagon pHexagon) {
			final float[] bufferData = this.mBufferData;

			final float side = pHexagon.mSide;
			final float h = pHexagon.mH;
			final float r = pHexagon.mR;
			final float height = pHexagon.mHeight;
			final float width = pHexagon.mWidth;
			final float x = 0;
			final float y = 0;
			
			// Origin is the left upper corner of the hexagon
			//point 1, Middle of the hexagon
			bufferData[0 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = r;
			bufferData[0 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = height / 2;
			//point 2, Top of the hexagon
			bufferData[1 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = r;
			bufferData[1 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = y;
			//point 3, UpperLeft of the hexagon
			bufferData[2 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = x;
			bufferData[2 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = h;
			//point 4, LowerLeft of the hexagon
			bufferData[3 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = x;
			bufferData[3 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = side + h;
			//point 5, Bottom of the hexagon
			bufferData[4 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = r;
			bufferData[4 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = height;
			//point 6, LowerRight of the hexagon
			bufferData[5 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = width;
			bufferData[5 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = side + h;
			//point 7, UpperRight of the hexagon
			bufferData[6 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = width;
			bufferData[6 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = h;
			//point 8, Top again of the hexagon
			bufferData[7 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X] = r;
			bufferData[7 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y] = y;
			
			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class LowMemoryHexagonVertexBufferObject extends LowMemoryVertexBufferObject implements IHexagonVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemoryHexagonVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Hexagon pHexagon) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float packedColor = pHexagon.getColor().getPacked();

			bufferData.put(0 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(1 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(2 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(3 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(4 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(5 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(6 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);
			bufferData.put(7 * Hexagon.VERTEX_SIZE + Hexagon.COLOR_INDEX, packedColor);

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Hexagon pHexagon) {
			final FloatBuffer bufferData = this.mFloatBuffer;
			
			final float side = pHexagon.mSide;
			final float h = pHexagon.mH;
			final float r = pHexagon.mR;
			final float height = pHexagon.mHeight;
			final float width = pHexagon.mWidth;
			final float x = 0;
			final float y = 0;
			
			//point 1, Middle of the hexagon
			bufferData.put(0 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, r);
			bufferData.put(0 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, height / 2);
			//point 2, Top of the hexagon
			bufferData.put(1 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, r);
			bufferData.put(1 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, y);
			//point 7, UpperLeft of the hexagon
			bufferData.put(2 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, x);
			bufferData.put(2 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, h);
			//point 6, LowerLeft of the hexagon
			bufferData.put(3 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, x);
			bufferData.put(3 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, side + h);
			//point 5, Bottom of the hexagon
			bufferData.put(4 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, r);
			bufferData.put(4 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, height);
			//point 4, LowerRight of the hexagon
			bufferData.put(5 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, width);
			bufferData.put(5 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, side + h);
			//point 3, UpperRight of the hexagon
			bufferData.put(6 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, width);
			bufferData.put(6 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, h);
			//point 8, Top again of the hexagon
			bufferData.put(7 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_X, r);
			bufferData.put(7 * Hexagon.VERTEX_SIZE + Hexagon.VERTEX_INDEX_Y, y);
			
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
