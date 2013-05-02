package org.andengine.util.adt.io.in;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:07:14 - 02.03.2012
 */
public class ResourceInputStreamOpener implements IInputStreamOpener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Resources mResources;
	private final int mResourceID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourceInputStreamOpener(final Resources pResources, final int pResourceID) {
		this.mResources = pResources;
		this.mResourceID = pResourceID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public InputStream open() throws IOException {
		return this.mResources.openRawResource(this.mResourceID);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
