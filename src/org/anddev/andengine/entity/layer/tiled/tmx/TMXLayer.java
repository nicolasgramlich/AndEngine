package org.anddev.andengine.entity.layer.tiled.tmx;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Base64;
import org.anddev.andengine.util.Base64InputStream;
import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 20:27:31 - 20.07.2010
 */
public class TMXLayer extends RectangularShape implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final TMXTiledMap mTMXTiledMap;

	private final String mName;
	
	private final int mTilesHorizontal;
	private final int mTilesVertical;
	
	private final TextureRegion[][] mTextureRegions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXLayer(final TMXTiledMap pTMXTiledMap, final Attributes pAttributes) {
		super(0, 0, 0, 0, null);
		
		this.mTMXTiledMap = pTMXTiledMap;
		this.mName = pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_NAME);
		this.mTilesHorizontal = Integer.parseInt(pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_WIDTH));
		this.mTilesVertical = Integer.parseInt(pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_HEIGHT));
		this.mTextureRegions = new TextureRegion[this.mTilesHorizontal][this.mTilesVertical];
		
		super.mWidth = pTMXTiledMap.getTileWidth() * this.mTilesHorizontal;
		final float width = super.mWidth;
		super.mBaseWidth = width;

		super.mHeight = pTMXTiledMap.getTileWidth() * this.mTilesHorizontal;
		final float height = super.mHeight;
		super.mBaseHeight = height;
		
		this.mRotationCenterX = width * 0.5f;
		this.mRotationCenterY = height * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public String getName() {
		return this.mName;
	}
	
	public int getTilesHorizontal() {
		return this.mTilesHorizontal;
	}
	
	public int getTilesVertical() {
		return this.mTilesVertical;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateVertexBuffer() {
		/* Nothing. */
	}

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		pGL.glPushMatrix();

		final TextureRegion[][] textureRegions = this.mTextureRegions;

		final int tilesHorizontal = this.mTilesHorizontal;
		final int tilesVertical = this.mTilesVertical;
		
		final int tileWidth = this.mTMXTiledMap.getTileWidth();
		final int tileHeight = this.mTMXTiledMap.getTileHeight();
		final int totalWidth = tilesHorizontal * tileWidth;
		final int totalHeight = tilesVertical * tileHeight;
		
		pGL.glTranslatef(totalWidth, totalHeight, 0);

		for(int y = tilesVertical - 1; y >= 0; y--) {
			pGL.glTranslatef(0, -tileHeight, 0);

			for(int x = tilesHorizontal - 1; x >= 0; x--) {
				pGL.glTranslatef(-tileWidth, 0, 0);

				final TextureRegion textureRegion = textureRegions[x][y];
				if(textureRegion != null) {
					this.initDraw(pGL);
					this.applySharedVertexBuffer(pGL);
					
					textureRegion.onApply(pGL);

					pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				}
			}

			pGL.glTranslatef(totalWidth, 0, 0);
		}
		
		pGL.glPopMatrix();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initDraw(final GL10 pGL) {
		GLHelper.setColor(pGL, 0.5f, 0.5f, 0.5f, 0.5f);

		GLHelper.enableVertexArray(pGL);
		GLHelper.blendFunction(pGL, GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
	}

	private void applySharedVertexBuffer(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mTMXTiledMap.getSharedVertexBuffer().selectOnHardware(gl11);
			GLHelper.vertexZeroPointer(gl11);
		} else {
			GLHelper.vertexPointer(pGL, this.mTMXTiledMap.getSharedVertexBuffer().getFloatBuffer());
		}
	}

	public void setData(final String pString) throws IOException {
		final TMXTiledMap tmxTiledMap = this.mTMXTiledMap;
		final int tilesHorizontal = this.mTilesHorizontal;
		final int tilesVertical = this.mTilesVertical;
		final TextureRegion[][] globalTileIDs = this.mTextureRegions;
		final byte[] globalTileIDFetcher = new byte[4];

		final DataInputStream dataIn = new DataInputStream(new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(pString.getBytes("UTF-8")), Base64.DEFAULT)));

		int globalTileIDsRead = 0;
		while(dataIn.read(globalTileIDFetcher) == BYTES_PER_GLOBALTILEID) {
			final int globalTileID = globalTileIDFetcher[0] |
			globalTileIDFetcher[1] << 8 |
			globalTileIDFetcher[2] << 16 |
			globalTileIDFetcher[3] << 24;

			if(globalTileID != 0) {
				globalTileIDs[globalTileIDsRead / tilesHorizontal][globalTileIDsRead % tilesHorizontal] = tmxTiledMap.getTextureRegionFromGlobalTileID(globalTileID);
			}
			globalTileIDsRead++;
		}

		final int expectedGlobalTileIDs = tilesHorizontal * tilesVertical;
		if(globalTileIDsRead != expectedGlobalTileIDs) {
			throw new IllegalArgumentException("Read: " + globalTileIDsRead + " GlobalTileIDs. Expected: " + expectedGlobalTileIDs);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
