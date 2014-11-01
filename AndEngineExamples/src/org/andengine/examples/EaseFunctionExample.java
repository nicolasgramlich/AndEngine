package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.ease.EaseBackIn;
import org.andengine.util.modifier.ease.EaseBackInOut;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseBounceIn;
import org.andengine.util.modifier.ease.EaseBounceInOut;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseCircularIn;
import org.andengine.util.modifier.ease.EaseCircularInOut;
import org.andengine.util.modifier.ease.EaseCircularOut;
import org.andengine.util.modifier.ease.EaseCubicIn;
import org.andengine.util.modifier.ease.EaseCubicInOut;
import org.andengine.util.modifier.ease.EaseCubicOut;
import org.andengine.util.modifier.ease.EaseElasticIn;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.EaseExponentialIn;
import org.andengine.util.modifier.ease.EaseExponentialInOut;
import org.andengine.util.modifier.ease.EaseExponentialOut;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.EaseQuadIn;
import org.andengine.util.modifier.ease.EaseQuadInOut;
import org.andengine.util.modifier.ease.EaseQuadOut;
import org.andengine.util.modifier.ease.EaseQuartIn;
import org.andengine.util.modifier.ease.EaseQuartInOut;
import org.andengine.util.modifier.ease.EaseQuartOut;
import org.andengine.util.modifier.ease.EaseQuintIn;
import org.andengine.util.modifier.ease.EaseQuintInOut;
import org.andengine.util.modifier.ease.EaseQuintOut;
import org.andengine.util.modifier.ease.EaseSineIn;
import org.andengine.util.modifier.ease.EaseSineInOut;
import org.andengine.util.modifier.ease.EaseSineOut;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.EaseStrongOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.graphics.Color;
import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 15:12:16 - 30.07.2010
 */
