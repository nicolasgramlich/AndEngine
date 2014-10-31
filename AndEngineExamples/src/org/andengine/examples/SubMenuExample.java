package org.andengine.examples;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:33:33 - 01.04.2010
 */
public class SubMenuExample extends MenuExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MENU_QUIT_OK = MenuExample.MENU_QUIT + 1;
	private static final int MENU_QUIT_BACK = MENU_QUIT_OK + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private MenuScene mSubMenuScene;

	private BitmapTextureAtlas mSubMenuTexture;
	private ITextureRegion mMenuOkTextureRegion;
	private ITextureRegion mMenuBackTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreateResources() {
		super.onCreateResources();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mSubMenuTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mMenuOkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSubMenuTexture, this, "menu_ok.png", 0, 0);
		this.mMenuBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSubMenuTexture, this, "menu_back.png", 0, 50);
		this.mSubMenuTexture.load();
	}

	@Override
	protected void createMenuScene() {
		super.createMenuScene();

		this.mSubMenuScene = new MenuScene(this.mCamera);
		this.mSubMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT_OK, this.mMenuOkTextureRegion, this.getVertexBufferObjectManager()));
		this.mSubMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT_BACK, this.mMenuBackTextureRegion, this.getVertexBufferObjectManager()));
		this.mSubMenuScene.setMenuAnimator(new SlideMenuAnimator());
		this.mSubMenuScene.buildAnimations();

		this.mSubMenuScene.setBackgroundEnabled(false);

		this.mSubMenuScene.setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_RESET:
				this.mMainScene.reset();

				this.mMenuScene.back();
				return true;
			case MENU_QUIT:
				pMenuScene.setChildSceneModal(this.mSubMenuScene);
				return true;
			case MENU_QUIT_BACK:
				this.mSubMenuScene.back();
				return true;
			case MENU_QUIT_OK:
				this.finish();
				return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
