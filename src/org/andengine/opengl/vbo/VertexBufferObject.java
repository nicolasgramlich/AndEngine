package org.andengine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.data.DataConstants;
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

	protected final int mUsage;

	protected final ByteBuffer mByteBuffer;

	protected int mHardwareBufferID = -1;
	protected boolean mLoadedToHardware;
	protected boolean mDirtyOnHardware = true;

	protected boolean mManaged;

	protected final VertexBufferObjectAttributes mVertexBufferObjectAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 * @param pVertexBufferObjectAttributes to be automatically enabled on the {@link ShaderProgram} used in {@link VertexBufferObject#bind(ShaderProgram)}.
	 */
	public VertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		this.mCapacity = pCapacity;
		this.mUsage = pDrawType.getUsage();
		this.mManaged = pManaged;
		this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;

		if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
			/* Honeycomb workaround for issue 16941. */
			this.mByteBuffer = BufferUtils.allocateDirect(pCapacity * DataConstants.BYTES_PER_FLOAT);
		} else {
			/* Other SDK versions. */
			this.mByteBuffer = ByteBuffer.allocateDirect(pCapacity * DataConstants.BYTES_PER_FLOAT);
		}
		this.mByteBuffer.order(ByteOrder.nativeOrder());

		if(pManaged) {
			this.loadToActiveBufferObjectManager();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public boolean isManaged() {
		return this.mManaged;
	}

	@Override
	public void setManaged(final boolean pManaged) {
		this.mManaged = pManaged;
	}

	@Override
	public int getHardwareBufferID() {
		return this.mHardwareBufferID;
	}

	@Override
	public boolean isLoadedToHardware() {
		return this.mLoadedToHardware;
	}

	void setLoadedToHardware(final boolean pLoadedToHardware) {
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
	public int getSize() {
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
	public void bind(final ShaderProgram pShaderProgram) {
		final int hardwareBufferID = this.mHardwareBufferID;
		if(hardwareBufferID == -1) {
			return;
		}

		GLState.bindBuffer(hardwareBufferID);

		if(this.mDirtyOnHardware) {
			this.mDirtyOnHardware = false;

			this.onBufferData();
		}

		pShaderProgram.bind(this.mVertexBufferObjectAttributes);
	}


	@Override
	public void unbind(final ShaderProgram pShaderProgram) {
		pShaderProgram.unbind(this.mVertexBufferObjectAttributes);

//		GLState.bindBuffer(0); // TODO Does this have an positive/negative impact on performance?
	}

	@Override
	public void loadToActiveBufferObjectManager() {
		VertexBufferObjectManager.loadBufferObject(this);
	}

	@Override
	public void unloadFromActiveBufferObjectManager() {
		VertexBufferObjectManager.unloadBufferObject(this);
	}

	@Override
	public void loadToHardware() {
		this.mHardwareBufferID = GLState.generateBuffer();

		this.mLoadedToHardware = true;
	}

	@Override
	public void unloadFromHardware() {
		GLState.deleteBuffer(this.mHardwareBufferID);

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