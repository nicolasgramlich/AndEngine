package org.anddev.andengine.opengl.buffer;

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
public abstract class BufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWAREBUFFERID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int[] mBufferData;

	private final int mDrawType;

	protected final FastFloatBuffer mFloatBuffer;

	private int mHardwareBufferID = -1;
	private boolean mLoadedToHardware;
	private boolean mHardwareBufferNeedsUpdate = true;

	private boolean mManaged;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link BufferObject} loads itself to the active {@link BufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 */
	public BufferObject(final int pCapacity, final int pDrawType, final boolean pManaged) {
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

	public void setHardwareBufferNeedsUpdate(){
		this.mHardwareBufferNeedsUpdate = true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void selectOnHardware() { // TODO Rename to bind, parameter: ShaderProgram
		final int hardwareBufferID = this.mHardwareBufferID;
		if(hardwareBufferID == -1) {
			return;
		}

		GLHelper.bindBuffer(hardwareBufferID); // TODO Does this always need to be bound, or are just for buffers of the same 'type'(texture/vertex)?

		if(this.mHardwareBufferNeedsUpdate) {
			this.mHardwareBufferNeedsUpdate = false;
			synchronized(this) {
				GLHelper.bufferData(this.mFloatBuffer.mByteBuffer, this.mDrawType);
			}
		}
		
		// TODO pShader.enableVertexAttributes
		// TODO pShader.setVertexAttributes
	}
	
	public void unbind() { // TODO Use, parameter: ShaderProgram
//		TODO Use: pShader.disableVertexAttributes
	}

	public void loadToActiveBufferObjectManager() {
		BufferObjectManager.getActiveInstance().loadBufferObject(this);
	}

	public void unloadFromActiveBufferObjectManager() {
		BufferObjectManager.getActiveInstance().unloadBufferObject(this);
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
		GLHelper.deleteBuffer(this.mHardwareBufferID);
	}

	private int generateHardwareBufferID() {
		GLES20.glGenBuffers(1, HARDWAREBUFFERID_FETCHER, 0);

		return HARDWAREBUFFERID_FETCHER[0];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
