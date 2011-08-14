package org.anddev.andengine.opengl.shader.util.constants;

import org.anddev.andengine.opengl.shader.ShaderProgram;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:55:23 - 10.08.2011
 */
public interface VertexShaders {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String VERTEXSHADER_POSITION_COLOR_TEXTURECOORDINATES =  
			"uniform mat4 " + ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderPrograms.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ShaderPrograms.ATTRIBUTE_COLOR + ";\n" +
			"attribute vec2 " + ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec4 " + ShaderPrograms.VARYING_COLOR + ";\n" +
		    "varying vec2 " + ShaderPrograms.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
		    "   " + ShaderPrograms.VARYING_COLOR + " = " + ShaderPrograms.ATTRIBUTE_COLOR + ";\n" +
		    "   " + ShaderPrograms.VARYING_TEXTURECOORDINATES + " = " + ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"   gl_Position = " + ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderPrograms.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String VERTEXSHADER_POSITION_COLOR =
			"uniform mat4 " + ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderPrograms.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ShaderPrograms.ATTRIBUTE_COLOR + ";\n" +
			"varying vec4 " + ShaderPrograms.VARYING_COLOR + ";\n" +
			"void main() {\n" +
			"   gl_Position = " + ShaderPrograms.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderPrograms.ATTRIBUTE_POSITION + ";\n" +
			"   " + ShaderPrograms.VARYING_COLOR + " = " + ShaderProgram.ATTRIBUTE_COLOR + ";\n" +
			"}";
	
	// ===========================================================
	// Methods
	// ===========================================================
}
