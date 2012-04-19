package org.andengine.util.level.simple;

import java.util.HashMap;

import org.andengine.entity.scene.Scene;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoaderContentHandler;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:12:23 - 19.04.2012
 */
public class SimpleLevelLoaderContentHandler extends LevelLoaderContentHandler<SimpleLevelEntityLoaderDataSource, SimpleLevelLoaderResult> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public SimpleLevelLoaderContentHandler(IEntityLoader<SimpleLevelEntityLoaderDataSource> pDefaultEntityLoader, HashMap<String, IEntityLoader<SimpleLevelEntityLoaderDataSource>> pEntityLoaders, SimpleLevelEntityLoaderDataSource pEntityLoaderDataSource) {
		super(pDefaultEntityLoader, pEntityLoaders, pEntityLoaderDataSource);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public SimpleLevelLoaderResult getLevelLoaderResult() {
		return new SimpleLevelLoaderResult((Scene) this.mRootEntity);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
