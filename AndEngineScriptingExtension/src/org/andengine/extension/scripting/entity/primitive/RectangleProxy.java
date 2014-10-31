package org.andengine.extension.scripting.entity.primitive;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class RectangleProxy extends Rectangle {
	private final long mAddress;

	public RectangleProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.mAddress = pAddress;
	}

	public RectangleProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager, pDrawType);
		this.mAddress = pAddress;
	}

	public RectangleProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final IRectangleVertexBufferObject pRectangleVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pRectangleVertexBufferObject);
		this.mAddress = pAddress;
	}

	public static native void nativeInitClass();

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final boolean handledNative = this.nativeOnAreaTouched(this.mAddress, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);

		if (handledNative) {
			return true;
		} else {
			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	}

	private native boolean nativeOnAreaTouched(final long pAddress, final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY);

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
