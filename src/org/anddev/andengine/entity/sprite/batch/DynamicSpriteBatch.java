package org.anddev.andengine.entity.sprite.batch;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.buffer.SpriteBatchTextureRegionBuffer;
import org.anddev.andengine.opengl.vertex.SpriteBatchVertexBuffer;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:48:21 - 27.07.2011
 */
public abstract class DynamicSpriteBatch extends SpriteBatch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity) {
		super(pTexture, pCapacity);
	}

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final SpriteBatchVertexBuffer pSpriteBatchVertexBuffer, final SpriteBatchTextureRegionBuffer pSpriteBatchTextureRegionBuffer) {
		super(pTexture, pCapacity, pSpriteBatchVertexBuffer, pSpriteBatchTextureRegionBuffer);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * @return <code>true</code> to submit, if you made any changes, or <code>false</code> otherwise.
	 */
	protected abstract boolean onUpdateSpriteBatch();

	@Override
	protected void begin(final GL10 pGL) {
		super.begin(pGL);

		if(this.onUpdateSpriteBatch()) {
			this.submit();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
