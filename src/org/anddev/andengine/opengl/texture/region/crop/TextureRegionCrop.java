package org.anddev.andengine.opengl.texture.region.crop;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;

/**
 * @author Jonathan Heek
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:51:50 - 05.07.2011
 */
public class TextureRegionCrop {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LEFT_INDEX = 0;
	private static final int BOTTOM_INDEX = 1;
	private static final int WIDTH_INDEX = 2;
	private static final int HEIGHT_INDEX = 3;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final BaseTextureRegion mTextureRegion;

	private boolean mFlippedHorizontal;
	private boolean mFlippedVertical;

	private final int[] mData = new int[4];
	private boolean mDirty = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegionCrop(final BaseTextureRegion pBaseTextureRegion) {
		this.mTextureRegion = pBaseTextureRegion;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isDirty() {
		return this.mDirty;
	}

	public int[] getData() {
		return this.mData;
	}

	public boolean isFlippedHorizontal() {
		return this.mFlippedHorizontal;
	}

	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		if(this.mFlippedHorizontal != pFlippedHorizontal) {
			this.mFlippedHorizontal = pFlippedHorizontal;
			this.update();
		}
	}

	public boolean isFlippedVertical() {
		return this.mFlippedVertical;
	}

	public void setFlippedVertical(final boolean pFlippedVertical) {
		if(this.mFlippedVertical != pFlippedVertical) {
			this.mFlippedVertical = pFlippedVertical;
			this.update();
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void update() {
		final BaseTextureRegion textureRegion = this.mTextureRegion;
		final ITexture texture = textureRegion.getTexture();

		if(texture == null) { // TODO Check really needed?
			return;
		}

		final int[] values = this.mData;

		final int textureCropLeft = textureRegion.getTextureCropLeft();
		final int textureCropTop = textureRegion.getTextureCropTop();
		final int textureCropWidth = textureRegion.getTextureCropWidth();
		final int textureCropHeight = textureRegion.getTextureCropHeight();

		// TODO support flipping !
		if(this.mFlippedVertical) {
			if(this.mFlippedHorizontal) {

			} else {

			}
		} else {
			if(this.mFlippedHorizontal) {

			} else {
				values[LEFT_INDEX] = textureCropLeft;
				values[BOTTOM_INDEX] = textureCropTop + textureCropHeight;
				values[WIDTH_INDEX] = textureCropWidth;
				values[HEIGHT_INDEX] = -textureCropHeight;
			}
		}

		this.mDirty = true;
	}

	public void selectOnHardware(final GL11 pGL11) {
		if(this.mDirty) {
			this.mDirty = false;
			synchronized(this) {
				GLHelper.textureCrop(pGL11, this);
			}
		}
	}

	public void apply(final GL11 pGL11) {
		GLHelper.textureCrop(pGL11, this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
