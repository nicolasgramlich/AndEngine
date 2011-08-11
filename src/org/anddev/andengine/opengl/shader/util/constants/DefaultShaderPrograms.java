package org.anddev.andengine.opengl.shader.util.constants;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.GLHelper;

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
		public void bind() {
			super.bind();
			this.setUniform(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX, GLHelper.getModelViewProjectionMatrix());
			this.setTexture(ShaderProgramConstants.UNIFORM_TEXTURE_0, 0);	
		};
	};
	
	public static final ShaderProgram SHADERPROGRAM_POSITION_COLOR = new ShaderProgram(VertexShaders.VERTEXSHADER_POSITION_COLOR, FragmentShaders.FRAGMENTSHADER_COLOR) {
		public void bind() {
			super.bind();
			this.setUniform(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX, GLHelper.getModelViewProjectionMatrix());
		};
	};

	// ===========================================================
	// Methods
	// ===========================================================
}
