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

	private static BufferObjectManager mActiveInstance = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static BufferObjectManager getActiveInstance() {
		return BufferObjectManager.mActiveInstance;
	}

	public static void setActiveInstance(final BufferObjectManager pActiveInstance) {
		BufferObjectManager.mActiveInstance = pActiveInstance;
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

	public void clear() {
		BufferObjectManager.mBufferObjectsToBeLoaded.clear();
		BufferObjectManager.mLoadedBufferObjects.clear();
		BufferObjectManager.mManagedBufferObjects.clear();
	}

	public void loadBufferObject(final BufferObject pBufferObject) {
		if(BufferObjectManager.mManagedBufferObjects.contains(pBufferObject) == false){
			BufferObjectManager.mManagedBufferObjects.add(pBufferObject);
			BufferObjectManager.mBufferObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public void loadBufferObjects(final BufferObject ... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			BufferObjectManager.mBufferObjectsToBeLoaded.add(pBufferObjects[i]);
		}
	}

	public void reloadBufferObjects() {
		final ArrayList<BufferObject> loadedBufferObjects = BufferObjectManager.mLoadedBufferObjects;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		BufferObjectManager.mBufferObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public void updateBufferObjects(final GL11 pGL11) {
		final ArrayList<BufferObject> bufferObjectToBeLoaded = BufferObjectManager.mBufferObjectsToBeLoaded;
		final int bufferObjectToBeLoadedCount = bufferObjectToBeLoaded.size();
		if(bufferObjectToBeLoadedCount > 0){
			final ArrayList<BufferObject> loadedBufferObjects = BufferObjectManager.mLoadedBufferObjects;

			for(int i = 0; i < bufferObjectToBeLoadedCount; i++){
				final BufferObject pendingBufferObject = bufferObjectToBeLoaded.get(i);
				if(!pendingBufferObject.isLoadedToHardware()){
					pendingBufferObject.loadToHardware(pGL11);
					pendingBufferObject.setHardwareBufferNeedsUpdate(true);
				}
				loadedBufferObjects.add(pendingBufferObject);
			}

			bufferObjectToBeLoaded.clear();
			//			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
