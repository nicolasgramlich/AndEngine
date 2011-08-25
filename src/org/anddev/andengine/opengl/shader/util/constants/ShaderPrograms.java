package org.anddev.andengine.opengl.shader.util.constants;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributes;

import android.opengl.GLES20;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 01:03:16 - 07.08.2011
 */
public interface ShaderPrograms {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String ATTRIBUTE_POSITION = "a_position";
	public static final int ATTRIBUTE_POSITION_LOCATION = 0;
	public static final String ATTRIBUTE_COLOR = "a_color";
	public static final int ATTRIBUTE_COLOR_LOCATION = 1;
	public static final String ATTRIBUTE_NORMAL = "a_normal";
	public static final int ATTRIBUTE_NORMAL_LOCATION = 2;
	public static final String ATTRIBUTE_TEXTURECOORDINATES = "a_textureCoordinates";
	public static final int ATTRIBUTE_TEXTURECOORDINATES_LOCATION = 3;

	public static final String UNIFORM_MODELVIEWPROJECTIONMATRIX = "u_modelViewProjectionMatrix";
	public static final String UNIFORM_MODELVIEWMATRIX = "u_modelViewMatrix";
	public static final String UNIFORM_PROJECTIONMATRIX = "u_projectionMatrix";
	public static final String UNIFORM_COLOR = "u_color";
	public static final String UNIFORM_RED = "u_red";
	public static final String UNIFORM_GREEN = "u_green";
	public static final String UNIFORM_BLUE = "u_blue";
	public static final String UNIFORM_ALPHA = "u_alpha";
	public static final String UNIFORM_TEXTURE_0 = "u_texture_0";
	public static final String UNIFORM_TEXTURE_1 = "u_texture_1";
	public static final String UNIFORM_TEXTURE_2 = "u_texture_2";
	public static final String UNIFORM_TEXTURE_3 = "u_texture_3";
	public static final String UNIFORM_TEXTURE_4 = "u_texture_4";
	public static final String UNIFORM_TEXTURE_5 = "u_texture_5";
	public static final String UNIFORM_TEXTURE_6 = "u_texture_6";
	public static final String UNIFORM_TEXTURE_7 = "u_texture_7";
	public static final String UNIFORM_TEXTURE_8 = "u_texture_8";
	public static final String UNIFORM_TEXTURE_9 = "u_texture_9";

	public static final String VARYING_TEXTURECOORDINATES = "v_textureCoordinates";
	public static final String VARYING_COLOR = "v_color";

	public static final ShaderProgram SHADERPROGRAM_POSITION_COLOR_TEXTURECOORDINATES = new ShaderProgram(VertexShaders.VERTEXSHADER_POSITION_COLOR_TEXTURECOORDINATES, FragmentShaders.FRAGMENTSHADER_COLOR_TEXTURECOORDINATES) {
		private int mUniformModelViewPositionMatrixLocation = ShaderProgram.LOCATION_INVALID;
		private int mUniformTexture0Location = ShaderProgram.LOCATION_INVALID;

		@Override
		protected void link() throws ShaderProgramLinkException {
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_POSITION_LOCATION, ShaderPrograms.ATTRIBUTE_POSITION);
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_COLOR_LOCATION, ShaderPrograms.ATTRIBUTE_COLOR);
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES);

			super.link();

			this.mUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX);
			this.mUniformTexture0Location = this.getUniformLocation(ShaderPrograms.UNIFORM_TEXTURE_0);
		}

		@Override
		public void bind(final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super.bind(pVertexBufferObjectAttributes);

			GLES20.glUniformMatrix4fv(this.mUniformModelViewPositionMatrixLocation, 1, false, GLState.getModelViewProjectionMatrix(), 0);
			GLES20.glUniform1i(this.mUniformTexture0Location, 0);
		};
	};

	public static final ShaderProgram SHADERPROGRAM_POSITION_TEXTURECOORDINATES = new ShaderProgram(VertexShaders.VERTEXSHADER_POSITION_TEXTURECOORDINATES, FragmentShaders.FRAGMENTSHADER_TEXTURECOORDINATES) {
		private int mUniformModelViewPositionMatrixLocation = ShaderProgram.LOCATION_INVALID;
		private int mUniformTexture0Location = ShaderProgram.LOCATION_INVALID;

		@Override
		protected void link() throws ShaderProgramLinkException {
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_POSITION_LOCATION, ShaderPrograms.ATTRIBUTE_POSITION);
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES);

			super.link();

			this.mUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX);
			this.mUniformTexture0Location = this.getUniformLocation(ShaderPrograms.UNIFORM_TEXTURE_0);
		}

		@Override
		public void bind(final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			GLES20.glDisableVertexAttribArray(ShaderProgram.ATTRIBUTE_COLOR_LOCATION);
			
			super.bind(pVertexBufferObjectAttributes);

			GLES20.glUniformMatrix4fv(this.mUniformModelViewPositionMatrixLocation, 1, false, GLState.getModelViewProjectionMatrix(), 0);
			GLES20.glUniform1i(this.mUniformTexture0Location, 0);
		};
		
		public void unbind(VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super.unbind(pVertexBufferObjectAttributes);
			
			GLES20.glEnableVertexAttribArray(ShaderProgram.ATTRIBUTE_COLOR_LOCATION);
		};
	};

	public static final ShaderProgram SHADERPROGRAM_POSITION_COLOR = new ShaderProgram(VertexShaders.VERTEXSHADER_POSITION_COLOR, FragmentShaders.FRAGMENTSHADER_COLOR) {
		private int mUniformModelViewPositionMatrixLocation = ShaderProgram.LOCATION_INVALID;

		@Override
		protected void link() throws ShaderProgramLinkException {
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_POSITION_LOCATION, ShaderPrograms.ATTRIBUTE_POSITION);
			GLES20.glBindAttribLocation(this.mProgramID, ShaderPrograms.ATTRIBUTE_COLOR_LOCATION, ShaderPrograms.ATTRIBUTE_COLOR);

			super.link();

			this.mUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		}

		@Override
		public void bind(final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			GLES20.glDisableVertexAttribArray(ShaderProgram.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

			super.bind(pVertexBufferObjectAttributes);

			GLES20.glUniformMatrix4fv(this.mUniformModelViewPositionMatrixLocation, 1, false, GLState.getModelViewProjectionMatrix(), 0);
		};
		
		public void unbind(VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super.unbind(pVertexBufferObjectAttributes);
			
			GLES20.glEnableVertexAttribArray(ShaderProgram.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);
		};
	};

	// ===========================================================
	// Methods
	// ===========================================================
}
