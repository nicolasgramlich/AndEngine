package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.vbo.HighPerformanceLineVertexBufferObject;
import org.andengine.entity.primitive.vbo.ILineVertexBufferObject;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.algorithm.collision.LineCollisionChecker;
import org.andengine.util.exception.MethodNotSupportedException;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 09:50:36 - 04.04.2010
 */
public class Line extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final float LINE_WIDTH_DEFAULT = GLState.LINE_WIDTH_DEFAULT;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Line.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Line.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_LINE = 2;
	public static final int LINE_SIZE = Line.VERTEX_SIZE * Line.VERTICES_PER_LINE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mX2;
	protected float mY2;

	protected float mLineWidth;

	protected final ILineVertexBufferObject mLineVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceLineVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link #VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX1, pY1, pX2, pY2, Line.LINE_WIDTH_DEFAULT, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceLineVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link #VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX1, pY1, pX2, pY2, Line.LINE_WIDTH_DEFAULT, pVertexBufferObjectManager, pDrawType);
	}

	/**
	 * Uses a default {@link HighPerformanceLineVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link #VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX1, pY1, pX2, pY2, pLineWidth, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX1, pY1, pX2, pY2, pLineWidth, new HighPerformanceLineVertexBufferObject(pVertexBufferObjectManager, Line.LINE_SIZE, pDrawType, true, Line.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final ILineVertexBufferObject pLineVertexBufferObject) {
		super(pX1, pY1, pX2 - pX1, pY2 - pY1, PositionColorShaderProgram.getInstance());

		this.mX2 = pX2;
		this.mY2 = pY2;

		this.setOffsetCenter(0, 0);

		this.mLineWidth = pLineWidth;

		this.mLineVertexBufferObject = pLineVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @deprecated Instead use {@link #getX1()} or {@link #getX2()}.
	 */
	@Deprecated
	@Override
	public float getX() {
		return super.getX();
	}

	/**
	 * @deprecated Instead use {@link #getY1()} or {@link #getY2()}.
	 */
	@Deprecated
	@Override
	public float getY() {
		return super.getY();
	}

	public float getX1() {
		return super.getX();
	}

	public float getY1() {
		return super.getY();
	}

	public float getX2() {
		return this.mX2;
	}

	public float getY2() {
		return this.mY2;
	}

	public float getLineWidth() {
		return this.mLineWidth;
	}

	public void setLineWidth(final float pLineWidth) {
		this.mLineWidth = pLineWidth;
	}

	/**
	 * Instead use {@link #setPosition(float, float, float, float)}.
	 */
	@Deprecated
	@Override
	public void setX(final float pX) {
		final float dX = this.mX - pX;

		this.mX2 += dX;

		super.setX(pX);

		this.onUpdateVertices();
	}

	/**
	 * Instead use {@link #setPosition(float, float, float, float)}.
	 */
	@Deprecated
	@Override
	public void setY(final float pY) {
		final float dY = this.mY - pY;

		this.mY2 += dY;

		super.setY(pY);

		this.onUpdateVertices();
	}

	/**
	 * Instead use {@link #setPosition(float, float, float, float)}.
	 */
	@Deprecated
	@Override
	public void setPosition(final float pX, final float pY) {
		final float dX = this.mX - pX;
		final float dY = this.mY - pY;

		this.mX2 += dX;
		this.mY2 += dY;

		super.setPosition(pX, pY);

		this.onUpdateVertices();
	}

	public void setPosition(final float pX1, final float pY1, final float pX2, final float pY2) {
		this.mX2 = pX2;
		this.mY2 = pY2;

		super.setPosition(pX1, pY1);

		this.onUpdateVertices();
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
	public ILineVertexBufferObject getVertexBufferObject() {
		return this.mLineVertexBufferObject;
	}

	@Override
	public boolean isCulled(final Camera pCamera) {
		return pCamera.isLineVisible(this);
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		pGLState.lineWidth(this.mLineWidth);

		this.mLineVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mLineVertexBufferObject.draw(GLES20.GL_LINES, Line.VERTICES_PER_LINE);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mLineVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mLineVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mLineVertexBufferObject.onUpdateVertices(this);
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

	@Override
	public boolean collidesWith(final IEntity pOtherEntity) {
		if (pOtherEntity instanceof Line) {
			final Line otherLine = (Line) pOtherEntity;
			return LineCollisionChecker.checkLineCollision(this.mX, this.mY, this.mX2, this.mY2, otherLine.mX, otherLine.mY, otherLine.mX2, otherLine.mY2);
		} else {
			return super.collidesWith(pOtherEntity);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}