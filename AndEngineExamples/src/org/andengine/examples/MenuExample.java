package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;
import android.view.KeyEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 01:30:15 - 02.04.2010
 */
public class MenuExample extends SimpleBaseGameActivity implements IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;

	protected Scene mMainScene;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	protected MenuScene mMenuScene;

	private BitmapTextureAtlas mMenuTexture;
	protected ITextureRegion mMenuResetTextureRegion;
	protected ITextureRegion mMenuQuitTextureRegion;

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
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box_menu.png", 0, 0);
		this.mBitmapTextureAtlas.load();

		this.mMenuTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mMenuResetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "menu_reset.png", 0, 0);
		this.mMenuQuitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "menu_quit.png", 0, 50);
		this.mMenuTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.createMenuScene();

		/* Just a simple scene with an animated face flying around. */
		this.mMainScene = new Scene();
		this.mMainScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final Sprite face = new Sprite(0, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face.registerEntityModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
		this.mMainScene.attachChild(face);

		return this.mMainScene;
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mMainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMenuScene.back();
			} else {
				/* Attach the menu. */
				this.mMainScene.setChildScene(this.mMenuScene, false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_RESET:
				/* Restart the animation. */
				this.mMainScene.reset();

				/* Remove the menu and reset it. */
				this.mMainScene.clearChildScene();
				this.mMenuScene.reset();
				return true;
			case MENU_QUIT:
				/* End Activity. */
				this.finish();
				return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void createMenuScene() {
		this.mMenuScene = new MenuScene(this.mCamera);

		final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, this.mMenuResetTextureRegion, this.getVertexBufferObjectManager());
		resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(resetMenuItem);

		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, this.mMenuQuitTextureRegion, this.getVertexBufferObjectManager());
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(quitMenuItem);

		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled(false);

		this.mMenuScene.setOnMenuItemClickListener(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}