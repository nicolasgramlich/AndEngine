package org.andengine.examples;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.ZoomState;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.compressed.etc1.ETC1Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ETC1TextureExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private SmoothCamera mSmoothCamera;

	private ITexture mTexture;
	private ITextureRegion mHouseTextureRegion;

	private ZoomState mZoomState = ZoomState.NONE;

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
		Toast.makeText(this, "Click the top half of the screen to zoom in or the bottom half to zoom out!", Toast.LENGTH_LONG);

		this.mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 0, 0, 0.1f) {
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				switch (ETC1TextureExample.this.mZoomState) {
					case IN:
						this.setZoomFactor(this.getZoomFactor() + 0.1f * pSecondsElapsed);
						break;
					case OUT:
						this.setZoomFactor(this.getZoomFactor() - 0.1f * pSecondsElapsed);
						break;
				}
				super.onUpdate(pSecondsElapsed);
			}
		};

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mSmoothCamera);
	}

	@Override
	public void onCreateResources() {
		try {
			this.mTexture = new ETC1Texture(this.getTextureManager(), TextureOptions.BILINEAR) {
				@Override
				protected InputStream getInputStream() throws IOException {
					return ETC1TextureExample.this.getResources().openRawResource(R.raw.house_etc1);
				}
			};
			this.mTexture.load();

			this.mHouseTextureRegion = TextureRegionFactory.extractFromTexture(this.mTexture, 0, 0, 512, 512);

		} catch (final Throwable e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final float centerX = (CAMERA_WIDTH - this.mHouseTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mHouseTextureRegion.getHeight()) / 2;

		scene.attachChild(new Sprite(centerX, centerY, this.mHouseTextureRegion, this.getVertexBufferObjectManager()));

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
					if(pSceneTouchEvent.getY() < CAMERA_HEIGHT / 2) {
						ETC1TextureExample.this.mZoomState = ZoomState.IN;
					} else {
						ETC1TextureExample.this.mZoomState = ZoomState.OUT;
					}
				} else {
					ETC1TextureExample.this.mZoomState = ZoomState.NONE;
				}
				return true;
			}
		});

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
