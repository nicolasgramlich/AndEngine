package org.anddev.andengine.entity.sprite.batch;

import java.util.ArrayList;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.util.SmartList;

/**
 * @author Nicolas Gramlich
 * @since 12:10:35 - 15.06.2011
 */
public class SpriteGroup extends SpriteBatch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SmartList<BaseSprite> mSprites;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteGroup(final Texture pTexture, final int pCapacity) {
		super(pTexture, pCapacity);

		this.mSprites = new SmartList<BaseSprite>(pCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void draw(final BaseSprite pBaseSprite) {
		this.mSprites.add(pBaseSprite);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		final ArrayList<BaseSprite> sprites = this.mSprites;
		final int spriteCount = sprites.size();
		for(int i = 0; i < spriteCount; i++) {
			sprites.get(i).onUpdate(pSecondsElapsed);
		}
	}

	@Override
	protected void onDrawSpriteBatch() {
		final ArrayList<BaseSprite> sprites = this.mSprites;
		final int spriteCount = sprites.size();
		for(int i = 0; i < spriteCount; i++) {
			super.draw(sprites.get(i));
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
