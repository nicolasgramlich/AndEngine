package org.andengine.engine.options;

import org.andengine.audio.sound.SoundManager;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:13:07 - 22.11.2011
 */
public class SoundOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mNeedsSound;
	private int mMaxSimultaneousStreams = SoundManager.MAX_SIMULTANEOUS_STREAMS_DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean needsSound() {
		return this.mNeedsSound;
	}

	public SoundOptions setNeedsSound(final boolean pNeedsSound) {
		this.mNeedsSound = pNeedsSound;
		return this;
	}

	public int getMaxSimultaneousStreams() {
		return this.mMaxSimultaneousStreams;
	}

	public SoundOptions setMaxSimultaneousStreams(final int pMaxSimultaneousStreams) {
		this.mMaxSimultaneousStreams = pMaxSimultaneousStreams;
		return this;
	}

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
