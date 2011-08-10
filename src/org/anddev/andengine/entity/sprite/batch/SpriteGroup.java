package org.anddev.andengine.entity.sprite.batch;

import java.util.ArrayList;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.util.SmartList;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:10:35 - 15.06.2011
 */
public class SpriteGroup extends DynamicSpriteBatch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteGroup(final ITexture pTexture, final int pCapacity) {
		super(pTexture, pCapacity);
	}

	public SpriteGroup(final ITexture pTexture, final int pCapacity, final SpriteBatchMesh pSpriteBatchMesh) {
		super(pTexture, pCapacity, pSpriteBatchMesh);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Instead use {@link SpriteGroup#attachChild(BaseSprite)}.
	 */
	@Override
	@Deprecated
	public void attachChild(final IEntity pEntity) throws IllegalArgumentException {
		if(pEntity instanceof Sprite) {
			this.attachChild((Sprite)pEntity);
		} else {
			throw new IllegalArgumentException("A SpriteGroup can only handle children of type BaseSprite or subclasses of BaseSprite, like Sprite, TiledSprite or AnimatedSprite.");
		}
	}

	public void attachChild(final Sprite pBaseSprite) {
		this.assertCapacity();
		this.assertTexture(pBaseSprite.getTextureRegion());
		super.attachChild(pBaseSprite);
	}

	public void attachChildren(final ArrayList<? extends Sprite> pBaseSprites) {
		final int baseSpriteCount = pBaseSprites.size();
		for(int i = 0; i < baseSpriteCount; i++) {
			this.attachChild(pBaseSprites.get(i));
		}
	}

	@Override
	protected boolean onUpdateSpriteBatch() {
		final SmartList<IEntity> children = this.mChildren;
		if(children == null) {
			return false;
		} else {
			final int childCount = children.size();
			for(int i = 0; i < childCount; i++) {
				super.drawWithoutChecks((Sprite)children.get(i));
			}
			return true;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void assertCapacity() {
		if(this.getChildCount() >= this.mCapacity) {
			throw new IllegalStateException("This SpriteGroup has already reached its capacity (" + this.mCapacity + ") !");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
