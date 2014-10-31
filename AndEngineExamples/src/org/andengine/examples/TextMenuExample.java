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
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import android.view.KeyEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 01:30:15 - 02.04.2010
 */
public class TextMenuExample extends SimpleBaseGameActivity implements IOnMenuItemClickListener {
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

	private Camera mCamera;

	protected Scene mMainScene;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	private Font mFont;

	protected MenuScene mMenuScene;

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
		FontFactory.setAssetBasePath("font/");

		final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), "Plok.ttf", 48, true, android.graphics.Color.WHITE);
		this.mFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box_menu.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mMenuScene = this.createMenuScene();

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

	protected MenuScene createMenuScene() {
		final MenuScene menuScene = new MenuScene(this.mCamera);

		final IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESET, this.mFont, "RESET", this.getVertexBufferObjectManager()), new Color(1,0,0), new Color(0,0,0));
		resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(resetMenuItem);

		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, this.mFont, "QUIT", this.getVertexBufferObjectManager()), new Color(1,0,0), new Color(0,0,0));
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(quitMenuItem);

		menuScene.buildAnimations();

		menuScene.setBackgroundEnabled(false);

		menuScene.setOnMenuItemClickListener(this);
		return menuScene;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}