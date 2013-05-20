package org.andengine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.data.constants.DataConstants;

import android.opengl.GLES20;

/**
 * Compared to a {@link HighPerformanceVertexBufferObject} or a {@link LowMemoryVertexBufferObject}, the {@link ZeroMemoryVertexBufferObject} uses <b><u>no</u> permanent heap memory</b>,
 * at the cost of expensive data buffering (<b>up to <u>5x</u> slower!</b>) whenever the bufferdata needs to be updated and higher GC activity, due to the temporary {@link ByteBuffer} allocations.
 * <p/>
 * Usually a {@link ZeroMemoryVertexBufferObject} is preferred to a {@link HighPerformanceVertexBufferObject} or a {@link LowMemoryVertexBufferObject} when the following conditions are met:
 * <ol>
 * <li>The application is close to run out of memory.</li>
 * <li>You have very big {@link HighPerformanceVertexBufferObject}/{@link LowMemoryVertexBufferObject} or an extreme number of small {@link HighPerformanceVertexBufferObject}/{@link LowMemoryVertexBufferObject}s, where you can't afford to have any of the bufferdata to be kept in heap memory.</li>
 * <li>The content (color, vertices, texturecoordinates) of the {@link ZeroMemoryVertexBufferObject} is changed not often, or even better: never.</li>
 * </ol>
 * <p/>
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 19:03:32 - 10.02.2012
 */
public abstract class ZeroMemoryVertexBufferObject implements IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mCapacity;
	protected final boolean mAutoDispose;
	protected final int mUsage;

	protected int mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	protected boolean mDirtyOnHardware = true;

	protected boolean mDisposed;

	protected final VertexBufferObjectManager mVertexBufferObjectManager;
	protected final VertexBufferObjectAttributes mVertexBufferObjectAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ZeroMemoryVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
		this.mCapacity = pCapacity;
		this.mUsage = pDrawType.getUsage();
		this.mAutoDispose = pAutoDispose;
		this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return this.mVertexBufferObjectManager;
	}

	@Override
	public boolean isDisposed() {
		return this.mDisposed;
	}

	@Override
	public boolean isAutoDispose() {
		return this.mAutoDispose;
	}

	@Override
	public int getHardwareBufferID() {
		return this.mHardwareBufferID;
	}

	@Override
	public boolean isLoadedToHardware() {
		return this.mHardwareBufferID != IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	}

	@Override
	public void setNotLoadedToHardware() {
		this.mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
		this.mDirtyOnHardware = true;
	}

	@Override
	public boolean isDirtyOnHardware() {
		return this.mDirtyOnHardware;
	}

	@Override
	public void setDirtyOnHardware() {
		this.mDirtyOnHardware = true;
	}

	@Override
	public int getCapacity() {
		return this.mCapacity;
	}

	@Override
	public int getByteCapacity() {
		return this.mCapacity * DataConstants.BYTES_PER_FLOAT;
	}

	@Override
	public int getHeapMemoryByteSize() {
		return 0;
	}

	@Override
	public int getNativeHeapMemoryByteSize() {
		return 0;
	}

	@Override
	public int getGPUMemoryByteSize() {
		if (this.isLoadedToHardware()) {
			return this.getByteCapacity();
		} else {
			return 0;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onPopulateBufferData(final ByteBuffer pByteBuffer);

	@Override
	public void bind(final GLState pGLState) {
		if (this.mHardwareBufferID == IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID) {
			this.loadToHardware(pGLState);
			this.mVertexBufferObjectManager.onVertexBufferObjectLoaded(this);
		}

		pGLState.bindArrayBuffer(this.mHardwareBufferID);

		if (this.mDirtyOnHardware) {
			ByteBuffer byteBuffer = null;
			try {
				byteBuffer = this.aquireByteBuffer();

				this.onPopulateBufferData(byteBuffer);

				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, byteBuffer.limit(), byteBuffer, this.mUsage);
			} finally {
				if (byteBuffer != null) {
					this.releaseByteBuffer(byteBuffer);
				}
			}

			this.mDirtyOnHardware = false;
		}
	}

	@Override
	public void bind(final GLState pGLState, final ShaderProgram pShaderProgram) {
		this.bind(pGLState);

		pShaderProgram.bind(pGLState, this.mVertexBufferObjectAttributes);
	}

	@Override
	public void unbind(final GLState pGLState, final ShaderProgram pShaderProgram) {
		pShaderProgram.unbind(pGLState);

		// pGLState.bindBuffer(0); // TODO Does this have an positive/negative impact on performance?
	}

	@Override
	public void unloadFromHardware(final GLState pGLState) {
		pGLState.deleteArrayBuffer(this.mHardwareBufferID);

		this.mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	}

	@Override
	public void draw(final int pPrimitiveType, final int pCount) {
		GLES20.glDrawArrays(pPrimitiveType, 0, pCount);
	}

	@Override
	public void draw(final int pPrimitiveType, final int pOffset, final int pCount) {
		GLES20.glDrawArrays(pPrimitiveType, pOffset, pCount);
	}

	@Override
	public void dispose() {
		if (!this.mDisposed) {
			this.mDisposed = true;

			this.mVertexBufferObjectManager.onUnloadVertexBufferObject(this);
		} else {
			throw new AlreadyDisposedException();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if (!this.mDisposed) {
			this.dispose();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void loadToHardware(final GLState pGLState) {
		this.mHardwareBufferID = pGLState.generateBuffer();
		this.mDirtyOnHardware = true;
	}

	/**
	 * When a non <code>null</code> {@link ByteBuffer} is returned by this function, it is guaranteed that {@link #releaseByteBuffer(ByteBuffer)} is called.
	 * @return a {@link ByteBuffer} to be passed to {@link #onPopulateBufferData(ByteBuffer)}.
	 */
	protected ByteBuffer aquireByteBuffer() {
		final ByteBuffer byteBuffer = BufferUtils.allocateDirectByteBuffer(this.getByteCapacity());
		byteBuffer.order(ByteOrder.nativeOrder());
		return byteBuffer;
	}

	protected void releaseByteBuffer(final ByteBuffer pByteBuffer) {
		BufferUtils.freeDirectByteBuffer(pByteBuffer);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
