package org.andengine.opengl.vbo;

import java.util.ArrayList;
import java.util.HashSet;

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

	private final HashSet<VertexBufferObject> mVertexObjectsManaged = new HashSet<VertexBufferObject>();

	private final ArrayList<VertexBufferObject> mVertexObjectsLoaded = new ArrayList<VertexBufferObject>();

	private final ArrayList<VertexBufferObject> mVertexObjectsToBeLoaded = new ArrayList<VertexBufferObject>();
	private final ArrayList<VertexBufferObject> mVertexObjectsToBeUnloaded = new ArrayList<VertexBufferObject>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public synchronized int getByteSize() {
		int byteSize = 0;
		final ArrayList<VertexBufferObject> loadedBufferObjects = this.mVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			byteSize += loadedBufferObjects.get(i).getByteCapacity();
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
		final ArrayList<VertexBufferObject> loadedBufferObjects = this.mVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		this.mVertexObjectsToBeLoaded.clear();
		this.mVertexObjectsLoaded.clear();
		this.mVertexObjectsManaged.clear();
	}

	public synchronized void loadBufferObject(final VertexBufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}

		if(this.mVertexObjectsManaged.contains(pBufferObject)) {
			/* Just make sure it doesn't get deleted. */
			this.mVertexObjectsToBeUnloaded.remove(pBufferObject);
		} else {
			this.mVertexObjectsManaged.add(pBufferObject);
			this.mVertexObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public synchronized void unloadBufferObject(final VertexBufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}

		if(this.mVertexObjectsManaged.contains(pBufferObject)) {
			if(this.mVertexObjectsLoaded.contains(pBufferObject)) {
				this.mVertexObjectsToBeUnloaded.add(pBufferObject);
			} else if(this.mVertexObjectsToBeLoaded.remove(pBufferObject)) {
				this.mVertexObjectsManaged.remove(pBufferObject);
			}
		}
	}

	public void loadBufferObjects(final VertexBufferObject... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			this.loadBufferObject(pBufferObjects[i]);
		}
	}

	public void unloadBufferObjects(final VertexBufferObject... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			this.unloadBufferObject(pBufferObjects[i]);
		}
	}

	public synchronized void onReload() {
		final ArrayList<VertexBufferObject> loadedBufferObjects = this.mVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		this.mVertexObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public synchronized void updateBufferObjects(final GLState pGLState) {
		final HashSet<VertexBufferObject> vertexBufferObjectsManaged = this.mVertexObjectsManaged;
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = this.mVertexObjectsLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeLoaded = this.mVertexObjectsToBeLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeUnloaded = this.mVertexObjectsToBeUnloaded;

		/* First load pending BufferObjects. */
		final int vertexBufferObjectToBeLoadedCount = vertexBufferObjectsToBeLoaded.size();

		if(vertexBufferObjectToBeLoadedCount > 0) {
			for(int i = vertexBufferObjectToBeLoadedCount - 1; i >= 0; i--) {
				final VertexBufferObject vertexBufferObjectToBeLoaded = vertexBufferObjectsToBeLoaded.get(i);
				if(!vertexBufferObjectToBeLoaded.isLoadedToHardware()) {
					vertexBufferObjectToBeLoaded.loadToHardware(pGLState);
					vertexBufferObjectToBeLoaded.setDirtyOnHardware();
				}
				vertexBufferObjectsLoaded.add(vertexBufferObjectToBeLoaded);
			}

			vertexBufferObjectsToBeLoaded.clear();
		}

		/* Then unload pending BufferObjects. */
		final int vertexBufferObjectsToBeUnloadedCount = vertexBufferObjectsToBeUnloaded.size();

		if(vertexBufferObjectsToBeUnloadedCount > 0){
			for(int i = vertexBufferObjectsToBeUnloadedCount - 1; i >= 0; i--){
				final IVertexBufferObject vertexBufferObjectToBeUnloaded = vertexBufferObjectsToBeUnloaded.remove(i);
				if(vertexBufferObjectToBeUnloaded.isLoadedToHardware()){
					vertexBufferObjectToBeUnloaded.unloadFromHardware(pGLState);
				}
				vertexBufferObjectsLoaded.remove(vertexBufferObjectToBeUnloaded);
				vertexBufferObjectsManaged.remove(vertexBufferObjectToBeUnloaded);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
