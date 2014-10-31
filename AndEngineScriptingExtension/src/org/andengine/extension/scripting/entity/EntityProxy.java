package org.andengine.extension.scripting.entity;

import org.andengine.entity.Entity;

public class EntityProxy extends Entity {
	private final long mAddress;

	public EntityProxy(final long pAddress) {
		super();
		this.mAddress = pAddress;
	}

	public EntityProxy(final long pAddress, final float pX, final float pY) {
		super(pX, pY);
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
