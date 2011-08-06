package org.anddev.andengine.opengl.shader;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:56:34 - 05.08.2011
 */
public class ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] CONTAINER = new int[1]; // TODO Useful name...

	// ===========================================================
	// Fields
	// ===========================================================

	protected final String mVertexShaderSource;
	protected final String mFragmentShaderSource;

	protected int mVertexShaderID;
	protected int mFragmentShaderID;
	protected int mProgramID;

	protected boolean mCompiled;

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
		GLES20.glUseProgram(0); // TODO Needed?
	}

	protected void compile() throws ShaderProgramException {
		this.mVertexShaderID = ShaderProgram.compileShader(this.mVertexShaderSource, GLES20.GL_VERTEX_SHADER);
		this.mFragmentShaderID = ShaderProgram.compileShader(this.mFragmentShaderSource, GLES20.GL_FRAGMENT_SHADER);

		this.mProgramID = GLES20.glCreateProgram();
		GLES20.glAttachShader(this.mProgramID, this.mVertexShaderID);
		GLES20.glAttachShader(this.mProgramID, this.mFragmentShaderID);

		// TODO AttributeIDs before linking?

		this.link();
	}

	protected void link() {
		GLES20.glLinkProgram(this.mProgramID);

		GLES20.glGetProgramiv(this.mProgramID, GLES20.GL_LINK_STATUS, ShaderProgram.CONTAINER, 0);
		if(ShaderProgram.CONTAINER[0] == 0) {
			throw new ShaderProgramException(GLES20.glGetProgramInfoLog(this.mProgramID));
		}

		this.mCompiled = true;
	}

	private static int compileShader(final String pSource, final int pType) throws ShaderProgramException {
		final int shaderID = GLES20.glCreateShader(pType);
		if(shaderID == 0) {
			throw new ShaderProgramException("Could not create Shader of type: '" + pType + '"');
		}

		GLES20.glShaderSource(shaderID, pSource);
		GLES20.glCompileShader(shaderID);

		GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, ShaderProgram.CONTAINER, 0);
		if(ShaderProgram.CONTAINER[0] == 0) {
			throw new ShaderProgramException(GLES20.glGetShaderInfoLog(shaderID));
		}
		return shaderID;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ShaderProgramException extends RuntimeException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 2377158902169370516L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public ShaderProgramException(final String pMessage) {
			super(pMessage);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
