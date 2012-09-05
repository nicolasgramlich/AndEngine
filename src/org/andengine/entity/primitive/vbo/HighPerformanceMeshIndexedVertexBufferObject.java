package org.andengine.entity.primitive.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.vbo.IMeshVertexBufferObject;
import org.andengine.opengl.exception.GLException;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.DataConstants;
import org.andengine.util.system.SystemUtils;

import android.annotation.TargetApi;
import android.opengl.GLES20;

public class HighPerformanceMeshIndexedVertexBufferObject extends
		HighPerformanceVertexBufferObject  implements IMeshVertexBufferObject {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mIndexCapacity;
	protected short[] mIndexData;
	
	protected final ByteBuffer mIndexBuffer;
	protected final ShortBuffer mShortBuffer;
	
	protected int mHardwareIndexBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	
	protected boolean mIndexDirtyOnHardware = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceMeshIndexedVertexBufferObject(
			VertexBufferObjectManager pVertexBufferObjectManager,
			float[] pVertexData, short[] pIndexData, int pIndexCount, DrawType pDrawType,
			boolean pAutoDispose, VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pVertexData, pDrawType, pAutoDispose,
				pVertexBufferObjectAttributes);

		mIndexData = pIndexData;
		mIndexCapacity = pIndexCount;
		
		mIndexBuffer = BufferUtils.allocateDirectByteBuffer(pIndexCount * DataConstants.BYTES_PER_SHORT);
		this.mIndexBuffer.order(ByteOrder.nativeOrder());
		
		if(SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
			this.mShortBuffer = this.mIndexBuffer.asShortBuffer();
		} else {
			this.mShortBuffer = null;
		}
	}
	
	public HighPerformanceMeshIndexedVertexBufferObject(
			VertexBufferObjectManager pVertexBufferObjectManager,
			int pVertexCount, int pIndexCount, DrawType pDrawType,
			boolean pAutoDispose, VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pVertexCount, pDrawType, pAutoDispose,
				pVertexBufferObjectAttributes);

		mIndexData = new short[pIndexCount];
		mIndexCapacity = pIndexCount;
		
		mIndexBuffer = BufferUtils.allocateDirectByteBuffer(pIndexCount * DataConstants.BYTES_PER_SHORT);
		this.mIndexBuffer.order(ByteOrder.nativeOrder());
		
		if(SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
			this.mShortBuffer = this.mIndexBuffer.asShortBuffer();
		} else {
			this.mShortBuffer = null;
		}
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getIndexCapacity() {
		return this.mIndexCapacity;
	}
	
	public int getIndexByteCapacity() {
		return this.mIndexBuffer.capacity();
	}
	
	public short[] getIndexBufferData() {
		return this.mIndexData;
	}

	public int getHardwareIndexBufferID() {
		return this.mHardwareIndexBufferID;
	}

	@Override
	public boolean isLoadedToHardware() {
		return super.isLoadedToHardware() && this.mHardwareIndexBufferID != IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	}

	@Override
	public void setNotLoadedToHardware() {
		this.mHardwareIndexBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
		super.setNotLoadedToHardware();
	}
	
	public void setIndexDirtyOnHardware() {
		mIndexDirtyOnHardware = true;
	}
	
	public boolean getIndexDirtyOnHardware() {
		return mIndexDirtyOnHardware;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onBufferIndexData() {
		// TODO Check if, and how mow this condition affects performance.
		if(SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
		// TODO Check if this is similar fast or faster than the non Honeycomb codepath.
			this.mShortBuffer.position(0);
			this.mShortBuffer.put(this.mIndexData);

			GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.mIndexBuffer.capacity(), this.mIndexBuffer, this.mUsage);
		} else {
			put(this.mIndexBuffer, this.mIndexData, this.mIndexData.length, 0);
			
			GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.mIndexBuffer.limit(), this.mIndexBuffer, this.mUsage);
		}
	}

	@Override
	public void bind(GLState pGLState) {
		if(this.mHardwareIndexBufferID == IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID) {
			this.loadIndexBufferToHardware(pGLState);
		}
		
		pGLState.bindIndexBuffer(mHardwareIndexBufferID);
		
		if(mIndexDirtyOnHardware) {
			this.onBufferIndexData();
			mIndexDirtyOnHardware = false;
		}
		
		super.bind(pGLState);
	}

	@TargetApi(9)
	@Override
	public void draw(final int pPrimitiveType, final int pCount) {
		this.draw(pPrimitiveType, 0, pCount);
	}

	@TargetApi(9)
	@Override
	public void draw(final int pPrimitiveType, final int pOffset, final int pCount) {
		if(SystemUtils.SDK_VERSION_GINGERBREAD_OR_LATER)
			GLES20.glDrawElements(pPrimitiveType, pCount, GLES20.GL_UNSIGNED_SHORT, pOffset);
		else
			throw new GLException(GLES20.GL_INVALID_OPERATION, "glDrawElements is not supported in version before Gingerbread (target = 9).");
	}
	
	@Override
	public void onUpdateColor(Mesh pMesh) {
		/*Leaves the management of the data to the caller*/
		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(Mesh pMesh) {
		/*Leaves the management of the data to the caller*/
		this.setDirtyOnHardware();
	}
	
	public void onUpdateIndexes(Mesh pMesh) {
		/*Leaves the management of the data to the caller*/
		this.setIndexDirtyOnHardware();
	}

	@Override
	public void unloadFromHardware(final GLState pGLState) {
		pGLState.deleteIndexBuffer(this.mHardwareIndexBufferID);

		this.mHardwareIndexBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
		
		super.unloadFromHardware(pGLState);
	}

	@Override
	public void dispose() {
		super.dispose();
		BufferUtils.freeDirectByteBuffer(mIndexBuffer);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int loadIndexBufferToHardware(final GLState pGLState) {
		mHardwareIndexBufferID = pGLState.generateBuffer();
		this.setIndexDirtyOnHardware();
		return mHardwareIndexBufferID;
	}

	public static void put(final ByteBuffer pByteBuffer, final short[] pSource, final int pLength, final int pOffset) {
		for(int i = pOffset; i < (pOffset + pLength); i++) {
			pByteBuffer.putShort(pSource[i]);
		}

		pByteBuffer.position(0);
		pByteBuffer.limit(pLength << 1); // is this equivalent to pLenght * 2 ? (2 = bites per short)
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
