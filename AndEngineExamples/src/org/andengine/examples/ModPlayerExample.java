package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.FileUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import org.helllabs.android.xmp.ModPlayer;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 15:51:47 - 13.06.2010
 */
public class ModPlayerExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final String SAMPLE_MOD_DIRECTORY = "mfx/";
	private static final String SAMPLE_MOD_FILENAME = "lepeltheme.mod";

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mILove8BitTextureRegion;

	private final ModPlayer mModPlayer = ModPlayer.getInstance();

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
		Toast.makeText(this, "Touch the image to toggle the playback of this awesome 8-bit style .MOD music.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128);
		this.mILove8BitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "i_love_8_bit.png", 0, 0);
		this.mBitmapTextureAtlas.load();

		if(FileUtils.isFileExistingOnExternalStorage(this, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME)) {
			this.startPlayingMod();
		} else {
			ModPlayerExample.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ModPlayerExample.this.doAsync(R.string.dialog_modplayerexample_loading_to_external_title, R.string.dialog_modplayerexample_loading_to_external_message, new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							FileUtils.ensureDirectoriesExistOnExternalStorage(ModPlayerExample.this, SAMPLE_MOD_DIRECTORY);
							FileUtils.copyToExternalStorage(ModPlayerExample.this, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME);
							return null;
						}
					}, new Callback<Void>() {
						@Override
						public void onCallback(final Void pCallbackValue) {
							ModPlayerExample.this.startPlayingMod();
						}
					});
				}
			});
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final float centerX = (CAMERA_WIDTH - this.mILove8BitTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mILove8BitTextureRegion.getHeight()) / 2;

		final Sprite iLove8Bit = new Sprite(centerX, centerY, this.mILove8BitTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(iLove8Bit);

		scene.registerTouchArea(iLove8Bit);
		scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					ModPlayerExample.this.mModPlayer.pause();
				}

				return true;
			}
		});

		return scene;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ModPlayerExample.this.mModPlayer.stop();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void startPlayingMod() {
		this.mModPlayer.play(FileUtils.getAbsolutePathOnExternalStorage(this, SAMPLE_MOD_DIRECTORY + SAMPLE_MOD_FILENAME));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
