package org.andengine.util.level.simple;

import java.util.HashMap;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.IEntityLoaderListener;
import org.andengine.util.level.LevelLoader;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 4:11:17 - 19.04.2012
 */
public class SimpleLevelLoader extends LevelLoader<SimpleLevelEntityLoaderData, IEntityLoaderListener, SimpleLevelLoaderResult> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final VertexBufferObjectManager mVertexBufferObjectManager;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SimpleLevelLoader(final VertexBufferObjectManager pVertexBufferObjectManager) {
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected SimpleLevelEntityLoaderData onCreateEntityLoaderData() {
		return new SimpleLevelEntityLoaderData(this.mVertexBufferObjectManager);
	}

	@Override
	protected SimpleLevelLoaderContentHandler onCreateLevelLoaderContentHandler(final HashMap<String, IEntityLoader<SimpleLevelEntityLoaderData>> pEntityLoaders, final IEntityLoader<SimpleLevelEntityLoaderData> pDefaultEntityLoader, final SimpleLevelEntityLoaderData pEntityLoaderData, final IEntityLoaderListener pEntityLoaderListener) {
		return new SimpleLevelLoaderContentHandler(pDefaultEntityLoader, pEntityLoaders, pEntityLoaderData);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
