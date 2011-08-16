package org.anddev.andengine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.BufferUtils;
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

	private static final int[] HARDWAREID_CONTAINER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private final float[] mBufferData;

	private final int mDrawType;

	private final ByteBuffer mByteBuffer;

	private int mHardwareBufferID = -1;
	private boolean mLoadedToHardware;
	private boolean mDirtyOnHardware = true;

	private boolean mManaged;

	private final VertexBufferObjectAttributes mVertexBufferObjectAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 */
	public VertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged) {
		this(pCapacity, pDrawType, pManaged, null);
	}

	/**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 * @param pVertexBufferObjectAttributes to be automatically enabled on the {@link ShaderProgram} used in {@link VertexBufferObject#bind(ShaderProgram)}.
	 */
	public VertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		this.mDrawType = pDrawType.getGLConstant();
		this.mManaged = pManaged;
		this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;
		this.mBufferData = new float[pCapacity];

		this.mByteBuffer = ByteBuffer.allocateDirect((pCapacity * GLHelper.BYTES_PER_FLOAT)).order(ByteOrder.nativeOrder());

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

	public float[] getBufferData() {
		return this.mBufferData;
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

	public boolean isDirtyOnHardware() {
		return this.mDirtyOnHardware;
	}

	public void setDirtyOnHardware() {
		this.mDirtyOnHardware = true;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void bind(final ShaderProgram pShaderProgram) {
		final int hardwareBufferID = this.mHardwareBufferID;
		if(hardwareBufferID == -1) {
			return;
		}

		GLHelper.bindBuffer(hardwareBufferID);

		if(this.mDirtyOnHardware) {
			this.mDirtyOnHardware = false;

			// TODO On honeycomb the nio buffers are significantly faster, and below native call might not be needed!
//			this.mFloatBuffer.position(0);
//			this.mFloatBuffer.put(this.mBufferData);
//			this.mFloatBuffer.position(0);

			BufferUtils.put(this.mByteBuffer, this.mBufferData, this.mBufferData.length, 0);
			this.mByteBuffer.position(0);

			synchronized(this) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.limit(), this.mByteBuffer, this.mDrawType);
			}
		}

		pShaderProgram.bind(this.mVertexBufferObjectAttributes);
	}

	public void unbind(final ShaderProgram pShaderProgram) {
		pShaderProgram.unbind(this.mVertexBufferObjectAttributes);
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
		VertexBufferObject.HARDWAREID_CONTAINER[0] = this.mHardwareBufferID;

		GLES20.glDeleteBuffers(1, VertexBufferObject.HARDWAREID_CONTAINER, 0);
	}

	private int generateHardwareBufferID() {
		GLES20.glGenBuffers(1, VertexBufferObject.HARDWAREID_CONTAINER, 0);

		return VertexBufferObject.HARDWAREID_CONTAINER[0];
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

		private final int mGLConstant;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		private DrawType(final int pGLConstant) {
			this.mGLConstant = pGLConstant;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getGLConstant() {
			return this.mGLConstant;
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