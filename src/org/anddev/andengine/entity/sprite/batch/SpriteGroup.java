package org.anddev.andengine.entity.sprite.batch;

import java.util.ArrayList;
import java.util.Collection;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.ParameterCallable;
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

	public void add(final BaseSprite pBaseSprite) {
		this.mSprites.add(pBaseSprite);
	}

	public void addAll(final Collection<? extends BaseSprite> pBaseSprites) {
		this.mSprites.addAll(pBaseSprites);
	}

	public void remove(final BaseSprite pBaseSprite) {
		this.mSprites.remove(pBaseSprite);
	}

	public void removeAll(final Collection<? extends BaseSprite> pBaseSprites) {
		this.mSprites.removeAll(pBaseSprites);
	}

	public void remove(final BaseSprite pBaseSprite, final ParameterCallable<BaseSprite> pBaseSpriteParameterCallable) {
		this.mSprites.remove(pBaseSprite, pBaseSpriteParameterCallable);
	}

	public void remove(final IMatcher<BaseSprite> pBaseSpriteMatcher) {
		this.mSprites.remove(pBaseSpriteMatcher);
	}

	public void removeAll(final IMatcher<BaseSprite> pBaseSpriteMatcher) {
		this.mSprites.removeAll(pBaseSpriteMatcher);
	}

	public void removeAll(final IMatcher<BaseSprite> pBaseSpriteMatcher, final ParameterCallable<BaseSprite> pBaseSpriteParameterCallable) {
		this.mSprites.removeAll(pBaseSpriteMatcher, pBaseSpriteParameterCallable);
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
			final BaseSprite baseSprite = sprites.get(i);
			if(baseSprite.isVisible()) {
				super.draw(baseSprite);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
