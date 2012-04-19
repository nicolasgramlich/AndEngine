package org.andengine.util.level.simple;

import java.util.HashMap;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 4:11:17 - 19.04.2012
 */
public class SimpleLevelLoader extends LevelLoader<SimpleLevelEntityLoaderDataSource, SimpleLevelLoaderResult> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SimpleLevelLoader(final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(new SimpleLevelEntityLoaderDataSource(pVertexBufferObjectManager));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected SimpleLevelLoaderContentHandler onCreateLevelLoaderContentHandler(final HashMap<String, IEntityLoader<SimpleLevelEntityLoaderDataSource>> pEntityLoaders, final IEntityLoader<SimpleLevelEntityLoaderDataSource> pDefaultEntityLoader, final SimpleLevelEntityLoaderDataSource pEntityLoaderDataSource) {
		return new SimpleLevelLoaderContentHandler(pDefaultEntityLoader, pEntityLoaders, pEntityLoaderDataSource);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
