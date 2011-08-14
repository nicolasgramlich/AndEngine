package org.anddev.andengine.entity.primitive;

import org.anddev.andengine.collision.LineCollisionChecker;
import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.util.constants.ShaderPrograms;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributesBuilder;
import org.anddev.andengine.util.constants.Constants;

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

	public static final float LINE_WIDTH_DEFAULT = 1.0f;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Line.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Line.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_LINE = 2;
	public static final int LINE_SIZE = Line.VERTEX_SIZE * Line.VERTICES_PER_LINE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderPrograms.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderPrograms.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mX2;
	protected float mY2;

	private float mLineWidth;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Line(final float pX1, final float pY1, final float pX2, final float pY2) {
		this(pX1, pY1, pX2, pY2, Line.LINE_WIDTH_DEFAULT);
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth) {
		this(pX1, pY1, pX2, pY2, pLineWidth, new Mesh(Line.LINE_SIZE, GLES20.GL_STATIC_DRAW, true, Line.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final Mesh pMesh) {
		super(pX1, pY1, pMesh, ShaderPrograms.SHADERPROGRAM_POSITION_COLOR);

		this.mX2 = pX2;
		this.mY2 = pY2;

		this.mLineWidth = pLineWidth;

		this.onUpdateVertices();
		this.onUpdateColor();

		final float centerX = (this.mX2 - this.mX) * 0.5f;
		final float centerY = (this.mY2 - this.mY) * 0.5f;

		this.mRotationCenterX = centerX;
		this.mRotationCenterY = centerY;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @deprecated Instead use {@link Line#getX1()} or {@link Line#getX2()}.
	 */
	@Deprecated
	@Override
	public float getX() {
		return super.getX();
	}

	/**
	 * @deprecatedInstead use {@link Line#getY1()} or {@link Line#getY2()}.
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
	 * @deprecated Instead use {@link Line#setPosition(float, float, float, float)}.
	 */
	@Deprecated
	@Override
	public void setPosition(final float pX, final float pY) {
		final float dX = this.mX - pX;
		final float dY = this.mY - pY;

		super.setPosition(pX, pY);

		this.mX2 += dX;
		this.mY2 += dY;
	}

	public void setPosition(final float pX1, final float pY1, final float pX2, final float pY2) {
		this.mX2 = pX2;
		this.mY2 = pY2;

		super.setPosition(pX1, pY1);

		this.onUpdateVertices();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected boolean isCulled(final Camera pCamera) {
		return pCamera.isLineVisible(this);
	}

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		GLHelper.lineWidth(this.mLineWidth);
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_LINES, Line.VERTICES_PER_LINE);
	}

	@Override
	protected void onUpdateColor() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float packedColor = this.mColor.getPacked();

		bufferData[0 * Line.VERTEX_SIZE + Line.COLOR_INDEX] = packedColor;
		bufferData[1 * Line.VERTEX_SIZE + Line.COLOR_INDEX] = packedColor;

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		bufferData[0 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_X] = 0;
		bufferData[0 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_Y] = 0;

		bufferData[1 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_X] = this.mX2 - this.mX;
		bufferData[1 * Line.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = this.mY2 - this.mY;

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return null; // TODO
		//		return convertLocalToSceneCoordinates(this, (this.mX + this.mX2) * 0.5f, (this.mY + this.mY2) * 0.5f);
	}

	@Override
	@Deprecated
	public boolean contains(final float pX, final float pY) {
		return false;
	}

	@Override
	@Deprecated
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY) {
		return null;
	}

	@Override
	@Deprecated
	public float[] convertLocalToSceneCoordinates(final float pX, final float pY) {
		return null;
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof Line) {
			final Line otherLine = (Line) pOtherShape;
			return LineCollisionChecker.checkLineCollision(this.mX, this.mY, this.mX2, this.mY2, otherLine.mX, otherLine.mY, otherLine.mX2, otherLine.mY2);
		} else if(pOtherShape instanceof RectangularShape) {
			final RectangularShape rectangularShape = (RectangularShape) pOtherShape;
			return RectangularShapeCollisionChecker.checkCollision(rectangularShape, this);
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
}
