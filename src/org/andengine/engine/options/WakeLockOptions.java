package org.andengine.engine.options;

import android.os.PowerManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:45:23 - 10.07.2010
 */
public enum WakeLockOptions {
	// ===========================================================
	// Elements
	// ===========================================================

	/** Screen is on at full brightness. Keyboard backlight is on at full brightness. Requires <b>WAKE_LOCK</b> permission! */
	BRIGHT(PowerManager.FULL_WAKE_LOCK),
	/** Screen is on at full brightness. Keyboard backlight will be allowed to go off. Requires <b>WAKE_LOCK</b> permission!*/
	SCREEN_BRIGHT(PowerManager.SCREEN_BRIGHT_WAKE_LOCK),
	/** Screen is on but may be dimmed. Keyboard backlight will be allowed to go off. Requires <b>WAKE_LOCK</b> permission!*/
	SCREEN_DIM(PowerManager.SCREEN_DIM_WAKE_LOCK),
	/** Screen is on at full brightness. Does <b>not</b> require <b>WAKE_LOCK</b> permission! */
	SCREEN_ON(-1);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mFlag;

	// ===========================================================
	// Constructors
	// ===========================================================

	private WakeLockOptions(final int pFlag) {
		this.mFlag = pFlag;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getFlag() {
		return this.mFlag;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
