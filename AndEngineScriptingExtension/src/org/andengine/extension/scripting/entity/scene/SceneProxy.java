package org.andengine.extension.scripting.entity.scene;

import org.andengine.entity.scene.Scene;

public class SceneProxy extends Scene {
	private final long mAddress;

	public SceneProxy(final long pAddress) {
		super();
		this.mAddress = pAddress;
	}

	@Deprecated
	public SceneProxy(final long pAddress, final int pChildCount) {
		super(pChildCount);
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
