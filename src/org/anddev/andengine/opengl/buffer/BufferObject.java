package org.anddev.andengine.opengl.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.util.GLHelper;

/**
 * @author Nicolas Gramlich
 * @since 14:22:56 - 07.04.2010
 */
public abstract class BufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BYTES_PER_FLOAT = 4;

	private static final int[] HARDWAREBUFFERID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mByteCount;
	private final int mDrawType;

	private final FloatBuffer mFloatBuffer;

	private int mHardwareBufferID = -1;
	private boolean mLoadedToHardware;
	private boolean mHardwareBufferNeedsUpdate = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BufferObject(final int pByteCount, final int pDrawType) {
		this.mByteCount = pByteCount;
		this.mDrawType = pDrawType;

		this.mFloatBuffer = ByteBuffer.allocateDirect(pByteCount).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public FloatBuffer getFloatBuffer() {
		return this.mFloatBuffer;
	}

	public int getByteCount() {
		return this.mByteCount;
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
		final int hardwareBufferID = this.mHardwareBufferID;
		if(hardwareBufferID == -1) {
			return;
		}

		GLHelper.bindBuffer(pGL11, hardwareBufferID); // TODO Does this always need to be binded, or are just for buffers of the same 'type'(texture/vertex)?

		if(this.mHardwareBufferNeedsUpdate) {
			//			Debug.d("BufferObject.updating: ID = "  + this.mHardwareBufferID);
			this.mHardwareBufferNeedsUpdate = false;
			synchronized(this) {
				GLHelper.bufferData(pGL11, this, this.mDrawType);
			}
		}
	}

	public void loadToHardware(final GL11 pGL11) {
		this.mHardwareBufferID = this.generateHardwareBufferID(pGL11);
		//		Debug.d("BufferObject.loadToHardware(): ID = " + this.mHardwareBufferID);

		this.mLoadedToHardware = true;
	}

	public void unloadFromHardware(final GL11 pGL11) {
		this.deleteBufferOnHardware(pGL11);

		this.mHardwareBufferID = -1;

		this.mLoadedToHardware = false;
	}

	private void deleteBufferOnHardware(final GL11 pGL11) {
		GLHelper.deleteBuffer(pGL11, this.mHardwareBufferID);
	}

	private int generateHardwareBufferID(final GL11 pGL11) {
		pGL11.glGenBuffers(1, HARDWAREBUFFERID_FETCHER, 0);

		return HARDWAREBUFFERID_FETCHER[0];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
