/**
 * 
 */
package org.anddev.andengine.opengl.view;

import java.io.Writer;

import android.util.Log;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:42:02 - 28.06.2010
 */
class LogWriter extends Writer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final StringBuilder mBuilder = new StringBuilder();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void close() {
		this.flushBuilder();
	}

	@Override
	public void flush() {
		this.flushBuilder();
	}

	@Override
	public void write(final char[] buf, final int offset, final int count) {
		for(int i = 0; i < count; i++) {
			final char c = buf[offset + i];
			if(c == '\n') {
				this.flushBuilder();
			} else {
				this.mBuilder.append(c);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void flushBuilder() {
		if(this.mBuilder.length() > 0) {
			Log.v("GLSurfaceView", this.mBuilder.toString());
			this.mBuilder.delete(0, this.mBuilder.length());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}