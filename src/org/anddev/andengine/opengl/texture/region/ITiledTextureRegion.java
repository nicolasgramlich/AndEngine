package org.anddev.andengine.opengl.texture.region;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:31:52 - 08.08.2011
 */
public interface ITiledTextureRegion extends ITextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getTileIndex();
	public void setTileIndex(final int pTileIndex);
	public void nextTile();
	public int getTileCount();

	@Override
	public ITiledTextureRegion clone();
}
