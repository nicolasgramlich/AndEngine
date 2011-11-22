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
	
	public int getWidth(final int pTileIndex);
	public int getHeight(final int pTileIndex);

	public void setWidth(final int pTileIndex, final int pWidth);
	public void setHeight(final int pTileIndex, final int pHeight);
	public void setSize(final int pTileIndex, final int pWidth, final int pHeight);

	public int getX(final int pTileIndex);
	public int getY(final int pTileIndex);

	public void setX(final int pTileIndex, final int pX);
	public void setY(final int pTileIndex, final int pY);
	public void setPosition(final int pTileIndex, final int pX, final int pY);

	public void set(final int pTileIndex, final int pX, final int pY, final int pWidth, final int pHeight);

	public float getU(final int pTileIndex);
	public float getU2(final int pTileIndex);
	public float getV(final int pTileIndex);
	public float getV2(final int pTileIndex);

	public boolean isRotated(final int pTileIndex);

	@Override
	public ITiledTextureRegion deepCopy();
}
