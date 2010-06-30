package org.anddev.andengine.opengl.buffer;

import java.util.ArrayList;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL11;

/**
 * @author Nicolas Gramlich
 * @since 17:48:46 - 08.03.2010
 */
public class BufferObjectManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static final ArrayList<BufferObject> mBufferObjectsToBeLoaded = new ArrayList<BufferObject>();
	private static final ArrayList<BufferObject> mLoadedBufferObjects = new ArrayList<BufferObject>();
	
	private static final HashSet<BufferObject> mManagedBufferObjects = new HashSet<BufferObject>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void clear() {
		BufferObjectManager.mBufferObjectsToBeLoaded.clear();
		BufferObjectManager.mLoadedBufferObjects.clear();
		BufferObjectManager.mManagedBufferObjects.clear();
	}

	public static void loadBufferObject(final BufferObject pBufferObject) {
		if(BufferObjectManager.mManagedBufferObjects.contains(pBufferObject) == false){
			BufferObjectManager.mManagedBufferObjects.add(pBufferObject);
			BufferObjectManager.mBufferObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public static void loadBufferObjects(final BufferObject ... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			BufferObjectManager.mBufferObjectsToBeLoaded.add(pBufferObjects[i]);
		}
	}

	public static void reloadBufferObjects() {
		final ArrayList<BufferObject> loadedBufferObjects = BufferObjectManager.mLoadedBufferObjects;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		BufferObjectManager.mBufferObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public static void updateBufferObjects(final GL11 pGL11) {
		final ArrayList<BufferObject> pendingBufferObjects = BufferObjectManager.mBufferObjectsToBeLoaded;
		final int pendingBufferObjectCount = pendingBufferObjects.size();
		if(pendingBufferObjectCount > 0){
			for(int i = 0; i < pendingBufferObjectCount; i++){
				final BufferObject pendingBufferObject = pendingBufferObjects.get(i);
				if(!pendingBufferObject.isLoadedToHardware()){
					pendingBufferObject.loadToHardware(pGL11);
					pendingBufferObject.setHardwareBufferNeedsUpdate(true);
				} 
				BufferObjectManager.mLoadedBufferObjects.add(pendingBufferObject);
			}

			pendingBufferObjects.clear();
			//			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
