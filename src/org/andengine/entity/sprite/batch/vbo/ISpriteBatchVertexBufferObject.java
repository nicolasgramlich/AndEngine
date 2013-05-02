package org.andengine.entity.sprite.batch.vbo;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:35:06 - 29.03.2012
 */
public interface ISpriteBatchVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getBufferDataOffset();
	public void setBufferDataOffset(final int pBufferDataOffset);

	public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pColorABGRPackedInt);
	public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pColorABGRPackedInt);
}