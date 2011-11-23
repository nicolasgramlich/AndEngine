package org.andengine.engine.options;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:13:07 - 22.11.2011
 */
public class AudioOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mNeedsSound;
	private boolean mNeedsMusic;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean needsSound() {
		return this.mNeedsSound;
	}

	public AudioOptions setNeedsSound(final boolean pNeedsSound) {
		this.mNeedsSound = pNeedsSound;
		return this;
	}

	public boolean needsMusic() {
		return this.mNeedsMusic;
	}

	public AudioOptions setNeedsMusic(final boolean pNeedsMusic) {
		this.mNeedsMusic = pNeedsMusic;
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
