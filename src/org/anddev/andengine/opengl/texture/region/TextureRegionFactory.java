package org.anddev.andengine.opengl.texture.region;


import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:15:14 - 09.03.2010
 */
public class TextureRegionFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public static TextureRegion extractFromTexture(final ITexture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final boolean pTextureRegionBufferManaged) {
		final TextureRegion textureRegion = new TextureRegion(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		textureRegion.setTextureRegionBufferManaged(pTextureRegionBufferManaged);
		return textureRegion;
	}

	public static <T extends ITextureAtlasSource> TextureRegion createFromSource(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY, final boolean pCreateTextureRegionBuffersManaged) {
		final TextureRegion textureRegion = new TextureRegion(pTextureAtlas, pTexturePositionX, pTexturePositionY, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight());
		pTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, textureRegion.getTexturePositionX(), textureRegion.getTexturePositionY());
		textureRegion.setTextureRegionBufferManaged(pCreateTextureRegionBuffersManaged);
		return textureRegion;
	}

	public static <T extends ITextureAtlasSource> TiledTextureRegion createTiledFromSource(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows, final boolean pCreateTextureRegionBuffersManaged) {
		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(pTextureAtlas, pTexturePositionX, pTexturePositionY, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight(), pTileColumns, pTileRows);
		pTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, tiledTextureRegion.getTexturePositionX(), tiledTextureRegion.getTexturePositionY());
		tiledTextureRegion.setTextureRegionBufferManaged(pCreateTextureRegionBuffersManaged);
		return tiledTextureRegion;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
