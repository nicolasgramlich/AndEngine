package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.vbo.HighPerformanceLineChainVertexBufferObject;
import org.andengine.entity.primitive.vbo.ILineChainVertexBufferObject;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.exception.MethodNotSupportedException;

import android.opengl.GLES20;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 20:26:36 - 30.05.2013
 */
public abstract class LineChain extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final float LINE_WIDTH_DEFAULT = GLState.LINE_WIDTH_DEFAULT;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = LineChain.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = LineChain.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mCapacity;
	protected final ILineChainVertexBufferObject mLineChainVertexBufferObject;

	protected final float[] mXs;
	protected final float[] mYs;

	protected float mLineWidth;

	protected int mIndex;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceLineChainVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link #VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public LineChain(final float pX, final float pY, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, LineChain.LINE_WIDTH_DEFAULT, pCapacity, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceLineChainVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link #VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public LineChain(final float pX, final float pY, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, LineChain.LINE_WIDTH_DEFAULT, pCapacity, pVertexBufferObjectManager, pDrawType);
	}

	/**
	 * Uses a default {@link HighPerformanceLineChainVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link #VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public LineChain(final float pX, final float pY, final float pLineWidth, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pLineWidth, pCapacity, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public LineChain(final float pX, final float pY, final float pLineWidth, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pLineWidth, pCapacity, new HighPerformanceLineChainVertexBufferObject(pVertexBufferObjectManager, pCapacity * LineChain.VERTEX_SIZE, pDrawType, true, LineChain.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public LineChain(final float pX, final float pY, final float pLineWidth, final int pCapacity, final ILineChainVertexBufferObject pLineChainVertexBufferObject) {
		super(pX, pY, 0, 0, PositionColorShaderProgram.getInstance());

		this.mXs = new float[pCapacity];
		this.mYs = new float[pCapacity];
		this.mCapacity = pCapacity;

		this.mLineWidth = pLineWidth;

		this.mLineChainVertexBufferObject = pLineChainVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCapacity() {
		return this.mCapacity;
	}

	public int getIndex() {
		return this.mIndex;
	}

	public void setIndex(final int pIndex) {
		this.assertCapacity(pIndex);

		this.mIndex = pIndex;
	}

	public float getLineWidth() {
		return this.mLineWidth;
	}

	public void setLineWidth(final float pLineWidth) {
		this.mLineWidth = pLineWidth;
	}

	@Override
	public void setX(final float pX) {
		final float dX = this.mX - pX;

		for (int i = this.mIndex - 1; i >= 0; i++) {
			this.mXs[i] += dX;
		}

		super.setX(pX);

		this.onUpdateVertices();
	}

	@Override
	public void setY(final float pY) {
		final float dY = this.mY - pY;

		for (int i = this.mIndex - 1; i >= 0; i++) {
			this.mYs[i] += dY;
		}

		super.setY(pY);

		this.onUpdateVertices();
	}

	@Override
	public void setPosition(final float pX, final float pY) {
		final float dX = this.mX - pX;
		final float dY = this.mY - pY;

		for (int i = this.mIndex - 1; i >= 0; i++) {
			this.mXs[i] += dX;
			this.mYs[i] += dY;
		}

		super.setPosition(pX, pY);

		this.onUpdateVertices();
	}

	public float getX(final int pIndex) {
		this.assertCapacity(pIndex);

		return this.mX + this.mXs[pIndex];
	}

	public float getY(final int pIndex) {
		this.assertCapacity(pIndex);

		return this.mY + this.mYs[pIndex];
	}

	public void setX(final int pIndex, final float pX) {
		this.assertCapacity(pIndex);
		this.assertIndex(pIndex);

		this.mXs[pIndex] = pX;

		this.onUpdateVertices();
	}

	public void setY(final int pIndex, final float pY) {
		this.assertCapacity(pIndex);
		this.assertIndex(pIndex);

		this.mYs[pIndex] = pY;

		this.onUpdateVertices();
	}

	public void add(final float pX, final float pY) {
		this.assertCapacity();

		this.mXs[this.mIndex] = pX;
		this.mYs[this.mIndex] = pY;

		this.mIndex++;

		this.onUpdateVertices();
	}

	public void shift() {
		final int length = this.mCapacity - 1;
		System.arraycopy(this.mXs, 0, this.mXs, 1, length);
		System.arraycopy(this.mYs, 0, this.mYs, 1, length);
	}

	@Deprecated
	@Override
	public void setWidth(final float pWidth) {
		super.setWidth(pWidth);
	}

	@Deprecated
	@Override
	public void setHeight(final float pHeight) {
		super.setHeight(pHeight);
	}

	@Deprecated
	@Override
	public void setSize(final float pWidth, final float pHeight) {
		super.setSize(pWidth, pHeight);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ILineChainVertexBufferObject getVertexBufferObject() {
		return this.mLineChainVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		pGLState.lineWidth(this.mLineWidth);

		this.mLineChainVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mLineChainVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mLineChainVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mLineChainVertexBufferObject.onUpdateVertices(this);
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		throw new MethodNotSupportedException();
	}

	@Override
	public float[] getSceneCenterCoordinates(final float[] pReuse) {
		throw new MethodNotSupportedException();
	}

	@Override
	@Deprecated
	public boolean contains(final float pX, final float pY) {
		throw new MethodNotSupportedException();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void assertIndex(final int pIndex) {
		if (pIndex >= this.mIndex - 1) {
			throw new IllegalStateException("This supplied pIndex: '" + pIndex + "' is exceeding the current index: '" + this.mIndex + "' of this " + this.getClass().getSimpleName() + "!");
		}
	}

	private void assertCapacity(final int pIndex) {
		if (pIndex >= this.mCapacity) {
			throw new IllegalStateException("This supplied pIndex: '" + pIndex + "' is exceeding the capacity: '" + this.mCapacity + "' of this " + this.getClass().getSimpleName() + "!");
		}
	}

	private void assertCapacity() {
		if (this.mIndex == this.mCapacity) {
			throw new IllegalStateException("This " + this.getClass().getSimpleName() + " has already reached its capacity (" + this.mCapacity + ") !");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}