package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.builder.ITextureBuilder;
import org.anddev.andengine.opengl.texture.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class BuildableTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ITextureSource> mTextureSourcesToPlace = new ArrayList<ITextureSource>();

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 */
	public BuildableTexture(final int pWidth, final int pHeight) {
		super(pWidth, pHeight, TextureOptions.DEFAULT, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link BuildableTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BuildableTexture(final int pWidth, final int pHeight, final ITextureStateListener pTextureStateListener) {
		super(pWidth, pHeight, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 */
	public BuildableTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link BuildableTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BuildableTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
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
	 * Use {@link BuildableTexture#addTextureSource(ITextureSource)} instead.
	 */
	@Deprecated
	public void addTextureSource(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		super.addTextureSource(pTextureSource, pTexturePositionX, pTexturePositionY);
	}
	
	@Override
	public void clearTextureSources() {
		super.clearTextureSources();
		this.mTextureSourcesToPlace.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * When all {@link ITextureSource}s are added you have to call {@link BuildableTexture#build(ITextureBuilder)}.
	 * @param pTextureSource to be added.
	 */
	public void addTextureSource(final ITextureSource pTextureSource) {
		this.mTextureSourcesToPlace.add(pTextureSource);
	}
	
	/**
	 * Removes a {@link ITextureSource} before {@link BuildableTexture#build(ITextureBuilder)} is called.
	 * @param pTextureSource to be removed.
	 */
	public void removeTextureSource(final ITextureSource pTextureSource) {
		final ArrayList<ITextureSource> textureSources = this.mTextureSourcesToPlace;
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
	 * @param pTextureSourcePackingAlgorithm the {@link ITextureBuilder} to use for packing the {@link ITextureSource} in this {@link BuildableTexture}.
	 * @throws TextureSourcePackingException i.e. when the {@link ITextureSource}s didn't fit into this {@link BuildableTexture}.
	 */
	public void build(final ITextureBuilder pTextureSourcePackingAlgorithm) throws TextureSourcePackingException {
		pTextureSourcePackingAlgorithm.pack(this, this.mTextureSourcesToPlace);
		this.mTextureSourcesToPlace.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
