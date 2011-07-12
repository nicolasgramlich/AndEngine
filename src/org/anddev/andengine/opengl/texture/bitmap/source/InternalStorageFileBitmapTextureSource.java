package org.anddev.andengine.opengl.texture.bitmap.source;

import java.io.File;

import org.anddev.andengine.util.FileUtils;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 10:01:19 - 30.05.2011
 */
public class InternalStorageFileBitmapTextureSource extends FileBitmapTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public InternalStorageFileBitmapTextureSource(final Context pContext, final String pFilePath) {
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
