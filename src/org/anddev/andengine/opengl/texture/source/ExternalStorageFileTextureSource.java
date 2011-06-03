package org.anddev.andengine.opengl.texture.source;

import java.io.File;

import org.anddev.andengine.util.FileUtils;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 10:03:19 - 30.05.2011
 */
public class ExternalStorageFileTextureSource extends FileTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExternalStorageFileTextureSource(final Context pContext, final String pFilePath) {
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
