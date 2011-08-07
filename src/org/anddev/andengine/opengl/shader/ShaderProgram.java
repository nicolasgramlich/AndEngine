package org.anddev.andengine.opengl.shader;

import java.util.HashMap;

import org.anddev.andengine.opengl.shader.exception.ShaderProgramException;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:56:34 - 05.08.2011
 */
public class ShaderProgram implements ShaderProgramConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWAREID_CONTAINER = new int[1];
	private static final int[] PARAMETERS_CONTAINER = new int[1];
	private static final int[] LENGTH_CONTAINER = new int[1];
	private static final int[] SIZE_CONTAINER = new int[1];
	private static final int[] TYPE_CONTAINER = new int[1];
	private static final int NAME_CONTAINER_SIZE = 64;
	private static final byte[] NAME_CONTAINER = new byte[ShaderProgram.NAME_CONTAINER_SIZE];

	// ===========================================================
	// Fields
	// ===========================================================

	protected final String mVertexShaderSource;
	protected final String mFragmentShaderSource;

	protected int mVertexShaderID;
	protected int mFragmentShaderID;
	protected int mProgramID;

	protected boolean mCompiled;

	private final HashMap<String, Integer> mUniformLocations = new HashMap<String, Integer>();
	private final HashMap<String, Integer> mAttributeLocations = new HashMap<String, Integer>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public ShaderProgram(final String pVertexShaderSource, final String pFragmentShaderSource) throws ShaderProgramException {
		this.mVertexShaderSource = pVertexShaderSource;
		this.mFragmentShaderSource = pFragmentShaderSource;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isCompiled() {
		return this.mCompiled;
	}

	public int getAttributeLocation(final String pAttributeName) {
		final Integer location = this.mAttributeLocations.get(pAttributeName);
		if(location != null) {
			return location.intValue();
		} else {
			throw new ShaderProgramException("Unexpected attribute: '" + pAttributeName + "'.");
		}
	}

	public int getUniformLocation(final String pUniformName) {
		final Integer location = this.mUniformLocations.get(pUniformName);
		if(location != null) {
			return location.intValue();
		} else {
			throw new ShaderProgramException("Unexpected uniform: '" + pUniformName + "'.");
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void bind() {
		if(!this.mCompiled) {
			this.compile();
		}
		GLES20.glUseProgram(this.mProgramID);
	}

	public void unbind() {
		GLES20.glUseProgram(0);
	}

	protected void compile() throws ShaderProgramException {
		this.mVertexShaderID = ShaderProgram.compileShader(this.mVertexShaderSource, GLES20.GL_VERTEX_SHADER);
		this.mFragmentShaderID = ShaderProgram.compileShader(this.mFragmentShaderSource, GLES20.GL_FRAGMENT_SHADER);

		this.mProgramID = GLES20.glCreateProgram();
		GLES20.glAttachShader(this.mProgramID, this.mVertexShaderID);
		GLES20.glAttachShader(this.mProgramID, this.mFragmentShaderID);

		this.link();
	}

	protected void link() {
		GLES20.glLinkProgram(this.mProgramID);

		GLES20.glGetProgramiv(this.mProgramID, GLES20.GL_LINK_STATUS, ShaderProgram.HARDWAREID_CONTAINER, 0);
		if(ShaderProgram.HARDWAREID_CONTAINER[0] == 0) {
			throw new ShaderProgramException(GLES20.glGetProgramInfoLog(this.mProgramID));
		}

		this.initAttributeLocations();
		this.initUniformLocations();

		this.mCompiled = true;
	}

	private static int compileShader(final String pSource, final int pType) throws ShaderProgramException {
		final int shaderID = GLES20.glCreateShader(pType);
		if(shaderID == 0) {
			throw new ShaderProgramException("Could not create Shader of type: '" + pType + '"');
		}

		GLES20.glShaderSource(shaderID, pSource);
		GLES20.glCompileShader(shaderID);

		GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, ShaderProgram.HARDWAREID_CONTAINER, 0);
		if(ShaderProgram.HARDWAREID_CONTAINER[0] == 0) {
			throw new ShaderProgramException(GLES20.glGetShaderInfoLog(shaderID));
		}
		return shaderID;
	}

	private void initUniformLocations() {
		this.mUniformLocations.clear();
		ShaderProgram.PARAMETERS_CONTAINER[0] = 0;
		GLES20.glGetProgramiv(this.mProgramID, GLES20.GL_ACTIVE_UNIFORMS, ShaderProgram.PARAMETERS_CONTAINER, 0);
		final int numUniforms = ShaderProgram.PARAMETERS_CONTAINER[0];

		for(int i = 0; i < numUniforms; i++) {
			GLES20.glGetActiveUniform(this.mProgramID, i, ShaderProgram.NAME_CONTAINER_SIZE, ShaderProgram.LENGTH_CONTAINER, 0, ShaderProgram.SIZE_CONTAINER, 0, ShaderProgram.TYPE_CONTAINER, 0, ShaderProgram.NAME_CONTAINER, 0);
			final String name = new String(ShaderProgram.NAME_CONTAINER, 0, ShaderProgram.LENGTH_CONTAINER[0]);
			final int location = GLES20.glGetUniformLocation(this.mProgramID, name);
			this.mUniformLocations.put(name, location);
		}
	}

	private void initAttributeLocations() {
		this.mAttributeLocations.clear();
		ShaderProgram.PARAMETERS_CONTAINER[0] = 0;
		GLES20.glGetProgramiv(this.mProgramID, GLES20.GL_ACTIVE_ATTRIBUTES, ShaderProgram.PARAMETERS_CONTAINER, 0);
		final int numAttributes = ShaderProgram.PARAMETERS_CONTAINER[0];

		for(int i = 0; i < numAttributes; i++) {
			GLES20.glGetActiveAttrib(this.mProgramID, i, ShaderProgram.NAME_CONTAINER_SIZE, ShaderProgram.LENGTH_CONTAINER, 0, ShaderProgram.SIZE_CONTAINER, 0, ShaderProgram.TYPE_CONTAINER, 0, ShaderProgram.NAME_CONTAINER, 0);
			final String name = new String(ShaderProgram.NAME_CONTAINER, 0, ShaderProgram.LENGTH_CONTAINER[0]);
			final int location = GLES20.glGetAttribLocation(this.mProgramID, name);
			this.mAttributeLocations.put(name, location);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
