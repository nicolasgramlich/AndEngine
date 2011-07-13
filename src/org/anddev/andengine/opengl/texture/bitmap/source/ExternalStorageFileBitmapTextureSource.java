package org.anddev.andengine.opengl.texture.bitmap.source;

import java.io.File;

import org.anddev.andengine.util.FileUtils;

import android.content.Context;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:03:19 - 30.05.2011
 */
public class ExternalStorageFileBitmapTextureSource extends FileBitmapTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExternalStorageFileBitmapTextureSource(final Context pContext, final String pFilePath) {
		super(new File(FileUtils.getAbsolutePathOnExternalStorage(pContext, pFilePath)));
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
