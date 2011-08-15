package org.anddev.andengine.opengl.texture.region;


import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.source.ITextureAtlasSource;


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
	
	public static ITextureRegion extractFromTexture(final ITexture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final boolean pTextureRegionBufferManaged) {
		final ITextureRegion textureRegion = new TextureRegion(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		return textureRegion;
	}

	public static <T extends ITextureAtlasSource> ITextureRegion createFromSource(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
		final TextureRegion textureRegion = new TextureRegion(pTextureAtlas, pTexturePositionX, pTexturePositionY, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight());
		pTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, textureRegion.getX(), textureRegion.getY());
		return textureRegion;
	}

	public static <T extends ITextureAtlasSource> TiledTextureRegion createTiledFromSource(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final TiledTextureRegion tiledTextureRegion = TiledTextureRegion.create(pTextureAtlas, pTexturePositionX, pTexturePositionY, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight(), pTileColumns, pTileRows);
		pTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, tiledTextureRegion.getX(), tiledTextureRegion.getY());
		return tiledTextureRegion;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
