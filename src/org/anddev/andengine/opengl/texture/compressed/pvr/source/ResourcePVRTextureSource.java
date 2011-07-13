package org.anddev.andengine.opengl.texture.compressed.pvr.source;

import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.opengl.texture.source.BaseTextureSource;

import android.content.Context;

/**
 * TODO Implementation: https://github.com/cocos2d/cocos2d-iphone/blob/develop/cocos2d/CCTexturePVR.m
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:30:33 - 13.07.2010
 */
public class ResourcePVRTextureSource extends BaseTextureSource implements IPVRTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private final int mRawResourceID;
	private final Context mContext;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourcePVRTextureSource(final Context pContext, final int pRawResourceID) throws IOException {
		this(pContext, pRawResourceID, 0, 0);
	}

	public ResourcePVRTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY) throws IOException {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;

		// TODO Read width/height and additional data like internal format, etc...
		this.mWidth = 0;
		this.mHeight = 0;
	}

	protected ResourcePVRTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public ResourcePVRTextureSource clone() {
		return new ResourcePVRTextureSource(this.mContext, this.mRawResourceID, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	public InputStream onGetInputStream() {
		return this.mContext.getResources().openRawResource(this.mRawResourceID);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mRawResourceID + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}