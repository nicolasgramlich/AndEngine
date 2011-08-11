package org.anddev.andengine.opengl.shader.util.constants;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 01:03:16 - 07.08.2011
 */
public interface ShaderProgramConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String ATTRIBUTE_POSITION = "a_position";
	public static final String ATTRIBUTE_COLOR = "a_color";
	public static final String ATTRIBUTE_NORMAL = "a_normal";
	public static final String ATTRIBUTE_TEXTURECOORDINATES = "a_textureCoordinates";

	public static final String UNIFORM_MODELVIEWPROJECTIONMATRIX = "u_modelViewProjectionMatrix";
	public static final String UNIFORM_MODELVIEWMATRIX = "u_modelViewMatrix";
	public static final String UNIFORM_PROJECTIONMATRIX = "u_projectionMatrix";
	public static final String UNIFORM_COLOR = "u_color";
	public static final String UNIFORM_RED = "u_red";
	public static final String UNIFORM_GREEN = "u_green";
	public static final String UNIFORM_BLUE = "u_blue";
	public static final String UNIFORM_ALPHA = "u_alpha";
	public static final String UNIFORM_TEXTURE_0 = "u_texture_0";

	public static final String VARYING_TEXTURECOORDINATES = "v_textureCoordinates";
	public static final String VARYING_COLOR = "v_color";
	
	// ===========================================================
	// Methods
	// ===========================================================
}
