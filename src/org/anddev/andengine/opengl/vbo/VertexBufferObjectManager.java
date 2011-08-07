package org.anddev.andengine.opengl.vbo;

import java.util.ArrayList;
import java.util.HashSet;

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

	private static final HashSet<VertexBufferObject> mVertexObjectsManaged = new HashSet<VertexBufferObject>();

	private static final ArrayList<VertexBufferObject> mVertexObjectsLoaded = new ArrayList<VertexBufferObject>();

	private static final ArrayList<VertexBufferObject> mVertexObjectsToBeLoaded = new ArrayList<VertexBufferObject>();
	private static final ArrayList<VertexBufferObject> mVertexObjectsToBeUnloaded = new ArrayList<VertexBufferObject>();

	private static VertexBufferObjectManager mActiveInstance = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static VertexBufferObjectManager getActiveInstance() {
		return VertexBufferObjectManager.mActiveInstance;
	}

	public static void setActiveInstance(final VertexBufferObjectManager pActiveInstance) {
		VertexBufferObjectManager.mActiveInstance = pActiveInstance;
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

	public synchronized void clear() {
		VertexBufferObjectManager.mVertexObjectsToBeLoaded.clear();
		VertexBufferObjectManager.mVertexObjectsLoaded.clear();
		VertexBufferObjectManager.mVertexObjectsManaged.clear();
	}

	public synchronized void loadBufferObject(final VertexBufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}

		if(VertexBufferObjectManager.mVertexObjectsManaged.contains(pBufferObject)) {
			/* Just make sure it doesn't get deleted. */
			VertexBufferObjectManager.mVertexObjectsToBeUnloaded.remove(pBufferObject);
		} else {
			VertexBufferObjectManager.mVertexObjectsManaged.add(pBufferObject);
			VertexBufferObjectManager.mVertexObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public synchronized void unloadBufferObject(final VertexBufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}
		if(VertexBufferObjectManager.mVertexObjectsManaged.contains(pBufferObject)) {
			if(VertexBufferObjectManager.mVertexObjectsLoaded.contains(pBufferObject)) {
				VertexBufferObjectManager.mVertexObjectsToBeUnloaded.add(pBufferObject);
			} else if(VertexBufferObjectManager.mVertexObjectsToBeLoaded.remove(pBufferObject)) {
				VertexBufferObjectManager.mVertexObjectsManaged.remove(pBufferObject);
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

	public synchronized void reloadBufferObjects() {
		final ArrayList<VertexBufferObject> loadedBufferObjects = VertexBufferObjectManager.mVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		VertexBufferObjectManager.mVertexObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public synchronized void updateBufferObjects() {
		final HashSet<VertexBufferObject> vertexBufferObjectsManaged = VertexBufferObjectManager.mVertexObjectsManaged;
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = VertexBufferObjectManager.mVertexObjectsLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeLoaded = VertexBufferObjectManager.mVertexObjectsToBeLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeUnloaded = VertexBufferObjectManager.mVertexObjectsToBeUnloaded;

		/* First load pending BufferObjects. */
		final int vertexBufferObjectToBeLoadedCount = vertexBufferObjectsToBeLoaded.size();

		if(vertexBufferObjectToBeLoadedCount > 0) {
			for(int i = vertexBufferObjectToBeLoadedCount - 1; i >= 0; i--) {
				final VertexBufferObject vertexBufferObjectToBeLoaded = vertexBufferObjectsToBeLoaded.get(i);
				if(!vertexBufferObjectToBeLoaded.isLoadedToHardware()) {
					vertexBufferObjectToBeLoaded.loadToHardware();
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
				final VertexBufferObject vertexBufferObjectToBeUnloaded = vertexBufferObjectsToBeUnloaded.remove(i);
				if(vertexBufferObjectToBeUnloaded.isLoadedToHardware()){
					vertexBufferObjectToBeUnloaded.unloadFromHardware();
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
