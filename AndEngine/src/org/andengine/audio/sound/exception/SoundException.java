package org.andengine.audio.sound.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:37:53 - 09.11.2011
 */
public class SoundException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 2647561236520151571L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SoundException() {
		super();
	}

	public SoundException(final String pMessage) {
		super(pMessage);
	}

	public SoundException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public SoundException(final String pMessage, final Throwable pThrowable) {
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
