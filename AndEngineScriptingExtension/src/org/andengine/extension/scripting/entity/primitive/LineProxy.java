package org.andengine.extension.scripting.entity.primitive;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.vbo.ILineVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LineProxy extends Line {
	private final long mAddress;

	public LineProxy(final long pAddress, final float pX1, final float pY1, final float pX2, final float pY2, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX1, pY1, pX2, pY2, pVertexBufferObjectManager);
		this.mAddress = pAddress;
	}

	public LineProxy(final long pAddress, final float pX1, final float pY1, final float pX2, final float pY2, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX1, pY1, pX2, pY2, pVertexBufferObjectManager, pDrawType);
		this.mAddress = pAddress;
	}

	public LineProxy(final long pAddress, final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX1, pY1, pX2, pY2, pLineWidth, pVertexBufferObjectManager);
		this.mAddress = pAddress;
	}

	public LineProxy(final long pAddress, final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType) {
		super(pX1, pY1, pX2, pY2, pLineWidth, pVertexBufferObjectManager, pDrawType);
		this.mAddress = pAddress;
	}

	public LineProxy(final long pAddress, final float pX1, final float pY1, final float pX2, final float pY2, final float pLineWidth, final ILineVertexBufferObject pLineVertexBufferObject) {
		super(pX1, pY1, pX2, pY2, pLineWidth, pLineVertexBufferObject);
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
