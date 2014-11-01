package org.andengine.opengl.shader;

import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:56:44 - 25.08.2011
 */
public class PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram extends ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================

	private static PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram INSTANCE;

	public static final String VERTEXSHADER =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"uniform float " + ShaderProgramConstants.UNIFORM_POSITION_INTERPOLATION_MIX_0 + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION_0 + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION_1 + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"	vec4 position = vec4(0, 0, 0, 1);\n" +
			"	position.xy = mix(" + ShaderProgramConstants.ATTRIBUTE_POSITION_0 + ".xy," + ShaderProgramConstants.ATTRIBUTE_POSITION_1 + ".xy," + ShaderProgramConstants.UNIFORM_POSITION_INTERPOLATION_MIX_0 + ");\n" +
			"	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * position;\n" +
			"}";

	public static final String FRAGMENTSHADER = PositionTextureCoordinatesTextureSelectShaderProgram.FRAGMENTSHADER;

	// ===========================================================
	// Fields
	// ===========================================================

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture1Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTextureSelectTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformPositionInterpolationMix0Location = ShaderProgramConstants.LOCATION_INVALID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram() {
		super(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.VERTEXSHADER, PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.FRAGMENTSHADER);
	}

	public static PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram getInstance() {
		if(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.INSTANCE == null) {
			PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.INSTANCE = new PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram();
		}
		return PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_0_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION_0);
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_1_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION_1);
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
		PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformTexture1Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_1);
		PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformTextureSelectTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURESELECT_TEXTURE_0);
		PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformPositionInterpolationMix0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_POSITION_INTERPOLATION_MIX_0);
	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_0_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_1_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformTexture0Location, 0);
		GLES20.glUniform1i(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.sUniformTexture1Location, 1);
	}

	@Override
	public void unbind(final GLState pGLState) {
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION);
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_0_LOCATION);
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_1_LOCATION);
		
		super.unbind(pGLState);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
