package org.anddev.andengine.entity.primitive;

import org.anddev.andengine.collision.LineCollisionChecker;
import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.constants.MathConstants;

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

	private static final float LINEWIDTH_DEFAULT = 1.0f;

	private static final int VERTICES_PER_LINE = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mX2;
	protected float mY2;

	private float mLineWidth;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, Mesh pMesh, ShaderProgram pShaderProgram) {
		this(pX1, pY1, pX2, pY2, LINEWIDTH_DEFAULT, pMesh, pShaderProgram);
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, Mesh pMesh, ShaderProgram pShaderProgram) {
		super(pX1, pY1, pMesh, pShaderProgram);

		this.mX2 = pX2;
		this.mY2 = pY2;

		this.mLineWidth = pLineWidth;

		this.onUpdateVertices();

		final float width = this.getWidth();
		final float height = this.getHeight();

		this.mRotationCenterX = width * 0.5f;
		this.mRotationCenterY = height * 0.5f;

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

	@Override
	public float getBaseHeight() {
		return this.mY2 - this.mY;
	}

	@Override
	public float getBaseWidth() {
		return this.mX2 - this.mX;
	}

	@Override
	public float getHeight() {
		return this.mY2 - this.mY;
	}

	@Override
	public float getWidth() {
		return this.mX2 - this.mX;
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
	protected ShaderProgram createDefaultShaderProgram() {
		return null; // TODO 
	}

	@Override
	protected boolean isCulled(final Camera pCamera) {
		return pCamera.isLineVisible(this);
	}
	
	@Override
	protected void onUpdateVertices() {
        VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final int[] bufferData = vertexBufferObject.getBufferData();

        bufferData[0 + Constants.VERTEX_INDEX_X]  = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO; 
        bufferData[0 + Constants.VERTEX_INDEX_Y]  = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;

        bufferData[2 + Constants.VERTEX_INDEX_X]  = Float.floatToRawIntBits(this.mX2 - this.mX);
        bufferData[2 + Constants.VERTEX_INDEX_Y]  = Float.floatToRawIntBits(this.mY2 - this.mY);

        final FastFloatBuffer buffer = vertexBufferObject.getFloatBuffer();
        buffer.position(0);
        buffer.put(bufferData);
        buffer.position(0);

        vertexBufferObject.setDirtyOnHardware();
	}
	
	@Override
	protected void draw(Camera pCamera) {
		GLHelper.lineWidth(this.mLineWidth);

		GLES20.glDrawArrays(GLES20.GL_LINES, 0, VERTICES_PER_LINE);		
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
