package org.andengine.extension.tmx;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.zip.InflaterInputStream;
import java.util.zip.GZIPInputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.constants.TMXConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.StreamUtils;
import org.andengine.util.algorithm.collision.RectangularShapeCollisionChecker;
import org.andengine.util.base64.Base64;
import org.andengine.util.base64.Base64InputStream;
import org.andengine.util.color.Color;
import org.andengine.util.exception.AndEngineRuntimeException;
import org.andengine.util.exception.MethodNotSupportedException;
import org.andengine.util.math.MathUtils;
import org.xml.sax.Attributes;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:27:31 - 20.07.2010
 */
public class TMXLayer extends SpriteBatch implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final TMXTiledMap mTMXTiledMap;

	private final String mName;
	private final int mTileColumns;
	private final int mTileRows;
	private final TMXTile[][] mTMXTiles;

	private int mTilesAdded;
	private final int mGlobalTileIDsExpected;

	private final float[] mCullingVertices = new float[2 * Sprite.VERTICES_PER_SPRITE];

	private final TMXProperties<TMXLayerProperty> mTMXLayerProperties = new TMXProperties<TMXLayerProperty>();

	private final int mWidth;
	private final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXLayer(final TMXTiledMap pTMXTiledMap, final Attributes pAttributes, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(null, SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_LAYER_ATTRIBUTE_WIDTH) * SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_LAYER_ATTRIBUTE_HEIGHT), pVertexBufferObjectManager);

		this.mTMXTiledMap = pTMXTiledMap;
		this.mName = pAttributes.getValue("", TMXConstants.TAG_LAYER_ATTRIBUTE_NAME);
		this.mTileColumns = SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_LAYER_ATTRIBUTE_WIDTH);
		this.mTileRows = SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_LAYER_ATTRIBUTE_HEIGHT);
		this.mTMXTiles = new TMXTile[this.mTileRows][this.mTileColumns];

		this.mWidth = pTMXTiledMap.getTileWidth() * this.mTileColumns;
		this.mHeight = pTMXTiledMap.getTileHeight() * this.mTileRows;

		this.mRotationCenterX = this.mWidth * 0.5f;
		this.mRotationCenterY = this.mHeight * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;

		this.mGlobalTileIDsExpected = this.mTileColumns * this.mTileRows;

		this.setVisible(SAXUtils.getIntAttribute(pAttributes, TMXConstants.TAG_LAYER_ATTRIBUTE_VISIBLE, TMXConstants.TAG_LAYER_ATTRIBUTE_VISIBLE_VALUE_DEFAULT) == 1);
		this.setAlpha(SAXUtils.getFloatAttribute(pAttributes, TMXConstants.TAG_LAYER_ATTRIBUTE_OPACITY, TMXConstants.TAG_LAYER_ATTRIBUTE_OPACITY_VALUE_DEFAULT));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getName() {
		return this.mName;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public int getTileColumns() {
		return this.mTileColumns;
	}

	public int getTileRows() {
		return this.mTileRows;
	}

	public TMXTile[][] getTMXTiles() {
		return this.mTMXTiles;
	}

	public TMXTile getTMXTile(final int pTileColumn, final int pTileRow) throws ArrayIndexOutOfBoundsException {
		return this.mTMXTiles[pTileRow][pTileColumn];
	}

	/**
	 * @param pX in SceneCoordinates.
	 * @param pY in SceneCoordinates.
	 * @return the {@link TMXTile} located at <code>pX/pY</code>.
	 */
	public TMXTile getTMXTileAt(final float pX, final float pY) {
		final float[] localCoords = this.convertSceneToLocalCoordinates(pX, pY);
		final TMXTiledMap tmxTiledMap = this.mTMXTiledMap;

		final int tileColumn = (int)(localCoords[SpriteBatch.VERTEX_INDEX_X] / tmxTiledMap.getTileWidth());
		if(tileColumn < 0 || tileColumn > this.mTileColumns - 1) {
			return null;
		}
		final int tileRow = (int)(localCoords[SpriteBatch.VERTEX_INDEX_Y] / tmxTiledMap.getTileWidth());
		if(tileRow < 0 || tileRow > this.mTileRows - 1) {
			return null;
		}

		return this.mTMXTiles[tileRow][tileColumn];
	}

	public void addTMXLayerProperty(final TMXLayerProperty pTMXLayerProperty) {
		this.mTMXLayerProperties.add(pTMXLayerProperty);
	}

	public TMXProperties<TMXLayerProperty> getTMXLayerProperties() {
		return this.mTMXLayerProperties;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void initBlendFunction(final ITexture pTexture) {

	}

	@Override
	@Deprecated
	public void setRotation(final float pRotation) throws MethodNotSupportedException {
		throw new MethodNotSupportedException();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		/* Nothing. */
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		final int tileColumns = this.mTileColumns;
		final int tileRows = this.mTileRows;
		final int tileWidth = this.mTMXTiledMap.getTileWidth();
		final int tileHeight = this.mTMXTiledMap.getTileHeight();

		final float scaledTileWidth = tileWidth * this.mScaleX;
		final float scaledTileHeight = tileHeight * this.mScaleY;

		final float[] cullingVertices = this.mCullingVertices;
		RectangularShapeCollisionChecker.fillVertices(0, 0, this.mWidth, this.mHeight, this.getLocalToSceneTransformation(), cullingVertices);

		final float layerMinX = cullingVertices[SpriteBatch.VERTEX_INDEX_X];
		final float layerMinY = cullingVertices[SpriteBatch.VERTEX_INDEX_Y];

		final float cameraMinX = pCamera.getXMin();
		final float cameraMinY = pCamera.getYMin();
		final float cameraWidth = pCamera.getWidth();
		final float cameraHeight = pCamera.getHeight();

		/* Determine the area that is visible in the camera. */
		final float firstColumnRaw = (cameraMinX - layerMinX) / scaledTileWidth;
		final int firstColumn = MathUtils.bringToBounds(0, tileColumns - 1, (int)Math.floor(firstColumnRaw));
		final int lastColumn = MathUtils.bringToBounds(0, tileColumns - 1, (int)Math.ceil(firstColumnRaw + cameraWidth / scaledTileWidth));

		final float firstRowRaw = (cameraMinY - layerMinY) / scaledTileHeight;
		final int firstRow = MathUtils.bringToBounds(0, tileRows - 1, (int)Math.floor(firstRowRaw));
		final int lastRow = MathUtils.bringToBounds(0, tileRows - 1, (int)Math.floor(firstRowRaw + cameraHeight / scaledTileHeight));

		for(int row = firstRow; row <= lastRow; row++) {
			for(int column = firstColumn; column <= lastColumn; column++) {
				this.mSpriteBatchVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, this.getSpriteBatchIndex(column, row) * SpriteBatch.VERTICES_PER_SPRITE, SpriteBatch.VERTICES_PER_SPRITE);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	void initializeTMXTileFromXML(final Attributes pAttributes, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this.addTileByGlobalTileID(SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_TILE_ATTRIBUTE_GID), pTMXTilePropertyListener);
	}

	void initializeTMXTilesFromDataString(final String pDataString, final String pDataEncoding, final String pDataCompression, final ITMXTilePropertiesListener pTMXTilePropertyListener) throws IOException, IllegalArgumentException {
		DataInputStream dataIn = null;
		try{
			InputStream in = new ByteArrayInputStream(pDataString.getBytes("UTF-8"));

			/* Wrap decoding Streams if necessary. */
			if(pDataEncoding != null) {
				if(pDataEncoding.equals(TMXConstants.TAG_DATA_ATTRIBUTE_ENCODING_VALUE_BASE64)) {
					in = new Base64InputStream(in, Base64.DEFAULT);
				} else {
					throw new IllegalArgumentException("Supplied encoding '" + pDataEncoding + "' is not supported yet.");
				}
			}

			if(pDataCompression != null) {
				if(pDataCompression.equals(TMXConstants.TAG_DATA_ATTRIBUTE_COMPRESSION_VALUE_GZIP)) {
					in = new GZIPInputStream(in);
				} else if(pDataCompression.equals(TMXConstants.TAG_DATA_ATTRIBUTE_COMPRESSION_VALUE_ZLIB)) {
					in = new InflaterInputStream(in);
				} else {
					throw new IllegalArgumentException("Supplied compression '" + pDataCompression + "' is not supported yet.");
				}
			}
			dataIn = new DataInputStream(in);

			while(this.mTilesAdded < this.mGlobalTileIDsExpected) {
				final int globalTileID = this.readGlobalTileID(dataIn);
				this.addTileByGlobalTileID(globalTileID, pTMXTilePropertyListener);
			}
		} finally {
			StreamUtils.close(dataIn);
		}
	}

	private void addTileByGlobalTileID(final int pGlobalTileID, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		final TMXTiledMap tmxTiledMap = this.mTMXTiledMap;

		final int tilesHorizontal = this.mTileColumns;

		final int column = this.mTilesAdded % tilesHorizontal;
		final int row = this.mTilesAdded / tilesHorizontal;

		final TMXTile[][] tmxTiles = this.mTMXTiles;

		final ITextureRegion tmxTileTextureRegion;
		if(pGlobalTileID == 0) {
			tmxTileTextureRegion = null;
		} else {
			tmxTileTextureRegion = tmxTiledMap.getTextureRegionFromGlobalTileID(pGlobalTileID);
		}
		final int tileHeight = this.mTMXTiledMap.getTileHeight();
		final int tileWidth = this.mTMXTiledMap.getTileWidth();

		if(this.mTexture == null) {
			this.mTexture = tmxTileTextureRegion.getTexture();
			super.initBlendFunction(this.mTexture);
		} else {
			if(this.mTexture != tmxTileTextureRegion.getTexture()) {
				throw new AndEngineRuntimeException("All TMXTiles in a TMXLayer need to be in the same TMXTileSet.");
			}
		}
		final TMXTile tmxTile = new TMXTile(pGlobalTileID, column, row, tileWidth, tileHeight, tmxTileTextureRegion);
		tmxTiles[row][column] = tmxTile;

		this.setIndex(this.getSpriteBatchIndex(column, row));
		this.drawWithoutChecks(tmxTileTextureRegion, tmxTile.getTileX(), tmxTile.getTileY(), tileWidth, tileHeight, Color.WHITE_ABGR_PACKED_FLOAT);
		this.submit(); // TODO Doesn't need to be called here, but should rather be called in a "init" step, when parsing the XML is complete.

		if(pGlobalTileID != 0) {
			/* Notify the ITMXTilePropertiesListener if it exists. */
			if(pTMXTilePropertyListener != null) {
				final TMXProperties<TMXTileProperty> tmxTileProperties = tmxTiledMap.getTMXTileProperties(pGlobalTileID);
				if(tmxTileProperties != null) {
					pTMXTilePropertyListener.onTMXTileWithPropertiesCreated(tmxTiledMap, this, tmxTile, tmxTileProperties);
				}
			}
		}

		this.mTilesAdded++;
	}

	private int getSpriteBatchIndex(final int pColumn, final int pRow) {
		return pRow * this.mTileColumns + pColumn;
	}

	private int readGlobalTileID(final DataInputStream pDataIn) throws IOException {
		final int lowestByte = pDataIn.read();
		final int secondLowestByte = pDataIn.read();
		final int secondHighestByte = pDataIn.read();
		final int highestByte = pDataIn.read();

		if(lowestByte < 0 || secondLowestByte < 0 || secondHighestByte < 0 || highestByte < 0) {
			throw new IllegalArgumentException("Couldn't read global Tile ID.");
		}

		return lowestByte | secondLowestByte <<  8 |secondHighestByte << 16 | highestByte << 24;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
