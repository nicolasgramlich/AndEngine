package org.andengine.extension.scripting.engine.camera.hud;

import org.andengine.engine.camera.hud.HUD;

public class HUDProxy extends HUD {
	private final long mAddress;

	public HUDProxy(final long pAddress) {
		super();
		this.mAddress = pAddress;
	}

	public static native void nativeInitClass();

	@Override
	public void onAttached() {
		if (!this.nativeOnAttached(this.mAddress)) {
			super.onAttached();
		}
	}

	private native boolean nativeOnAttached(final long pAddress);

	@Override
	public void onDetached() {
		if (!this.nativeOnDetached(this.mAddress)) {
			super.onDetached();
		}
	}

	private native boolean nativeOnDetached(final long pAddress);
}
