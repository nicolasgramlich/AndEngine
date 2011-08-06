package org.anddev.andengine.entity.primitive;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Rectangle extends BaseRectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int FLOAT_TO_RAW_INT_BITS_ZERO = Float.floatToIntBits(0);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight, final Mesh pMesh, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pMesh, pShaderProgram);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void doDraw(final Camera pCamera) {
		this.mMesh.render(this.mShaderProgram, GLES20.GL_TRIANGLE_STRIP, 4);
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();

		final int x = Rectangle.FLOAT_TO_RAW_INT_BITS_ZERO;
		final int y = Rectangle.FLOAT_TO_RAW_INT_BITS_ZERO;
		final int x2 = Float.floatToRawIntBits(this.mWidth);
		final int y2 = Float.floatToRawIntBits(this.mHeight);

		final int[] bufferData = vertexBufferObject.getBufferData();
		bufferData[0] = x;
		bufferData[1] = y;

		bufferData[2] = x;
		bufferData[3] = y2;

		bufferData[4] = x2;
		bufferData[5] = y;

		bufferData[6] = x2;
		bufferData[7] = y2;

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
