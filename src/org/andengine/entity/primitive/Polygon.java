package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.exception.MethodNotSupportedException;

import android.opengl.GLES20;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:44:50 - 09.02.2012
 */
public class Polygon extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Polygon.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Polygon.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_POLYGON = 4;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IPolygonVertexBufferObject mPolygonVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformancePolygonVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Polygon#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Polygon(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pBufferData, pVertexCount, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformancePolygonVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Polygon#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Polygon(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, new HighPerformancePolygonVertexBufferObject(pVertexBufferObjectManager, pBufferData, pVertexCount, pDrawType, true, Polygon.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Polygon(final float pX, final float pY, final IPolygonVertexBufferObject pPolygonVertexBufferObject) {
		super(pX, pY, PositionColorShaderProgram.getInstance());

		this.mPolygonVertexBufferObject = pPolygonVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getBufferData() {
		return this.mPolygonVertexBufferObject.getBufferData();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IPolygonVertexBufferObject getVertexBufferObject() {
		return this.mPolygonVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mPolygonVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mPolygonVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Polygon.VERTICES_PER_POLYGON);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mPolygonVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mPolygonVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mPolygonVertexBufferObject.onUpdateVertices(this);
	}

	@Override
	@Deprecated
	public boolean contains(final float pX, final float pY) {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof Line) {
			// TODO
			return false;
		} else if(pOtherShape instanceof RectangularShape) {
			// TODO
			return false;
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPolygonVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public float[] getBufferData();
		public void onUpdateColor(final Polygon pPolygon);
		public void onUpdateVertices(final Polygon pPolygon);
	}

	public static class HighPerformancePolygonVertexBufferObject extends HighPerformanceVertexBufferObject implements IPolygonVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mVertexCount;

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformancePolygonVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final float[] pBufferData, final int pVertexCount, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pBufferData, pDrawType, pManaged, pVertexBufferObjectAttributes);

			this.mVertexCount = pVertexCount;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Polygon pPolygon) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pPolygon.getColor().getFloatPacked();

			for(int i = 0; i < this.mVertexCount; i++) {
				bufferData[i * Polygon.VERTEX_SIZE + Polygon.COLOR_INDEX] = packedColor;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Polygon pPolygon) {
			/* Since the buffer data is managed from the caller, we just mark the buffer data as dirty. */
			
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