public class EaseFunctionExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private Font mFont;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mBadgeTextureRegion;
	private ITextureRegion mNextTextureRegion;

	private static final IEaseFunction[][] EASEFUNCTIONS = new IEaseFunction[][]{
		new IEaseFunction[] {
				EaseLinear.getInstance(),
				EaseLinear.getInstance(),
				EaseLinear.getInstance()
		},
		new IEaseFunction[] {
				EaseBackIn.getInstance(),
				EaseBackOut.getInstance(),
				EaseBackInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseBounceIn.getInstance(),
				EaseBounceOut.getInstance(),
				EaseBounceInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseCircularIn.getInstance(),
				EaseCircularOut.getInstance(),
				EaseCircularInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseCubicIn.getInstance(),
				EaseCubicOut.getInstance(),
				EaseCubicInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseElasticIn.getInstance(),
				EaseElasticOut.getInstance(),
				EaseElasticInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseExponentialIn.getInstance(),
				EaseExponentialOut.getInstance(),
				EaseExponentialInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseQuadIn.getInstance(),
				EaseQuadOut.getInstance(),
				EaseQuadInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseQuartIn.getInstance(),
				EaseQuartOut.getInstance(),
				EaseQuartInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseQuintIn.getInstance(),
				EaseQuintOut.getInstance(),
				EaseQuintInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseSineIn.getInstance(),
				EaseSineOut.getInstance(),
				EaseSineInOut.getInstance()
		},
		new IEaseFunction[] {
				EaseStrongIn.getInstance(),
				EaseStrongOut.getInstance(),
				EaseStrongInOut.getInstance()
		}
	};

	private int mCurrentEaseFunctionSet = 0;

	private final Sprite[] mBadges = new Sprite[3];
	private final Text[] mEaseFunctionNameTexts = new Text[3];

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
		/* The font. */
		final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mFont = new Font(this.getFontManager(), fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.WHITE);
		this.mFont.load();

		/* The textures. */
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mNextTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "next.png", 0, 0);
		this.mBadgeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "badge.png", 97, 0);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final HUD hud = new HUD();

		final Sprite nextSprite = new Sprite(CAMERA_WIDTH - 100 - this.mNextTextureRegion.getWidth(), 0, this.mNextTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					EaseFunctionExample.this.next();
				}
				return true;
			};
		};
		final Sprite previousSprite = new Sprite(100, 0, this.mNextTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					EaseFunctionExample.this.previous();
				}
				return true;
			};
		};
		previousSprite.setFlippedHorizontal(true);

		hud.attachChild(nextSprite);
		hud.attachChild(previousSprite);

		hud.registerTouchArea(nextSprite);
		hud.registerTouchArea(previousSprite);

		this.mCamera.setHUD(hud);

		/* Create the sprites that will be moving. */

		this.mBadges[0] = new Sprite(0, CAMERA_HEIGHT - 300, this.mBadgeTextureRegion, this.getVertexBufferObjectManager());
		this.mBadges[1] = new Sprite(0, CAMERA_HEIGHT - 200, this.mBadgeTextureRegion, this.getVertexBufferObjectManager());
		this.mBadges[2] = new Sprite(0, CAMERA_HEIGHT - 100, this.mBadgeTextureRegion, this.getVertexBufferObjectManager());

		this.mEaseFunctionNameTexts[0] = new Text(0, CAMERA_HEIGHT - 250, this.mFont, "Function", 20, this.getVertexBufferObjectManager());
		this.mEaseFunctionNameTexts[1] = new Text(0, CAMERA_HEIGHT - 150, this.mFont, "Function", 20, this.getVertexBufferObjectManager());
		this.mEaseFunctionNameTexts[2] = new Text(0, CAMERA_HEIGHT - 50, this.mFont, "Function", 20, this.getVertexBufferObjectManager());

		scene.attachChild(this.mBadges[0]);
		scene.attachChild(this.mBadges[1]);
		scene.attachChild(this.mBadges[2]);
		scene.attachChild(this.mEaseFunctionNameTexts[0]);
		scene.attachChild(this.mEaseFunctionNameTexts[1]);
		scene.attachChild(this.mEaseFunctionNameTexts[2]);
		scene.attachChild(new Line(0, CAMERA_HEIGHT - 110, CAMERA_WIDTH, CAMERA_HEIGHT - 110, this.getVertexBufferObjectManager()));
		scene.attachChild(new Line(0, CAMERA_HEIGHT - 210, CAMERA_WIDTH, CAMERA_HEIGHT - 210, this.getVertexBufferObjectManager()));
		scene.attachChild(new Line(0, CAMERA_HEIGHT - 310, CAMERA_WIDTH, CAMERA_HEIGHT - 310, this.getVertexBufferObjectManager()));

		return scene;
	}

	@Override
	public void onGameCreated() {
		this.reanimate();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void next() {
		this.mCurrentEaseFunctionSet++;
		this.mCurrentEaseFunctionSet %= EASEFUNCTIONS.length;
		this.reanimate();
	}

	public void previous() {
		this.mCurrentEaseFunctionSet--;
		if(this.mCurrentEaseFunctionSet < 0) {
			this.mCurrentEaseFunctionSet += EASEFUNCTIONS.length;
		}

		this.reanimate();
	}

	private void reanimate() {
		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				final IEaseFunction[] currentEaseFunctionsSet = EASEFUNCTIONS[EaseFunctionExample.this.mCurrentEaseFunctionSet];
				final Text[] easeFunctionNameTexts = EaseFunctionExample.this.mEaseFunctionNameTexts;
				final Sprite[] faces = EaseFunctionExample.this.mBadges;

				for(int i = 0; i < 3; i++) {
					easeFunctionNameTexts[i].setText(currentEaseFunctionsSet[i].getClass().getSimpleName());
					final Sprite face = faces[i];
					face.clearEntityModifiers();

					final float y = face.getY();
					face.setPosition(0, y);
					face.registerEntityModifier(new MoveModifier(3, 0, CAMERA_WIDTH - face.getWidth(), y, y, currentEaseFunctionsSet[i]));
				}
			}
		});
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
