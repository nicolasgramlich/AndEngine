package org.andengine.audio.music.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:37:53 - 09.11.2011
 */
public class MusicException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -3314204068618256639L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MusicException() {
		super();
	}

	public MusicException(final String pMessage) {
		super(pMessage);
	}

	public MusicException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public MusicException(final String pMessage, final Throwable pThrowable) {
		super(pMessage, pThrowable);
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
