package org.anddev.andengine.opengl.shader.util.constants;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:56:45 - 10.08.2011
 */
public interface FragmentShaders {
	// ===========================================================
	// Constants
	// ===========================================================

	// TODO Try 'precision lowp float;\n'
	public static final String FRAGMENTSHADER_COLOR_TEXTURECOORDINATES =
			"precision mediump float;\n" + 
			"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
			"}";

	public static final String FRAGMENTSHADER_COLOR =
			"precision mediump float;\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"}";

	// ===========================================================
	// Methods
	// ===========================================================
}
