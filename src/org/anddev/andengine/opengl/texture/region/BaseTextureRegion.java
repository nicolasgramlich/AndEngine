package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public abstract class BaseTextureRegion implements ITextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITexture mTexture;

	protected float mU;
	protected float mU2;
	protected float mV;
	protected float mV2;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureRegion(final ITexture pTexture) {
		this.mTexture = pTexture;
	}
	
	@Override
	public ITextureRegion clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITexture getTexture() {
		return this.mTexture;
	}

	@Override
	public float getU() {
		return this.mU;
	}

	@Override
	public float getU2() {
		return this.mU2;
	}

	@Override
	public float getV() {
		return this.mV;
	}

	@Override
	public float getV2() {
		return this.mV2;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void updateUV() {
		final ITexture texture = this.mTexture;
		final int textureWidth = texture.getWidth();
		final int textureHeight = texture.getHeight();

		final int x = this.getX();
		final int y = this.getY();

		this.mU = (float) x / textureWidth;
		this.mU2 = (float) (x + this.getWidth()) / textureWidth;

		this.mV = (float) y / textureHeight;
		this.mV2 = (float) (y + this.getHeight()) / textureHeight;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
