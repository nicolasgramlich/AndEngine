package org.andengine.extension.scripting.entity.sprite;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SpriteProxy extends Sprite {
	private final long mAddress;

	public SpriteProxy(final long pAddress, final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObject, pShaderProgram);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion,
			final ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion,
			final ISpriteVertexBufferObject pSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject, pShaderProgram);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType,
			final ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
		this.mAddress = pAddress;
	}

	public SpriteProxy(final long pAddress, final float pX, final float pY, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pVertexBufferObject) {
		super(pX, pY, pTextureRegion, pVertexBufferObject);
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
