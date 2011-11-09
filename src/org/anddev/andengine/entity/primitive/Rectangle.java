package org.anddev.andengine.entity.primitive;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.shader.PositionColorShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttribute;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Rectangle extends RectangularShape<Mesh> {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Rectangle.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Rectangle.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_RECTANGLE = 4;
	public static final int RECTANGLE_SIZE = Rectangle.VERTEX_SIZE * Rectangle.VERTICES_PER_RECTANGLE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight) {
		this(pX, pY, pWidth, pHeight, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, new Mesh(Rectangle.RECTANGLE_SIZE, pDrawType, true, Rectangle.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Rectangle(final float pX, final float pY, final float pWidth, final float pHeight, final Mesh pMesh) {
		super(pX, pY, pWidth, pHeight, pMesh, PositionColorShaderProgram.getInstance());

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
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		this.mMesh.preDraw(this.mShaderProgram);
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(GLES20.GL_TRIANGLE_STRIP, Rectangle.VERTICES_PER_RECTANGLE);
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		this.mMesh.postDraw(this.mShaderProgram);

		super.postDraw(pCamera);
	}

	@Override
	protected void onUpdateColor() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float packedColor = this.mColor.getPacked();

		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();

		final float x = 0;
		final float y = 0;
		final float x2 = this.mWidth;
		final float y2 = this.mHeight;

		final float[] bufferData = vertexBufferObject.getBufferData();
		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x;
		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y;

		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x;
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x2;
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x2;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y2;

		vertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
