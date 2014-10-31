package org.andengine.examples;

import java.util.HashMap;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.card.Card;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 18:20:40 - 18.06.2010
 */
public class MultiTouchExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mCardDeckTexture;

	private Scene mScene;

	private HashMap<Card, ITextureRegion> mCardTotextureRegionMap;

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

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		if(MultiTouch.isSupported(this)) {
			if(MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
		}

		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mCardDeckTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 512, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mCardDeckTexture, this, "carddeck_tiled.png", 0, 0);
		this.mCardDeckTexture.load();

		this.mCardTotextureRegionMap = new HashMap<Card, ITextureRegion>();

		/* Extract the TextureRegion of each card in the whole deck. */
		for(final Card card : Card.values()) {
			final ITextureRegion cardTextureRegion = TextureRegionFactory.extractFromTexture(this.mCardDeckTexture, card.getTexturePositionX(), card.getTexturePositionY(), Card.CARD_WIDTH, Card.CARD_HEIGHT);
			this.mCardTotextureRegionMap.put(card, cardTextureRegion);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setOnAreaTouchTraversalFrontToBack();

		this.addCard(Card.CLUB_ACE, 200, 100);
		this.addCard(Card.HEART_ACE, 200, 260);
		this.addCard(Card.DIAMOND_ACE, 440, 100);
		this.addCard(Card.SPADE_ACE, 440, 260);

		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		return this.mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void addCard(final Card pCard, final int pX, final int pY) {
		final Sprite sprite = new Sprite(pX, pY, this.mCardTotextureRegionMap.get(pCard), this.getVertexBufferObjectManager()) {
			boolean mGrabbed = false;

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						this.setScale(1.25f);
						this.mGrabbed = true;
						break;
					case TouchEvent.ACTION_MOVE:
						if(this.mGrabbed) {
							this.setPosition(pSceneTouchEvent.getX() - Card.CARD_WIDTH / 2, pSceneTouchEvent.getY() - Card.CARD_HEIGHT / 2);
						}
						break;
					case TouchEvent.ACTION_UP:
						if(this.mGrabbed) {
							this.mGrabbed = false;
							this.setScale(1.0f);
						}
						break;
				}
				return true;
			}
		};

		this.mScene.attachChild(sprite);
		this.mScene.registerTouchArea(sprite);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
