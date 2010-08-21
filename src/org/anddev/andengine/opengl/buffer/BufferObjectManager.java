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

	private final HashSet<BufferObject> mBufferObjectsManaged = new HashSet<BufferObject>();

	private final ArrayList<BufferObject> mBufferObjectsLoaded = new ArrayList<BufferObject>();

	private final ArrayList<BufferObject> mBufferObjectsToBeLoaded = new ArrayList<BufferObject>();
	private final ArrayList<BufferObject> mBufferObjectsToBeUnloaded = new ArrayList<BufferObject>();

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
		this.mBufferObjectsToBeLoaded.clear();
		this.mBufferObjectsLoaded.clear();
		this.mBufferObjectsManaged.clear();
	}

	public void loadBufferObject(final BufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}
		
		if(this.mBufferObjectsManaged.contains(pBufferObject)) {
			/* Just make sure it doesn't get deleted. */
			this.mBufferObjectsToBeUnloaded.remove(pBufferObject);
		} else {
			this.mBufferObjectsManaged.add(pBufferObject);
			this.mBufferObjectsToBeLoaded.add(pBufferObject);
		}
	}

	public void unloadBufferObject(final BufferObject pBufferObject) {
		if(pBufferObject == null) {
			return;
		}
		if(this.mBufferObjectsManaged.contains(pBufferObject)) {
			if(this.mBufferObjectsLoaded.contains(pBufferObject)) {
				this.mBufferObjectsToBeUnloaded.add(pBufferObject);
			} else if(this.mBufferObjectsToBeLoaded.remove(pBufferObject)) {
				this.mBufferObjectsManaged.remove(pBufferObject);
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

	public void reloadBufferObjects() {
		final ArrayList<BufferObject> loadedBufferObjects = this.mBufferObjectsLoaded;
		for(int i = loadedBufferObjects.size() - 1; i >= 0; i--) {
			loadedBufferObjects.get(i).setLoadedToHardware(false);
		}

		this.mBufferObjectsToBeLoaded.addAll(loadedBufferObjects);

		loadedBufferObjects.clear();
	}

	public void updateBufferObjects(final GL11 pGL11) {
		final HashSet<BufferObject> bufferObjectsManaged = this.mBufferObjectsManaged;
		final ArrayList<BufferObject> bufferObjectsLoaded = this.mBufferObjectsLoaded;
		final ArrayList<BufferObject> bufferObjectsToBeLoaded = this.mBufferObjectsToBeLoaded;
		final ArrayList<BufferObject> bufferObjectsToBeUnloaded = this.mBufferObjectsToBeUnloaded;

		/* First load pending BufferObjects. */
		final int bufferObjectToBeLoadedCount = bufferObjectsToBeLoaded.size();

		if(bufferObjectToBeLoadedCount > 0) {
			for(int i = bufferObjectToBeLoadedCount - 1; i >= 0; i--) {
				final BufferObject pendingBufferObject = bufferObjectsToBeLoaded.get(i);
				if(!pendingBufferObject.isLoadedToHardware()) {
					pendingBufferObject.loadToHardware(pGL11);
					pendingBufferObject.setHardwareBufferNeedsUpdate(true);
				}
				bufferObjectsLoaded.add(pendingBufferObject);
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
