package org.andengine.extension.tmx;

import org.andengine.extension.tmx.util.constants.TMXConstants;
import org.andengine.extension.tmx.util.exception.TMXParseException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.ColorKeyBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.RectangleBitmapTextureAtlasSourceDecoratorShape;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:03:24 - 20.07.2010
 */
public class TMXTileSet implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mFirstGlobalTileID;
	private final String mName;
	private final int mTileWidth;
	private final int mTileHeight;

	private String mImageSource;
	private ITexture mTexture;
	private final TextureOptions mTextureOptions;

	private int mTilesHorizontal;
	private int mTilesVertical;

	private final int mSpacing;
	private final int mMargin;

	private final SparseArray<TMXProperties<TMXTileProperty>> mTMXTileProperties = new SparseArray<TMXProperties<TMXTileProperty>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	TMXTileSet(final Attributes pAttributes, final TextureOptions pTextureOptions) {
		this(SAXUtils.getIntAttribute(pAttributes, TMXConstants.TAG_TILESET_ATTRIBUTE_FIRSTGID, 1), pAttributes, pTextureOptions);
	}

	TMXTileSet(final int pFirstGlobalTileID, final Attributes pAttributes, final TextureOptions pTextureOptions) {
		this.mFirstGlobalTileID = pFirstGlobalTileID;
		this.mName = pAttributes.getValue("", TMXConstants.TAG_TILESET_ATTRIBUTE_NAME);
		this.mTileWidth = SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_TILESET_ATTRIBUTE_TILEWIDTH);
		this.mTileHeight = SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_TILESET_ATTRIBUTE_TILEHEIGHT);
		this.mSpacing = SAXUtils.getIntAttribute(pAttributes, TMXConstants.TAG_TILESET_ATTRIBUTE_SPACING, 0);
		this.mMargin = SAXUtils.getIntAttribute(pAttributes, TMXConstants.TAG_TILESET_ATTRIBUTE_MARGIN, 0);

		this.mTextureOptions = pTextureOptions;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final int getFirstGlobalTileID() {
		return this.mFirstGlobalTileID;
	}

	public final String getName() {
		return this.mName;
	}

	public final int getTileWidth() {
		return this.mTileWidth;
	}

	public final int getTileHeight() {
		return this.mTileHeight;
	}

	public int getTilesHorizontal() {
		return this.mTilesHorizontal;
	}

	public int getTilesVertical() {
		return this.mTilesVertical;
	}

	public ITexture getTexture() {
		return this.mTexture;
	}

	public void setImageSource(final AssetManager pAssetManager, final TextureManager pTextureManager, final Attributes pAttributes) throws TMXParseException {
		this.mImageSource = pAttributes.getValue("", TMXConstants.TAG_IMAGE_ATTRIBUTE_SOURCE);

		final AssetBitmapTextureAtlasSource assetBitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(pAssetManager, this.mImageSource);
		this.mTilesHorizontal = TMXTileSet.determineCount(assetBitmapTextureAtlasSource.getTextureWidth(), this.mTileWidth, this.mMargin, this.mSpacing);
		this.mTilesVertical = TMXTileSet.determineCount(assetBitmapTextureAtlasSource.getTextureHeight(), this.mTileHeight, this.mMargin, this.mSpacing);
		final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(pTextureManager, assetBitmapTextureAtlasSource.getTextureWidth(), assetBitmapTextureAtlasSource.getTextureHeight(), BitmapTextureFormat.RGBA_8888, this.mTextureOptions); // TODO Make TextureFormat variable

		final String transparentColor = SAXUtils.getAttribute(pAttributes, TMXConstants.TAG_IMAGE_ATTRIBUTE_TRANS, null);
		if(transparentColor == null) {
			BitmapTextureAtlasTextureRegionFactory.createFromSource(bitmapTextureAtlas, assetBitmapTextureAtlasSource, 0, 0);
		} else {
			try{
				final int color = Color.parseColor((transparentColor.charAt(0) == '#') ? transparentColor : "#" + transparentColor);
				BitmapTextureAtlasTextureRegionFactory.createFromSource(bitmapTextureAtlas, new ColorKeyBitmapTextureAtlasSourceDecorator(assetBitmapTextureAtlasSource, RectangleBitmapTextureAtlasSourceDecoratorShape.getDefaultInstance(), color), 0, 0);
			} catch (final IllegalArgumentException e) {
				throw new TMXParseException("Illegal value: '" + transparentColor + "' for attribute 'trans' supplied!", e);
			}
		}
		this.mTexture = bitmapTextureAtlas;
		this.mTexture.load();
	}

	public String getImageSource() {
		return this.mImageSource;
	}

	public SparseArray<TMXProperties<TMXTileProperty>> getTMXTileProperties() {
		return this.mTMXTileProperties;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public TMXProperties<TMXTileProperty> getTMXTilePropertiesFromGlobalTileID(final int pGlobalTileID) {
		final int localTileID = pGlobalTileID - this.mFirstGlobalTileID;
		return this.mTMXTileProperties.get(localTileID);
	}

	public void addTMXTileProperty(final int pLocalTileID, final TMXTileProperty pTMXTileProperty) {
		final TMXProperties<TMXTileProperty> existingProperties = this.mTMXTileProperties.get(pLocalTileID);
		if(existingProperties != null) {
			existingProperties.add(pTMXTileProperty);
		} else {
			final TMXProperties<TMXTileProperty> newProperties = new TMXProperties<TMXTileProperty>();
			newProperties.add(pTMXTileProperty);
			this.mTMXTileProperties.put(pLocalTileID, newProperties);
		}
	}

	public ITextureRegion getTextureRegionFromGlobalTileID(final int pGlobalTileID) {
		final int localTileID = pGlobalTileID - this.mFirstGlobalTileID;
		final int tileColumn = localTileID % this.mTilesHorizontal;
		final int tileRow = localTileID / this.mTilesHorizontal;

		final int texturePositionX = this.mMargin + (this.mSpacing + this.mTileWidth) * tileColumn;
		final int texturePositionY = this.mMargin + (this.mSpacing + this.mTileHeight) * tileRow;

		return new TextureRegion(this.mTexture, texturePositionX, texturePositionY, this.mTileWidth, this.mTileHeight);
	}

	private static int determineCount(final int pTotalExtent, final int pTileExtent, final int pMargin, final int pSpacing) {
		int count = 0;
		int remainingExtent = pTotalExtent;

		remainingExtent -= pMargin * 2;

		while(remainingExtent > 0) {
			remainingExtent -= pTileExtent;
			remainingExtent -= pSpacing;
			count++;
		}

		return count;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
