package org.andengine.entity.shape;

import org.andengine.entity.IEntity;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 13:32:52 - 07.07.2010
 */
public interface IShape extends IEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BLENDFUNCTION_SOURCE_DEFAULT = GLES20.GL_SRC_ALPHA;
	public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	public static final int BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE;
	public static final int BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isBlendingEnabled();
	public void setBlendingEnabled(final boolean pBlendingEnabled);
	public int getBlendFunctionSource();
	public int getBlendFunctionDestination();
	public void setBlendFunctionSource(final int pBlendFunctionSource);
	public void setBlendFunctionDestination(final int pBlendFunctionDestination);
	public void setBlendFunction(final int pBlendFunctionSource, final int pBlendFunctionDestination);

	public VertexBufferObjectManager getVertexBufferObjectManager();
	public IVertexBufferObject getVertexBufferObject();
	public ShaderProgram getShaderProgram();
	public void setShaderProgram(final ShaderProgram pShaderProgram);
}