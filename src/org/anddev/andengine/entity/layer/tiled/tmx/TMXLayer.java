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
import org.anddev.andengine.util.SAXUtils;
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
	
	private final int mTileColumns;
	private final int mTileRows;
	
	private final TextureRegion[][] mTextureRegions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXLayer(final TMXTiledMap pTMXTiledMap, final Attributes pAttributes) {
		super(0, 0, 0, 0, null);
		
		this.mTMXTiledMap = pTMXTiledMap;
		this.mName = pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_NAME);
		this.mTileColumns = SAXUtils.getIntAttribute(pAttributes, TAG_LAYER_ATTRIBUTE_WIDTH, -1);
		this.mTileRows = SAXUtils.getIntAttribute(pAttributes, TAG_LAYER_ATTRIBUTE_HEIGHT, -1);
		this.mTextureRegions = new TextureRegion[this.mTileColumns][this.mTileRows];
		
		super.mWidth = pTMXTiledMap.getTileWidth() * this.mTileColumns;
		final float width = super.mWidth;
		super.mBaseWidth = width;

		super.mHeight = pTMXTiledMap.getTileWidth() * this.mTileColumns;
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
	
	public int getTileColumns() {
		return this.mTileColumns;
	}
	
	public int getTileRows() {
		return this.mTileRows;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateVertexBuffer() {
		/* Nothing. */
	}
	
	@Override
	protected void onInitDraw(GL10 pGL) {
		super.onInitDraw(pGL);

		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
	}
	
	@Override
	protected void onApplyVertices(GL10 pGL) {
		/* Nothing. */ 
	}
	
	@Override
	protected void drawVertices(GL10 pGL) {
		final TextureRegion[][] textureRegions = this.mTextureRegions;

		final int tileColumns = this.mTileColumns;
		final int tileRows = this.mTileRows;
		
		final int tileWidth = this.mTMXTiledMap.getTileWidth();
		final int tileHeight = this.mTMXTiledMap.getTileHeight();
		final int totalWidth = tileColumns * tileWidth;
		final int totalHeight = tileRows * tileHeight;

		this.applySharedVertexBuffer(pGL);
		
		pGL.glTranslatef(totalWidth, totalHeight, 0);

		for(int row = tileRows - 1; row >= 0; row--) {
			pGL.glTranslatef(0, -tileHeight, 0);

			final TextureRegion[] textureRegionRow = textureRegions[row];
			
			for(int column = tileColumns - 1; column >= 0; column--) {
				pGL.glTranslatef(-tileWidth, 0, 0);

				final TextureRegion textureRegion = textureRegionRow[column];
				if(textureRegion != null) {					
					textureRegion.onApply(pGL);

					pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				}
			}

			pGL.glTranslatef(totalWidth, 0, 0);
		}
		pGL.glLoadIdentity();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void applySharedVertexBuffer(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mTMXTiledMap.getSharedVertexBuffer().selectOnHardware(gl11);
			GLHelper.vertexZeroPointer(gl11);
		} else {
			GLHelper.vertexPointer(pGL, this.mTMXTiledMap.getSharedVertexBuffer().getFloatBuffer());
		}
	}

	public void initializeTextureRegions(final String pString) throws IOException, IllegalArgumentException {
		final TMXTiledMap tmxTiledMap = this.mTMXTiledMap;
		final int tilesHorizontal = this.mTileColumns;
		final int tilesVertical = this.mTileRows;
		final TextureRegion[][] textureRegions = this.mTextureRegions;
		final byte[] globalTileIDFetcher = new byte[4];

		final DataInputStream dataIn = new DataInputStream(new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(pString.getBytes("UTF-8")), Base64.DEFAULT)));

		int globalTileIDsRead = 0;
		while(dataIn.read(globalTileIDFetcher) == BYTES_PER_GLOBALTILEID) {
			final int globalTileID = calculateGlobalTileID(globalTileIDFetcher);

			if(globalTileID != 0) {
				final int column = globalTileIDsRead % tilesHorizontal;
				final int row = globalTileIDsRead / tilesHorizontal;
				textureRegions[row][column] = tmxTiledMap.getTextureRegionFromGlobalTileID(globalTileID);
			}
			globalTileIDsRead++;
		}

		final int expectedGlobalTileIDs = tilesHorizontal * tilesVertical;
		if(globalTileIDsRead != expectedGlobalTileIDs) {
			throw new IllegalArgumentException("Read: " + globalTileIDsRead + " GlobalTileIDs. Expected: " + expectedGlobalTileIDs);
		}
	}

	private int calculateGlobalTileID(final byte[] globalTileIDFetcher) {
		return globalTileIDFetcher[0] | globalTileIDFetcher[1] << 8 | globalTileIDFetcher[2] << 16 | globalTileIDFetcher[3] << 24;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
