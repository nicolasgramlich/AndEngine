package org.anddev.andengine.opengl.shader.util.constants;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:56:14 - 10.08.2011
 */
public interface DefaultShaderPrograms {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final ShaderProgram SHADERPROGRAM_POSITION_COLOR_TEXTURECOORDINATES = new ShaderProgram(VertexShaders.VERTEXSHADER_POSITION_COLOR_TEXTURECOORDINATES, FragmentShaders.FRAGMENTSHADER_COLOR_TEXTURECOORDINATES) {
		private int mUniformModelViewPositionMatrixLocation = ShaderProgram.LOCATION_INVALID;
		private int mUniformTexture0Location = ShaderProgram.LOCATION_INVALID;

		@Override
		protected void onCompiled() {
			this.mUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
			this.mUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
		};

		@Override
		public void bind() {
			super.bind();
			GLES20.glUniformMatrix4fv(this.mUniformModelViewPositionMatrixLocation, 1, false, GLHelper.getModelViewProjectionMatrix().getValues(), 0);
			GLES20.glUniform1i(this.mUniformTexture0Location, 0);
		};
	};

	public static final ShaderProgram SHADERPROGRAM_POSITION_COLOR = new ShaderProgram(VertexShaders.VERTEXSHADER_POSITION_COLOR, FragmentShaders.FRAGMENTSHADER_COLOR) {
		private int mUniformModelViewPositionMatrixLocation = ShaderProgram.LOCATION_INVALID;

		@Override
		protected void onCompiled() {
			this.mUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		};

		@Override
		public void bind() {
			super.bind();
			GLES20.glUniformMatrix4fv(this.mUniformModelViewPositionMatrixLocation, 1, false, GLHelper.getModelViewProjectionMatrix().getValues(), 0);
		};
	};

	// ===========================================================
	// Methods
	// ===========================================================
}
