package org.andengine.opengl.shader.constants;

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

	public static final int LOCATION_INVALID = -1;

	public static final String ATTRIBUTE_POSITION = "a_position";
	public static final int ATTRIBUTE_POSITION_LOCATION = 0;
	public static final String ATTRIBUTE_COLOR = "a_color";
	public static final int ATTRIBUTE_COLOR_LOCATION = 1;
	public static final String ATTRIBUTE_NORMAL = "a_normal";
	public static final int ATTRIBUTE_NORMAL_LOCATION = 2;
	public static final String ATTRIBUTE_TEXTURECOORDINATES = "a_textureCoordinates";
	public static final int ATTRIBUTE_TEXTURECOORDINATES_LOCATION = 3;
	public static final String ATTRIBUTE_POSITION_0 = "a_position_0";
	public static final int ATTRIBUTE_POSITION_0_LOCATION = 4;
	public static final String ATTRIBUTE_POSITION_1 = "a_position_1";
	public static final int ATTRIBUTE_POSITION_1_LOCATION = 5;
	public static final String ATTRIBUTE_POSITION_2 = "a_position_2";
	public static final int ATTRIBUTE_POSITION_2_LOCATION = 6;

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
	public static final String UNIFORM_TEXTURESELECT_TEXTURE_0 = "u_textureselect_texture_0";
	public static final String UNIFORM_POSITION_INTERPOLATION_MIX_0 = "u_position_interpolation_mix_0";
	public static final String UNIFORM_POSITION_INTERPOLATION_MIX_1 = "u_position_interpolation_mix_1";

	public static final String VARYING_TEXTURECOORDINATES = "v_textureCoordinates";
	public static final String VARYING_COLOR = "v_color";

	// ===========================================================
	// Methods
	// ===========================================================
}
