package org.anddev.andengine.entity.primitive;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.RectangularShape;
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
 * @since 12:18:49 - 13.03.2010
 */
public class Rectangle extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_SIZE = 2;
	public static final int VERTICES_PER_RECTANGLE = 4;
	public static final int RECTANGLE_SIZE = Rectangle.VERTEX_SIZE * Rectangle.VERTICES_PER_RECTANGLE;
	public static final int VERTEX_STRIDE = Rectangle.VERTEX_SIZE * DataConstants.BYTES_PER_FLOAT;

	public static final VertexBufferObjectAttribute[] VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = {
		new VertexBufferObjectAttribute(ShaderProgramConstants.ATTRIBUTE_POSITION, Rectangle.VERTEX_SIZE, GLES20.GL_FLOAT, false, Rectangle.VERTEX_STRIDE, 0)
	};

	public static final String SHADERPROGRAM_VERTEXSHADER_DEFAULT =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"void main() {\n" +
			"   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String SHADERPROGRAM_FRAGMENTSHADER_DEFAULT =
			"precision mediump float;\n" +
			"uniform vec4 " + ShaderProgramConstants.UNIFORM_COLOR + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderProgramConstants.UNIFORM_COLOR + ";\n" +
			"}";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 * 
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pShaderProgram
	 */
	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight) {
		this(pX, pY, pWidth, pHeight, new Mesh(Rectangle.RECTANGLE_SIZE, GLES20.GL_STATIC_DRAW, true, Rectangle.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pMesh
	 * @param pShaderProgram if <code>null</code> is passed, a default {@link ShaderProgram} is used.
	 */
	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight, final Mesh pMesh) {
		super(pX, pY, pWidth, pHeight, pMesh);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	@Override
	public Rectangle setShaderProgram(ShaderProgram pShaderProgram) {
		return (Rectangle) super.setShaderProgram(pShaderProgram);
	}

	@Override
	public Rectangle setDefaultShaderProgram() {
		return this.setShaderProgram(new ShaderProgram(SHADERPROGRAM_VERTEXSHADER_DEFAULT, SHADERPROGRAM_FRAGMENTSHADER_DEFAULT){
			@Override
			public void bind() {
				super.bind();

				this.setUniform(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX, GLHelper.getModelViewProjectionMatrix());
				this.setUniform(ShaderProgramConstants.UNIFORM_COLOR, Rectangle.this.mRed, Rectangle.this.mGreen, Rectangle.this.mBlue, Rectangle.this.mAlpha);
			}
		});
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLE_STRIP, Rectangle.VERTICES_PER_RECTANGLE);
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();

		final int x = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;
		final int y = MathConstants.FLOAT_TO_RAW_INT_BITS_ZERO;
		final int x2 = Float.floatToRawIntBits(this.mWidth);
		final int y2 = Float.floatToRawIntBits(this.mHeight);

		final int[] bufferData = vertexBufferObject.getBufferData();
		bufferData[0 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x;
		bufferData[0 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y;

		bufferData[1 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x;
		bufferData[1 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
		bufferData[2 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y;

		bufferData[3 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = x2;
		bufferData[3 * Rectangle.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = y2;

		final FastFloatBuffer buffer = vertexBufferObject.getFloatBuffer();
		buffer.position(0);
		buffer.put(bufferData);
		buffer.position(0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
