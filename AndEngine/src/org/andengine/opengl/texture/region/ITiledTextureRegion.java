package org.andengine.opengl.texture.region;

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

	public int getCurrentTileIndex();
	public void setCurrentTileIndex(final int pCurrentTileIndex);
	public void nextTile();
	public ITextureRegion getTextureRegion(final int pTileIndex);
	public int getTileCount();

	public float getTextureX(final int pTileIndex);
	public float getTextureY(final int pTileIndex);

	public void setTextureX(final int pTileIndex, final float pTextureX);
	public void setTextureY(final int pTileIndex, final float pTextureY);
	public void setTexturePosition(final int pTileIndex, final float pTextureX, final float pTextureY);

	/**
	 * Note: Takes {@link ITiledTextureRegion#getScale(int)} into account!
	 */
	public float getWidth(final int pTileIndex);
	/**
	 * Note: Takes {@link ITiledTextureRegion#getScale(int)} into account!
	 */
	public float getHeight(final int pTileIndex);

	public void setTextureWidth(final int pTileIndex, final float pWidth);
	public void setTextureHeight(final int pTileIndex, final float pHeight);
	public void setTextureSize(final int pTileIndex, final float pWidth, final float pHeight);

	public void set(final int pTileIndex, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight);

	public float getU(final int pTileIndex);
	public float getU2(final int pTileIndex);
	public float getV(final int pTileIndex);
	public float getV2(final int pTileIndex);

	public boolean isScaled(final int pTileIndex);
	public float getScale(final int pTileIndex);
	public boolean isRotated(final int pTileIndex);

	@Override
	public ITiledTextureRegion deepCopy();
}
