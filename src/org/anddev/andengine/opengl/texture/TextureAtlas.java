package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:22:55 - 14.07.2011
 */
public abstract class TextureAtlas<T extends ITextureSource> extends Texture implements ITextureAtlas<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ArrayList<T> mTextureSources = new ArrayList<T>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureAtlas(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<T> pTextureAtlasStateListener) {
		super(pWidth, pHeight, pTextureFormat, pTextureOptions, pTextureAtlasStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@SuppressWarnings("unchecked")
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener() {
		return (ITextureAtlasStateListener<T>) super.getTextureStateListener();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void addTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		this.checkTextureSourcePosition(pTextureSource, pTexturePositionX, pTexturePositionY);
		pTextureSource.setTexturePositionX(pTexturePositionX);
		pTextureSource.setTexturePositionY(pTexturePositionY);
		this.mTextureSources.add(pTextureSource);
		this.mUpdateOnHardwareNeeded = true;
	}

	@Override
	public void removeTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		final ArrayList<T> textureSources = this.mTextureSources;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final T textureSource = textureSources.get(i);
			if(textureSource == pTextureSource && textureSource.getTexturePositionX() == pTexturePositionX && textureSource.getTexturePositionY() == pTexturePositionY) {
				textureSources.remove(i);
				this.mUpdateOnHardwareNeeded = true;
				return;
			}
		}
	}

	@Override
	public void clearTextureSources() {
		this.mTextureSources.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void checkTextureSourcePosition(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		if(pTexturePositionX < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionX supplied: '" + pTexturePositionX + "'");
		} else if(pTexturePositionY < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionY supplied: '" + pTexturePositionY + "'");
		} else if(pTexturePositionX + pTextureSource.getWidth() > this.getWidth() || pTexturePositionY + pTextureSource.getHeight() > this.getHeight()) {
			throw new IllegalArgumentException("Supplied pTextureSource must not exceed bounds of Texture.");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
