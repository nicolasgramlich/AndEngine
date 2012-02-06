package org.andengine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.DataConstants;
import org.andengine.util.system.SystemUtils;

import android.opengl.GLES20;
import android.os.Build;

/**
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

	protected int mHardwareBufferID = -1;
	protected boolean mLoadedToHardware;
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

		if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
			/* Honeycomb workaround for issue 16941. */
			this.mByteBuffer = BufferUtils.allocateDirect(pCapacity * DataConstants.BYTES_PER_FLOAT);
		} else {
			/* Other SDK versions. */
			this.mByteBuffer = ByteBuffer.allocateDirect(pCapacity * DataConstants.BYTES_PER_FLOAT);
		}
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
		return this.mLoadedToHardware;
	}

	@Override
	public void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
	}

	@Override
	public boolean isDirtyOnHardware() {
		return this.mDirtyOnHardware;
	}

	/** Mark this {@link VertexBufferObject} dirty so it gets updated on the hardware. */
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onBufferData();

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void bind(final GLState pGLState, final ShaderProgram pShaderProgram) {
		if(!this.mLoadedToHardware) {
			this.loadToHardware(pGLState);
			this.mVertexBufferObjectManager.onVertexBufferObjectLoaded(this);
			this.mDirtyOnHardware = true;
		}

		pGLState.bindBuffer(this.mHardwareBufferID);

		if(this.mDirtyOnHardware) {
			this.mDirtyOnHardware = false;

			this.onBufferData();
		}

		pShaderProgram.bind(pGLState, this.mVertexBufferObjectAttributes);
	}


	@Override
	public void unbind(final GLState pGLState, final ShaderProgram pShaderProgram) {
		pShaderProgram.unbind(pGLState);

//		pGLState.bindBuffer(0); // TODO Does this have an positive/negative impact on performance?
	}

	@Override
	public void loadToHardware(final GLState pGLState) {
		this.mHardwareBufferID = pGLState.generateBuffer();

		this.mLoadedToHardware = true;
	}

	@Override
	public void unloadFromHardware(final GLState pGLState) {
		pGLState.deleteBuffer(this.mHardwareBufferID);

		this.mHardwareBufferID = -1;
		this.mLoadedToHardware = false;
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

			/* Cleanup due to 'Honeycomb workaround for issue 16941' in constructor. */
			if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
				BufferUtils.freeDirect(this.mByteBuffer);
			}
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
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum DrawType {
		// ===========================================================
		// Elements
		// ===========================================================

		STATIC(GLES20.GL_STATIC_DRAW),
		DYNAMIC(GLES20.GL_DYNAMIC_DRAW),
		STREAM(GLES20.GL_STREAM_DRAW);

		// ===========================================================
		// Constants
		// ===========================================================

		private final int mUsage;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		private DrawType(final int pUsage) {
			this.mUsage = pUsage;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getUsage() {
			return this.mUsage;
		}

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