package org.anddev.andengine.opengl.buffer;

import java.util.ArrayList;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL11;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	private static final HashSet<BufferObject> mBufferObjectsManaged = new HashSet<BufferObject>();

	private static final ArrayList<BufferObject> mBufferObjectsLoaded = new ArrayList<BufferObject>();

	private static final ArrayList<BufferObject> mBufferObjectsToBeLoaded = new ArrayList<BufferObject>();
	private static final ArrayList<BufferObject> mBufferObjectsToBeUnloaded = new ArrayList<BufferObject>();

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

	public synchronized void clear() {
		BufferObjectManager.mBufferObjectsToBeLoaded.clear();
		BufferObjectManager.mBufferObjectsLoaded.clear();
		BufferObjectManager.mBufferObjectsManaged.clear();
	}

	public synchronized void loadBufferObject(final BufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}

		if(BufferObjectManager.mBufferObjectsManaged.contains(pBufferObject)) {
			/* Just make sure it doesn't get deleted. */
			BufferObjectManager.mBufferObjectsToBeUnloaded.remove(pBufferObject);
		} else {
			BufferObjectManager.mBufferObjectsManaged.add(pBufferObject);
			BufferObjectManager.mBufferObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public synchronized void unloadBufferObject(final BufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}
		if(BufferObjectManager.mBufferObjectsManaged.contains(pBufferObject)) {
			if(BufferObjectManager.mBufferObjectsLoaded.contains(pBufferObject)) {
				BufferObjectManager.mBufferObjectsToBeUnloaded.add(pBufferObject);
			} else if(BufferObjectManager.mBufferObjectsToBeLoaded.remove(pBufferObject)) {
				BufferObjectManager.mBufferObjectsManaged.remove(pBufferObject);
			}
		}
	}

	public void loadBufferObjects(final BufferObject... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			this.loadBufferObject(pBufferObjects[i]);
		}
	}

	public void unloadBufferObjects(final BufferObject... pBufferObjects) {
		for(int i = pBufferObjects.length - 1; i >= 0; i--) {
			this.unloadBufferObject(pBufferObjects[i]);
		}
	}

	public synchronized void reloadBufferObjects() {
		final ArrayList<BufferObject> loadedBufferObjects = BufferObjectManager.mBufferObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		BufferObjectManager.mBufferObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public synchronized void updateBufferObjects(final GL11 pGL11) {
		final HashSet<BufferObject> bufferObjectsManaged = BufferObjectManager.mBufferObjectsManaged;
		final ArrayList<BufferObject> bufferObjectsLoaded = BufferObjectManager.mBufferObjectsLoaded;
		final ArrayList<BufferObject> bufferObjectsToBeLoaded = BufferObjectManager.mBufferObjectsToBeLoaded;
		final ArrayList<BufferObject> bufferObjectsToBeUnloaded = BufferObjectManager.mBufferObjectsToBeUnloaded;

		/* First load pending BufferObjects. */
		final int bufferObjectToBeLoadedCount = bufferObjectsToBeLoaded.size();

		if(bufferObjectToBeLoadedCount > 0) {
			for(int i = bufferObjectToBeLoadedCount - 1; i >= 0; i--) {
				final BufferObject bufferObjectToBeLoaded = bufferObjectsToBeLoaded.get(i);
				if(!bufferObjectToBeLoaded.isLoadedToHardware()) {
					bufferObjectToBeLoaded.loadToHardware(pGL11);
					bufferObjectToBeLoaded.setHardwareBufferNeedsUpdate();
				}
				bufferObjectsLoaded.add(bufferObjectToBeLoaded);
			}

			bufferObjectsToBeLoaded.clear();
		}

		/* Then unload pending BufferObjects. */
		final int bufferObjectsToBeUnloadedCount = bufferObjectsToBeUnloaded.size();

		if(bufferObjectsToBeUnloadedCount > 0){
			for(int i = bufferObjectsToBeUnloadedCount - 1; i >= 0; i--){
				final BufferObject bufferObjectToBeUnloaded = bufferObjectsToBeUnloaded.remove(i);
				if(bufferObjectToBeUnloaded.isLoadedToHardware()){
					bufferObjectToBeUnloaded.unloadFromHardware(pGL11);
				}
				bufferObjectsLoaded.remove(bufferObjectToBeUnloaded);
				bufferObjectsManaged.remove(bufferObjectToBeUnloaded);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
