package org.andengine.examples.game.snake;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.game.snake.adt.Direction;
import org.andengine.examples.game.snake.adt.SnakeSuicideException;
import org.andengine.examples.game.snake.entity.Frog;
import org.andengine.examples.game.snake.entity.Snake;
import org.andengine.examples.game.snake.entity.SnakeHead;
import org.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.graphics.Color;
import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 02:26:05 - 08.07.2010
 */
public class SnakeGameActivity extends SimpleBaseGameActivity implements SnakeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = CELLS_HORIZONTAL * CELL_WIDTH; // 640
	private static final int CAMERA_HEIGHT = CELLS_VERTICAL * CELL_HEIGHT; // 480

	private static final int LAYER_COUNT = 4;

	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_FOOD = LAYER_BACKGROUND + 1;
	private static final int LAYER_SNAKE = LAYER_FOOD + 1;
	private static final int LAYER_SCORE = LAYER_SNAKE + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private DigitalOnScreenControl mDigitalOnScreenControl;

	private Font mFont;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mTailPartTextureRegion;
	private TiledTextureRegion mHeadTextureRegion;
	private TiledTextureRegion mFrogTextureRegion;

	private BitmapTextureAtlas mBackgroundTexture;
	private ITextureRegion mBackgroundTextureRegion;

	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private Scene mScene;

	private Snake mSnake;
	private Frog mFrog;

	private int mScore = 0;
	private Text mScoreText;

	private Sound mGameOverSound;
	private Sound mMunchSound;
	protected boolean mGameRunning;
	private Text mGameOverText;

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
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		/* Load the font we are going to use. */
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "Plok.ttf", 32, true, Color.WHITE);
		this.mFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		/* Load all the textures this game needs. */
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128);
		this.mHeadTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "snake_head.png", 0, 0, 3, 1);
		this.mTailPartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "snake_tailpart.png", 96, 0);
		this.mFrogTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "frog.png", 0, 64, 3, 1);
		this.mBitmapTextureAtlas.load();

		this.mBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 512);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "snake_background.png", 0, 0);
		this.mBackgroundTexture.load();

		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		/* Load all the sounds this game needs. */
		try {
			SoundFactory.setAssetBasePath("mfx/");
			this.mGameOverSound = SoundFactory.createSoundFromAsset(this.getSoundManager(), this, "game_over.ogg");
			this.mMunchSound = SoundFactory.createSoundFromAsset(this.getSoundManager(), this, "munch.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		for(int i = 0; i < LAYER_COUNT; i++) {
			this.mScene.attachChild(new Entity());
		}

		/* No background color needed as we have a fullscreen background sprite. */
		this.mScene.setBackgroundEnabled(false);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));

		/* The ScoreText showing how many points the pEntity scored. */
		this.mScoreText = new Text(5, 5, this.mFont, "Score: 0", "Score: XXXX".length(), this.getVertexBufferObjectManager());
		this.mScoreText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mScoreText.setAlpha(0.5f);
		this.mScene.getChildByIndex(LAYER_SCORE).attachChild(this.mScoreText);

		/* The Snake. */
		this.mSnake = new Snake(Direction.RIGHT, 0, CELLS_VERTICAL / 2, this.mHeadTextureRegion, this.mTailPartTextureRegion, this.getVertexBufferObjectManager());
		this.mSnake.getHead().animate(200);
		/* Snake starts with one tail. */
		this.mSnake.grow();
		this.mScene.getChildByIndex(LAYER_SNAKE).attachChild(this.mSnake);

		/* A frog to approach and eat. */
		this.mFrog = new Frog(0, 0, this.mFrogTextureRegion, this.getVertexBufferObjectManager());
		this.mFrog.animate(1000);
		this.setFrogToRandomCell();
		this.mScene.getChildByIndex(LAYER_FOOD).attachChild(this.mFrog);

		/* The On-Screen Controls to control the direction of the snake. */
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				if(pValueX == 1) {
					SnakeGameActivity.this.mSnake.setDirection(Direction.RIGHT);
				} else if(pValueX == -1) {
					SnakeGameActivity.this.mSnake.setDirection(Direction.LEFT);
				} else if(pValueY == 1) {
					SnakeGameActivity.this.mSnake.setDirection(Direction.DOWN);
				} else if(pValueY == -1) {
					SnakeGameActivity.this.mSnake.setDirection(Direction.UP);
				}
			}
		});
		/* Make the controls semi-transparent. */
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);

		this.mScene.setChildScene(this.mDigitalOnScreenControl);

		/* Make the Snake move every 0.5 seconds. */
		this.mScene.registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if(SnakeGameActivity.this.mGameRunning) {
					try {
						SnakeGameActivity.this.mSnake.move();
					} catch (final SnakeSuicideException e) {
						SnakeGameActivity.this.onGameOver();
					}

					SnakeGameActivity.this.handleNewSnakePosition();
				}
			}
		}));

		/* The title-text. */
		final Text titleText = new Text(0, 0, this.mFont, "Snake\non a Phone!", new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
		titleText.setPosition((CAMERA_WIDTH - titleText.getWidth()) * 0.5f, (CAMERA_HEIGHT - titleText.getHeight()) * 0.5f);
		titleText.setScale(0.0f);
		titleText.registerEntityModifier(new ScaleModifier(2, 0.0f, 1.0f));
		this.mScene.getChildByIndex(LAYER_SCORE).attachChild(titleText);

		/* The handler that removes the title-text and starts the game. */
		this.mScene.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				SnakeGameActivity.this.mScene.unregisterUpdateHandler(pTimerHandler);
				SnakeGameActivity.this.mScene.getChildByIndex(LAYER_SCORE).detachChild(titleText);
				SnakeGameActivity.this.mGameRunning = true;
			}
		}));

		/* The game-over text. */
		this.mGameOverText = new Text(0, 0, this.mFont, "Game\nOver", new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
		this.mGameOverText.setPosition((CAMERA_WIDTH - this.mGameOverText.getWidth()) * 0.5f, (CAMERA_HEIGHT - this.mGameOverText.getHeight()) * 0.5f);
		this.mGameOverText.registerEntityModifier(new ScaleModifier(3, 0.1f, 2.0f));
		this.mGameOverText.registerEntityModifier(new RotationModifier(3, 0, 720));

		return this.mScene;
	}

	@Override
	public void onGameCreated() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void setFrogToRandomCell() {
		this.mFrog.setCell(MathUtils.random(1, CELLS_HORIZONTAL - 2), MathUtils.random(1, CELLS_VERTICAL - 2));
	}

	private void handleNewSnakePosition() {
		final SnakeHead snakeHead = this.mSnake.getHead();

		if(snakeHead.getCellX() < 0 || snakeHead.getCellX() >= CELLS_HORIZONTAL || snakeHead.getCellY() < 0 || snakeHead.getCellY() >= CELLS_VERTICAL) {
			this.onGameOver();
		} else if(snakeHead.isInSameCell(this.mFrog)) {
			this.mScore += 50;
			this.mScoreText.setText("Score: " + this.mScore);
			this.mSnake.grow();
			this.mMunchSound.play();
			this.setFrogToRandomCell();
		}
	}

	private void onGameOver() {
		this.mGameOverSound.play();
		this.mScene.getChildByIndex(LAYER_SCORE).attachChild(this.mGameOverText);
		this.mGameRunning = false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
