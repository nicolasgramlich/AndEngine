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
			"precision lowp float;\n" + 
			"uniform sampler2D " + ShaderPrograms.UNIFORM_TEXTURE_0 + ";\n" +
			"varying lowp vec4 " + ShaderPrograms.VARYING_COLOR + ";\n" +
			"varying mediump vec2 " + ShaderPrograms.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderPrograms.VARYING_COLOR + " * texture2D(" + ShaderPrograms.UNIFORM_TEXTURE_0 + ", " + ShaderPrograms.VARYING_TEXTURECOORDINATES + ");\n" +
			"}";

	public static final String FRAGMENTSHADER_COLOR =
			"precision lowp float;\n" +
			"varying vec4 " + ShaderPrograms.VARYING_COLOR + ";\n" +
			"void main() {\n" +
			"  gl_FragColor = " + ShaderPrograms.VARYING_COLOR + ";\n" +
			"}";

	// ===========================================================
	// Methods
	// ===========================================================
}
