package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.packing.ITextureSourcePackingAlgorithm;
import org.anddev.andengine.opengl.texture.source.packing.ITextureSourcePackingAlgorithm.TextureSourcePackingException;

/**
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class PackableTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ITextureSource> mTextureSourcesToPack = new ArrayList<ITextureSource>();

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 */
	public PackableTexture(final int pWidth, final int pHeight) {
		super(pWidth, pHeight, TextureOptions.DEFAULT, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link PackableTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public PackableTexture(final int pWidth, final int pHeight, final ITextureStateListener pTextureStateListener) {
		super(pWidth, pHeight, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 */
	public PackableTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link PackableTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public PackableTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions, pTextureStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureSource} won't get packed through this.
	 * Use {@link PackableTexture#addTextureSource(ITextureSource)} instead.
	 */
	@Deprecated
	public void addTextureSource(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		super.addTextureSource(pTextureSource, pTexturePositionX, pTexturePositionY);
	}
	
	@Override
	public void clearTextureSources() {
		super.clearTextureSources();
		this.mTextureSourcesToPack.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * When all {@link ITextureSource}s are added you have to call {@link PackableTexture#pack(ITextureSourcePackingAlgorithm)}.
	 * @param pTextureSource to be added.
	 */
	public void addTextureSource(final ITextureSource pTextureSource) {
		this.mTextureSourcesToPack.add(pTextureSource);
	}
	
	/**
	 * Removes a {@link ITextureSource} before {@link PackableTexture#pack(ITextureSourcePackingAlgorithm)} is called.
	 * @param pTextureSource to be removed.
	 */
	public void removeTextureSource(final ITextureSource pTextureSource) {
		final ArrayList<ITextureSource> textureSources = this.mTextureSourcesToPack;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final ITextureSource textureSource = textureSources.get(i);
			if(textureSource == pTextureSource) {
				textureSources.remove(i);
				this.mUpdateOnHardwareNeeded = true;
				return;
			}
		}
	}
	
	/**
	 * May draw over already added {@link ITextureSource}s.
	 * 
	 * @param pTextureSourcePackingAlgorithm the {@link ITextureSourcePackingAlgorithm} to use for packing the {@link ITextureSource} in this {@link PackableTexture}.
	 * @throws TextureSourcePackingException i.e. when the {@link ITextureSource}s didn't fit into this {@link PackableTexture}.
	 */
	public void pack(final ITextureSourcePackingAlgorithm pTextureSourcePackingAlgorithm) throws TextureSourcePackingException {
		pTextureSourcePackingAlgorithm.pack(this, this.mTextureSourcesToPack);
		this.mTextureSourcesToPack.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
