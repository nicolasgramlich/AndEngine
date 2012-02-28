package org.andengine.entity.shape;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene.ITouchArea;
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
public interface IShape extends IEntity, ITouchArea {
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

	public boolean collidesWith(final IShape pOtherShape);

	public boolean isBlendingEnabled();
	public void setBlendingEnabled(final boolean pBlendingEnabled);
	public int getSourceBlendFunction();
	public int getDestinationBlendFunction();
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction);

	public VertexBufferObjectManager getVertexBufferObjectManager();
	public IVertexBufferObject getVertexBufferObject();
	public ShaderProgram getShaderProgram();
	public void setShaderProgram(final ShaderProgram pShaderProgram);
}