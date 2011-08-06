package org.anddev.andengine.opengl.vbo;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:22:56 - 07.04.2010
 */
public class VertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] CONTAINER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int[] mBufferData;

	private final int mDrawType;

	protected final FastFloatBuffer mFloatBuffer;

	private int mHardwareBufferID = -1;
	private boolean mLoadedToHardware;
	private boolean mDirty = true;

	private boolean mManaged;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 */
	public VertexBufferObject(final int pCapacity, final int pDrawType, final boolean pManaged) {
		this.mDrawType = pDrawType;
		this.mManaged = pManaged;
		this.mBufferData = new int[pCapacity];
		this.mFloatBuffer = new FastFloatBuffer(pCapacity);

		if(pManaged) {
			this.loadToActiveBufferObjectManager();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isManaged() {
		return this.mManaged;
	}

	public void setManaged(final boolean pManaged) {
		this.mManaged = pManaged;
	}

	public int[] getBufferData() {
		return this.mBufferData;
	}

	public FastFloatBuffer getFloatBuffer() {
		return this.mFloatBuffer;
	}

	public int getHardwareBufferID() {
		return this.mHardwareBufferID;
	}

	public boolean isLoadedToHardware() {
		return this.mLoadedToHardware;
	}

	void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void setDirty(){
		this.mDirty = true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void bind(final ShaderProgram pShaderProgram) {
		pShaderProgram.bind();

		// TODO pShader.enableVertexAttributes
		// TODO pShader.setVertexAttributes
		
		final int hardwareBufferID = this.mHardwareBufferID;
		if(hardwareBufferID == -1) {
			return;
		}

		GLHelper.bindBuffer(hardwareBufferID);

		if(this.mDirty) {
			this.mDirty = false;
			synchronized(this) {
				GLHelper.bufferData(this.mFloatBuffer.mByteBuffer, this.mDrawType);
			}
		}
	}

	public void unbind(final ShaderProgram pShaderProgram) {
		pShaderProgram.unbind();
//		TODO Use: pShader.disableVertexAttributes
	}

	public void loadToActiveBufferObjectManager() {
		VertexBufferObjectManager.getActiveInstance().loadBufferObject(this);
	}

	public void unloadFromActiveBufferObjectManager() {
		VertexBufferObjectManager.getActiveInstance().unloadBufferObject(this);
	}

	public void loadToHardware() {
		this.mHardwareBufferID = this.generateHardwareBufferID();

		this.mLoadedToHardware = true;
	}

	public void unloadFromHardware() {
		this.deleteBufferOnHardware();

		this.mHardwareBufferID = -1;
		this.mLoadedToHardware = false;
	}

	private void deleteBufferOnHardware() {
		VertexBufferObject.CONTAINER[0] = this.mHardwareBufferID;

		GLES20.glDeleteBuffers(1, VertexBufferObject.CONTAINER, 0);
	}

	private int generateHardwareBufferID() {
		GLES20.glGenBuffers(1, VertexBufferObject.CONTAINER, 0);

		return VertexBufferObject.CONTAINER[0];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
