package org.andengine.opengl.texture;

import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:11:05 - 15.05.2012
 */
public class TextureWarmUpVertexBufferObject extends VertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_SIZE = 2 + 2;
	public static final int VERTICES_PER_VERTEXBUFFEROBJECT_SIZE = 3;
	public static final int VERTEXBUFFEROBJECT_SIZE = TextureWarmUpVertexBufferObject.VERTEX_SIZE * TextureWarmUpVertexBufferObject.VERTICES_PER_VERTEXBUFFEROBJECT_SIZE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureWarmUpVertexBufferObject() {
		super(null, VERTEXBUFFEROBJECT_SIZE, DrawType.STATIC, true, TextureWarmUpVertexBufferObject.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getHeapMemoryByteSize() {
		return 0;
	}

	@Override
	public int getNativeHeapMemoryByteSize() {
		return this.getByteCapacity();
	}

	@Override
	protected void onBufferData() {
		/* Nothing. */
	}

	public void warmup(final GLState pGLState, final ITexture pTexture) {
		pTexture.bind(pGLState);
		this.bind(pGLState, PositionTextureCoordinatesShaderProgram.getInstance());
		this.draw(GLES20.GL_TRIANGLES, VERTICES_PER_VERTEXBUFFEROBJECT_SIZE);
		this.unbind(pGLState, PositionTextureCoordinatesShaderProgram.getInstance());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
