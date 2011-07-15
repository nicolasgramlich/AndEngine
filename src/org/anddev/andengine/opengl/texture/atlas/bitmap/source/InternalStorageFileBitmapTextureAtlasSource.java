package org.anddev.andengine.opengl.texture.atlas.bitmap.source;

import java.io.File;

import org.anddev.andengine.util.FileUtils;

import android.content.Context;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:01:19 - 30.05.2011
 */
public class InternalStorageFileBitmapTextureAtlasSource extends FileBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public InternalStorageFileBitmapTextureAtlasSource(final Context pContext, final String pFilePath) {
		super(new File(FileUtils.getAbsolutePathOnInternalStorage(pContext, pFilePath)));
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
