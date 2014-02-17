package org.andengine.entity.sprite.vbo;

import org.andengine.entity.sprite.TiledSprite;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:39:06 - 28.03.2012
 */
public interface ITiledSpriteVertexBufferObject extends ISpriteVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateColor(final TiledSprite pTiledSprite);
	public void onUpdateVertices(final TiledSprite pTiledSprite);
	public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite);
}