package org.anddev.andengine.entity.shape;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.opengl.shader.ShaderProgram;

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
	// Final Fields
	// ===========================================================

	public static final int BLENDFUNCTION_SOURCE_DEFAULT = GLES20.GL_SRC_ALPHA;
	public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	public static final int BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE;
	public static final int BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isCullingEnabled();
	public void setCullingEnabled(final boolean pCullingEnabled);

//	public boolean isVisible(final Camera pCamera); // TODO. Could be use for automated culling.
	public boolean collidesWith(final IShape pOtherShape);

	public boolean isBlendingEnabled();
	public void setBlendingEnabled(final boolean pBlendingEnabled);
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction);
	
	public ShaderProgram getShaderProgram();
	public void setShaderProgram(final ShaderProgram pShaderProgram);
}