package org.anddev.andengine.entity.layer.tiled.tmx;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.util.Base64;
import org.anddev.andengine.util.Base64InputStream;
import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 20:27:31 - 20.07.2010
 */
public class TMXLayer implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mName;
	private final int mWidth;
	private final int mHeight;
	private final int[][] mGlobalTileIDs;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXLayer(final Attributes pAttributes) {
		this.mName = pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_NAME);
		this.mWidth = Integer.parseInt(pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_WIDTH));
		this.mHeight = Integer.parseInt(pAttributes.getValue("", TAG_LAYER_ATTRIBUTE_HEIGHT));
		this.mGlobalTileIDs = new int[this.mWidth][this.mHeight];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final String getName() {
		return this.mName;
	}

	public final int getWidth() {
		return this.mWidth;
	}

	public final int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void setData(final String pString) throws IOException {
		final int width = this.mWidth;
		final int height = this.mHeight;
		final int[][] globalTileIDs = this.mGlobalTileIDs;
		final byte[] globalTileIDFetcher = new byte[4];

		final DataInputStream dataIn = new DataInputStream(new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(pString.getBytes("UTF-8")), Base64.DEFAULT)));

		int globalTileIDsRead = 0;
		while(dataIn.read(globalTileIDFetcher) == BYTES_PER_GLOBALTILEID) {
			final int globalTileID = globalTileIDFetcher[0] |
			globalTileIDFetcher[1] << 8 |
			globalTileIDFetcher[2] << 16 |
			globalTileIDFetcher[3] << 24;

			globalTileIDs[globalTileIDsRead / width][globalTileIDsRead % width] = globalTileID;
			globalTileIDsRead++;
		}

		final int expectedGlobalTileIDs = width * height;
		if(globalTileIDsRead != expectedGlobalTileIDs) {
			throw new IllegalArgumentException("Read: " + globalTileIDsRead + " GlobalTileIDs. Expected: " + expectedGlobalTileIDs);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
