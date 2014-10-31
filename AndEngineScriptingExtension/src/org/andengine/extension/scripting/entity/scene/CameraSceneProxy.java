package org.andengine.extension.scripting.entity.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;

public class CameraSceneProxy extends CameraScene {
	private final long mAddress;

	public CameraSceneProxy(final long pAddress, final Camera pCamera) {
		super(pCamera);
		this.mAddress = pAddress;
	}

	public CameraSceneProxy(final long pAddress) {
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
