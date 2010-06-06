package org.anddev.andengine.opengl.buffer;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.GLHelper;

/**
 * @author Nicolas Gramlich
 * @since 14:22:56 - 07.04.2010
 */
public abstract class BufferObject extends BaseBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWAREBUFFERID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private int mHardwareBufferID = -1;

	private boolean mLoadedToHardware;

	private final int mDrawType;

	private boolean mHardwareBufferNeedsUpdate = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BufferObject(final int pByteCount, final int pDrawType) {
		super(pByteCount);
		this.mDrawType = pDrawType;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getHardwareBufferID() {
		return this.mHardwareBufferID;
	}

	public boolean isLoadedToHardware() {
		return this.mLoadedToHardware;
	}

	void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
	}

	public void setHardwareBufferNeedsUpdate(final boolean pHardwareBufferNeedsUpdate) {
		this.mHardwareBufferNeedsUpdate = pHardwareBufferNeedsUpdate;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void update(){
		this.mHardwareBufferNeedsUpdate = true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void selectOnHardware(final GL11 pGL11) {
		if(this.mHardwareBufferNeedsUpdate && this.mHardwareBufferID != -1) {
//			Debug.d("BufferObject.updating: ID = "  + this.mHardwareBufferID);
			this.mHardwareBufferNeedsUpdate = false;

			GLHelper.bindBuffer(pGL11, this.mHardwareBufferID);
			GLHelper.bufferData(pGL11, this, this.mDrawType);
		}

		GLHelper.bindBuffer(pGL11, this.mHardwareBufferID); // TODO Muss der immer gebindet werden oder gibts da quasi je immer einen für Texture/Vertex etc...
	}

	public void loadToHardware(final GL11 pGL11) {
		this.mHardwareBufferID = this.generateHardwareBufferID(pGL11);
//		Debug.d("BufferObject.loadToHardware(): ID = " + this.mHardwareBufferID);

		this.mLoadedToHardware = true;
	}

	private int generateHardwareBufferID(final GL11 pGL11) {
		pGL11.glGenBuffers(1, HARDWAREBUFFERID_FETCHER, 0);

		return HARDWAREBUFFERID_FETCHER[0];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
