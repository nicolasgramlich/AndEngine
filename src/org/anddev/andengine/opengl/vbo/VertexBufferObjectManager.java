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

	private static final HashSet<VertexBufferObject> sVertexObjectsManaged = new HashSet<VertexBufferObject>();

	private static final ArrayList<VertexBufferObject> sVertexObjectsLoaded = new ArrayList<VertexBufferObject>();

	private static final ArrayList<VertexBufferObject> sVertexObjectsToBeLoaded = new ArrayList<VertexBufferObject>();
	private static final ArrayList<VertexBufferObject> sVertexObjectsToBeUnloaded = new ArrayList<VertexBufferObject>();

	// ===========================================================
	// Constructors
	// ===========================================================

	private VertexBufferObjectManager() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static synchronized int getSize() {
		int sum = 0;
		final ArrayList<VertexBufferObject> loadedBufferObjects = VertexBufferObjectManager.sVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			sum += loadedBufferObjects.get(i).getSize();
		}
		return sum;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void onCreate() {

	}

	public static synchronized void onDestroy() {
		final ArrayList<VertexBufferObject> loadedBufferObjects = VertexBufferObjectManager.sVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		VertexBufferObjectManager.sVertexObjectsToBeLoaded.clear();
		VertexBufferObjectManager.sVertexObjectsLoaded.clear();
		VertexBufferObjectManager.sVertexObjectsManaged.clear();
	}

	public static synchronized void loadBufferObject(final VertexBufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}

		if(VertexBufferObjectManager.sVertexObjectsManaged.contains(pBufferObject)) {
			/* Just make sure it doesn't get deleted. */
			VertexBufferObjectManager.sVertexObjectsToBeUnloaded.remove(pBufferObject);
		} else {
			VertexBufferObjectManager.sVertexObjectsManaged.add(pBufferObject);
			VertexBufferObjectManager.sVertexObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public static synchronized void unloadBufferObject(final VertexBufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}
		if(VertexBufferObjectManager.sVertexObjectsManaged.contains(pBufferObject)) {
			if(VertexBufferObjectManager.sVertexObjectsLoaded.contains(pBufferObject)) {
				VertexBufferObjectManager.sVertexObjectsToBeUnloaded.add(pBufferObject);
			} else if(VertexBufferObjectManager.sVertexObjectsToBeLoaded.remove(pBufferObject)) {
				VertexBufferObjectManager.sVertexObjectsManaged.remove(pBufferObject);
			}
		}
	}

	public static void loadBufferObjects(final VertexBufferObject... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			VertexBufferObjectManager.loadBufferObject(pBufferObjects[i]);
		}
	}

	public static void unloadBufferObjects(final VertexBufferObject... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			VertexBufferObjectManager.unloadBufferObject(pBufferObjects[i]);
		}
	}

	public static synchronized void onReload() {
		final ArrayList<VertexBufferObject> loadedBufferObjects = VertexBufferObjectManager.sVertexObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		VertexBufferObjectManager.sVertexObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public static synchronized void updateBufferObjects() {
		final HashSet<VertexBufferObject> vertexBufferObjectsManaged = VertexBufferObjectManager.sVertexObjectsManaged;
		final ArrayList<VertexBufferObject> vertexBufferObjectsLoaded = VertexBufferObjectManager.sVertexObjectsLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeLoaded = VertexBufferObjectManager.sVertexObjectsToBeLoaded;
		final ArrayList<VertexBufferObject> vertexBufferObjectsToBeUnloaded = VertexBufferObjectManager.sVertexObjectsToBeUnloaded;

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
				final IVertexBufferObject vertexBufferObjectToBeUnloaded = vertexBufferObjectsToBeUnloaded.remove(i);
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
