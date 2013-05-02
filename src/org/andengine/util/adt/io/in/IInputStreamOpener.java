package org.andengine.util.adt.io.in;

import java.io.IOException;
import java.io.InputStream;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:54:25 - 02.03.2012
 */
public interface IInputStreamOpener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public InputStream open() throws IOException;
}
