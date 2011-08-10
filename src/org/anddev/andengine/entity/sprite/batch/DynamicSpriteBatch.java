package org.anddev.andengine.entity.sprite.batch;

import org.anddev.andengine.opengl.texture.ITexture;

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

	public DynamicSpriteBatch(ITexture pTexture, int pCapacity) {
		super(pTexture, pCapacity);
	}

	public DynamicSpriteBatch(ITexture pTexture, int pCapacity, SpriteBatchMesh pSpriteBatchMesh) {
		super(pTexture, pCapacity, pSpriteBatchMesh);
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
	protected void begin() {
		super.begin();

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
