package org.anddev.andengine.entity.primitive;

import org.anddev.andengine.collision.LineCollisionChecker;
import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttribute;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.constants.DataConstants;
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
	
	public static final float LINE_WIDTH_DEFAULT = 1.0f;

	public static final int VERTEX_SIZE = 2;
	public static final int VERTICES_PER_LINE = 4;
	public static final int LINE_SIZE = Line.VERTEX_SIZE * Line.VERTICES_PER_LINE;

	public static final VertexBufferObjectAttribute[] VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = { new VertexBufferObjectAttribute(ShaderProgramConstants.ATTRIBUTE_POSITION, Rectangle.VERTEX_SIZE, GLES20.GL_FLOAT, false, Line.VERTEX_SIZE * DataConstants.BYTES_PER_FLOAT, 0) };

	private static final String SHADERPROGRAM_VERTEXSHADER_DEFAULT =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
					"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
					"void main() {\n" +
					"   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
					"}";

	private static final String SHADERPROGRAM_FRAGMENTSHADER_DEFAULT =
			"precision mediump float;\n" +
					"uniform vec4 " + ShaderProgramConstants.UNIFORM_COLOR + ";\n" +
					"void main() {\n" +
					"  gl_FragColor = " + ShaderProgramConstants.UNIFORM_COLOR + ";\n" +
					"}";

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
		this(pX1, pY1, pX2, pY2, LINE_WIDTH_DEFAULT);
	}
	
	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth) {
		this(pX1, pY1, pX2, pY2, pLineWidth, null);
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final ShaderProgram pShaderProgram) {
		this(pX1, pY1, pX2, pY2, LINE_WIDTH_DEFAULT, pShaderProgram);
	}
	
	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final ShaderProgram pShaderProgram) {
		this(pX1, pY1, pX2, pY2, pLineWidth, new Mesh(Line.LINE_SIZE, GLES20.GL_STATIC_DRAW, true, Line.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public Line(final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final Mesh pMesh, final ShaderProgram pShaderProgram) {
		super(pX1, pY1, pMesh);

		this.mX2 = pX2;
		this.mY2 = pY2;

		this.mLineWidth = pLineWidth;

		this.onUpdateVertices();

		final float centerX = (this.mX2 - this.mX) * 0.5f;
		final float centerY = (this.mY2 - this.mY) * 0.5f;

		this.mRotationCenterX = centerX;
		this.mRotationCenterY = centerY;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;

		this.setShaderProgram((pShaderProgram == null) ? this.createDefaultShaderProgram() : pShaderProgram);
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
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final int[] bufferData = vertexBufferObject.getBufferData();

		bufferData[0 + Constants.VERTEX_INDEX_X] = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;
		bufferData[0 + Constants.VERTEX_INDEX_Y] = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;

		bufferData[2 + Constants.VERTEX_INDEX_X] = Float.floatToRawIntBits(this.mX2 - this.mX);
		bufferData[2 + Constants.VERTEX_INDEX_Y] = Float.floatToRawIntBits(this.mY2 - this.mY);

		final FastFloatBuffer buffer = vertexBufferObject.getFloatBuffer();
		buffer.position(0);
		buffer.put(bufferData);
		buffer.position(0);

		vertexBufferObject.setDirtyOnHardware();
	}
	
	@Override
	protected void preDraw(Camera pCamera) {
		super.preDraw(pCamera);
		
		GLHelper.lineWidth(this.mLineWidth);
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_LINES, Line.VERTICES_PER_LINE);
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

	public ShaderProgram createDefaultShaderProgram() {
		return new ShaderProgram(Line.SHADERPROGRAM_VERTEXSHADER_DEFAULT, Line.SHADERPROGRAM_FRAGMENTSHADER_DEFAULT) {
			@Override
			public void bind() {
				super.bind();

				this.setUniform(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX, GLHelper.getModelViewProjectionMatrix());
				this.setUniform(ShaderProgramConstants.UNIFORM_COLOR, Line.this.mRed, Line.this.mGreen, Line.this.mBlue, Line.this.mAlpha);
			}
		};
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
