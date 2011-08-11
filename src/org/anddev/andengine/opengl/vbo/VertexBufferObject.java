package org.anddev.andengine.opengl.vbo;

import org.anddev.andengine.opengl.GLES20Fix;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.constants.DataConstants;
import org.xml.sax.DTDHandler;

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

	private final int[] mBufferData;

	private final int mDrawType;

	private final FastFloatBuffer mFloatBuffer;

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
	public VertexBufferObject(final int pCapacity, final int pDrawType, final boolean pManaged) {
		this(pCapacity, pDrawType, pManaged, null);
	}

	/**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 * @param pVertexBufferObjectAttributes to be automatically enabled on the {@link ShaderProgram} used in {@link VertexBufferObject#bind(ShaderProgram)}.
	 */
	public VertexBufferObject(final int pCapacity, final int pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		this.mDrawType = pDrawType;
		this.mManaged = pManaged;
		this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;
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

			this.mFloatBuffer.position(0);
			this.mFloatBuffer.put(this.mBufferData);
			this.mFloatBuffer.position(0);

			synchronized(this) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mFloatBuffer.mByteBuffer.capacity(), this.mFloatBuffer.mByteBuffer, this.mDrawType);
			}
		}

		pShaderProgram.bind();

		this.enableVertexBufferObjectAttributes(pShaderProgram);
	}

	public void unbind(final ShaderProgram pShaderProgram) {
		this.disableVertexBufferObjectAttributes(pShaderProgram);

		pShaderProgram.unbind();
	}

	protected void enableVertexBufferObjectAttributes(final ShaderProgram pShaderProgram) {
		this.mVertexBufferObjectAttributes.enableVertexBufferObjectAttributes(pShaderProgram);
	}

	protected void disableVertexBufferObjectAttributes(final ShaderProgram pShaderProgram) {
		this.mVertexBufferObjectAttributes.disableVertexBufferObjectAttributes(pShaderProgram);
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

	public static class VertexBufferObjectAttribute {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int LOCATION_INVALID = -1;

		// ===========================================================
		// Fields
		// ===========================================================

		private int mLocation = LOCATION_INVALID;

		private final String mName;
		private final int mSize;
		private final int mType;
		private final boolean mNormalized;
		private final int mOffset;

		// ===========================================================
		// Constructors
		// ===========================================================

		public VertexBufferObjectAttribute(final String pName, final int pSize, final int pType, final boolean pNormalized, final int pOffset) {
			this.mName = pName;
			this.mSize = pSize;
			this.mType = pType;
			this.mNormalized = pNormalized;
			this.mOffset = pOffset;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public void enable(final ShaderProgram pShaderProgram, final int pStride) {
			if(this.mLocation == VertexBufferObjectAttribute.LOCATION_INVALID) {
				this.mLocation = pShaderProgram.getAttributeLocation(this.mName);
			}

			GLES20.glEnableVertexAttribArray(this.mLocation);
			GLES20Fix.glVertexAttribPointer(this.mLocation, this.mSize, this.mType, this.mNormalized, pStride, this.mOffset);
		}

		public void disable(final ShaderProgram pShaderProgram) {
			GLES20.glDisableVertexAttribArray(this.mLocation);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class VertexBufferObjectAttributes {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mStride;
		private final VertexBufferObjectAttribute[] mVertexBufferObjectAttributes;

		// ===========================================================
		// Constructors
		// ===========================================================

		public VertexBufferObjectAttributes(final int pStride, final VertexBufferObjectAttribute ... pVertexBufferObjectAttributes) {
			this.mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;
			this.mStride = pStride;
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

		public void enableVertexBufferObjectAttributes(final ShaderProgram pShaderProgram) {
			final VertexBufferObjectAttribute[] vertexBufferObjectAttributes = this.mVertexBufferObjectAttributes;
			if(vertexBufferObjectAttributes != null) {
				final int vertexBuggerObjectAttributeCount = vertexBufferObjectAttributes.length;
				for(int i = 0; i < vertexBuggerObjectAttributeCount; i++) {
					vertexBufferObjectAttributes[i].enable(pShaderProgram, this.mStride);
				}
			}
		}

		public void disableVertexBufferObjectAttributes(final ShaderProgram pShaderProgram) {
			final VertexBufferObjectAttribute[] vertexBufferObjectAttributes = this.mVertexBufferObjectAttributes;
			if(vertexBufferObjectAttributes != null) {
				final int vertexBuggerObjectAttributeCount = vertexBufferObjectAttributes.length;
				for(int i = 0; i < vertexBuggerObjectAttributeCount; i++) {
					vertexBufferObjectAttributes[i].disable(pShaderProgram);
				}
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class VertexBufferObjectAttributesBuilder {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private int mIndex;
		private final VertexBufferObjectAttribute[] mVertexBufferObjectAttributes;

		private int mOffset;

		// ===========================================================
		// Constructors
		// ===========================================================

		public VertexBufferObjectAttributesBuilder(final int pCount) {
			this.mVertexBufferObjectAttributes = new VertexBufferObjectAttribute[pCount];
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

		public VertexBufferObjectAttributesBuilder add(final String pName, final int pSize, final int pType, final boolean pNormalized) {
			this.mVertexBufferObjectAttributes[this.mIndex] = new VertexBufferObjectAttribute(pName, pSize, pType, pNormalized, this.mOffset);

			switch(pType) {
				case GLES20.GL_FLOAT:
					this.mOffset += pSize * DataConstants.BYTES_PER_FLOAT;
					break;
				case GLES20.GL_UNSIGNED_BYTE:
					this.mOffset += pSize * DataConstants.BYTES_PER_BYTE;
					break;
				default:
					throw new IllegalArgumentException("Unexpected pType: '" + pType + "'.");
			}
			this.mIndex++;
			return this;
		}

		public VertexBufferObjectAttributes build() {
			return new VertexBufferObjectAttributes(this.mOffset, this.mVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
