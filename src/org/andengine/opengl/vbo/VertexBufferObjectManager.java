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

	private final ArrayList<VertexBufferObject> mVertexBufferObjectsLoaded = new ArrayList<VertexBufferObject>();

	private final ArrayList<VertexBufferObject> mVertexBufferObjectsToBeUnloaded = new ArrayList<VertexBufferObject>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public synchronized int getByteSize() {
		int byteSize = 0;
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			byteSize += vertexBufferObjectsLoaded.get(i).getByteCapacity();
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
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			vertexBufferObjectsLoaded.get(i).setLoadedToHardware(false);
		}

		this.mVertexBufferObjectsLoaded.clear();
	}

	public synchronized void onVertexBufferObjectLoaded(final VertexBufferObject pVertexBufferObject) {
		this.mVertexBufferObjectsLoaded.add(pVertexBufferObject);
	}

	public synchronized void onUnloadVertexBufferObject(final VertexBufferObject pVertexBufferObject) {
		if(this.mVertexBufferObjectsLoaded.remove(pVertexBufferObject)) {
			this.mVertexBufferObjectsToBeUnloaded.add(pVertexBufferObject);
		}
	}

	public synchronized void onReload() {
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
			vertexBufferObjectsLoaded.get(i).setLoadedToHardware(false);
		}

		vertexBufferObjectsLoaded.clear();
	}

	public synchronized void updateVertexBufferObjects(final GLState pGLState) {
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = this.mVertexBufferObjectsLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeUnloaded = this.mVertexBufferObjectsToBeUnloaded;

		/* Unload pending VertexBufferObjects. */
		for(int i = vertexBufferObjectsToBeUnloaded.size() - 1; i >= 0; i--){
			final IVertexBufferObject vertexBufferObjectToBeUnloaded = vertexBufferObjectsToBeUnloaded.remove(i);
			if(vertexBufferObjectToBeUnloaded.isLoadedToHardware()){
				vertexBufferObjectToBeUnloaded.unloadFromHardware(pGLState);
			}
			vertexBufferObjectsLoaded.remove(vertexBufferObjectToBeUnloaded);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
