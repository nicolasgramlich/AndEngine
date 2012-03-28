package org.andengine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.DataConstants;

import android.opengl.GLES20;

/**
 * TODO Extract a common base class from {@link VertexBufferObject} and {@link ZeroMemoryVertexBufferObject} (due to significant code duplication).
 * 		For naming, maybe be inspired by the java ByteBuffer naming (i.e. HeapBackedFloatArrayVertexBufferObject, StreamBufferVertexBufferObject, SharedBufferStreamVertexBufferObject).  
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:22:56 - 07.04.2010
 */
public abstract class VertexBufferObject implements IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mCapacity;
	protected final boolean mAutoDispose;
	protected final int mUsage;
	protected final ByteBuffer mByteBuffer;

	protected int mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	protected boolean mDirtyOnHardware = true;

	protected boolean mDisposed;

	protected final VertexBufferObjectManager mVertexBufferObjectManager;
	protected final VertexBufferObjectAttributes mVertexBufferObjectAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pVertexBufferObjectManager
	 * @param pCapacity
	 * @param pDrawType
	 * @param pAutoDispose when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 * @param pVertexBufferObjectAttributes to be automatically enabled on the {@link ShaderProgram} used in {@link VertexBufferObject#bind(ShaderProgram)}.
	 */
	public VertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
		this.mCapacity = pCapacity;
		this.mUsage = pDrawType.getUsage();
		this.mAutoDispose = pAutoDispose;
		this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;

		this.mByteBuffer = BufferUtils.allocateDirectByteBuffer(pCapacity * DataConstants.BYTES_PER_FLOAT);

		this.mByteBuffer.order(ByteOrder.nativeOrder());
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
		return this.mByteBuffer.capacity();
	}

	@Override
	public int getGPUMemoryByteSize() {
		if(this.isLoadedToHardware()) {
			return this.getByteCapacity();
		} else {
			return 0;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onBufferData();

	@Override
	public void bind(final GLState pGLState) {
		if(this.mHardwareBufferID == IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID) {
			this.loadToHardware(pGLState);

			this.mVertexBufferObjectManager.onVertexBufferObjectLoaded(this);
		}

		pGLState.bindArrayBuffer(this.mHardwareBufferID);

		if(this.mDirtyOnHardware) {
			this.onBufferData();

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

//		pGLState.bindBuffer(0); // TODO Does this have an positive/negative impact on performance?
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
		if(!this.mDisposed) {
			this.mDisposed = true;

			this.mVertexBufferObjectManager.onUnloadVertexBufferObject(this);

			BufferUtils.freeDirectByteBuffer(this.mByteBuffer);
		} else {
			throw new AlreadyDisposedException();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if(!this.mDisposed) {
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
