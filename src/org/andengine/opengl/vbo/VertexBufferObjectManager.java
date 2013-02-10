package org.andengine.opengl.vbo;

import java.util.ArrayList;

import org.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 17:48:46 - 08.03.2010
 */
public class VertexBufferObjectManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<IVertexBufferObject> mVertexBufferObjectsLoaded = new ArrayList<IVertexBufferObject>();

	private final ArrayList<IVertexBufferObject> mVertexBufferObjectsToBeUnloaded = new ArrayList<IVertexBufferObject>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public synchronized int getHeapMemoryByteSize() {
		int byteSize = 0;
		final ArrayList<IVertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for (int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			byteSize += vertexBufferObjectsLoaded.get(i).getHeapMemoryByteSize();
		}
		return byteSize;
	}

	public synchronized int getNativeHeapMemoryByteSize() {
		int byteSize = 0;
		final ArrayList<IVertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for (int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			byteSize += vertexBufferObjectsLoaded.get(i).getNativeHeapMemoryByteSize();
		}
		return byteSize;
	}

	public synchronized int getGPUHeapMemoryByteSize() {
		int byteSize = 0;
		final ArrayList<IVertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for (int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			byteSize += vertexBufferObjectsLoaded.get(i).getGPUMemoryByteSize();
		}
		return byteSize;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onCreate() {

	}

	public synchronized void onDestroy() {
		final ArrayList<IVertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for (int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			vertexBufferObjectsLoaded.get(i).setNotLoadedToHardware();
		}

		vertexBufferObjectsLoaded.clear();
	}

	public synchronized void onVertexBufferObjectLoaded(final IVertexBufferObject pVertexBufferObject) {
		this.mVertexBufferObjectsLoaded.add(pVertexBufferObject);
	}

	public synchronized void onUnloadVertexBufferObject(final IVertexBufferObject pVertexBufferObject) {
		if (this.mVertexBufferObjectsLoaded.remove(pVertexBufferObject)) {
			this.mVertexBufferObjectsToBeUnloaded.add(pVertexBufferObject);
		}
	}

	public synchronized void onReload() {
		final ArrayList<IVertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for (int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			vertexBufferObjectsLoaded.get(i).setNotLoadedToHardware();
		}

		vertexBufferObjectsLoaded.clear();
	}

	public synchronized void updateVertexBufferObjects(final GLState pGLState) {
		final ArrayList<IVertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		final ArrayList<IVertexBufferObject> vertexBufferObjectsToBeUnloaded = this.mVertexBufferObjectsToBeUnloaded;

		/* Unload pending VertexBufferObjects. */
		for (int i = vertexBufferObjectsToBeUnloaded.size() - 1; i >= 0; i--) {
			final IVertexBufferObject vertexBufferObjectToBeUnloaded = vertexBufferObjectsToBeUnloaded.remove(i);
			if (vertexBufferObjectToBeUnloaded.isLoadedToHardware()) {
				vertexBufferObjectToBeUnloaded.unloadFromHardware(pGLState);
			}
			vertexBufferObjectsLoaded.remove(vertexBufferObjectToBeUnloaded);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
