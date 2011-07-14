package org.anddev.andengine.ui.activity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.SplashScene;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureAtlas.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureAtlasFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.app.Activity;
import android.content.Intent;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 08:25:31 - 03.05.2010
 */
public abstract class BaseSplashActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private IBitmapTextureSource mSplashTextureSource;
	private TextureRegion mLoadingScreenTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract ScreenOrientation getScreenOrientation();

	protected abstract IBitmapTextureSource onGetSplashTextureSource();

	protected abstract float getSplashDuration();

	protected abstract Class<? extends Activity> getFollowUpActivity();

	protected float getSplashScaleFrom() {
		return 1f;
	}

	protected float getSplashScaleTo() {
		return 1f;
	}

	@Override
	public void onLoadComplete() {
	}

	@Override
	public Engine onLoadEngine() {
		this.mSplashTextureSource = this.onGetSplashTextureSource();

		final int width = this.mSplashTextureSource.getWidth();
		final int height = this.mSplashTextureSource.getHeight();

		this.mCamera = this.getSplashCamera(width, height);
		return new Engine(new EngineOptions(true, this.getScreenOrientation(), this.getSplashResolutionPolicy(width, height), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		final BitmapTextureAtlas loadingScreenBitmapTextureAtlas = BitmapTextureAtlasFactory.createForTextureSourceSize(BitmapTextureFormat.RGBA_8888, this.mSplashTextureSource, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mLoadingScreenTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(loadingScreenBitmapTextureAtlas, this.mSplashTextureSource, 0, 0);

		this.getEngine().getTextureManager().loadTexture(loadingScreenBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		final float splashDuration = this.getSplashDuration();

		final SplashScene splashScene = new SplashScene(this.mCamera, this.mLoadingScreenTextureRegion, splashDuration, this.getSplashScaleFrom(), this.getSplashScaleTo());

		splashScene.registerUpdateHandler(new TimerHandler(splashDuration, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				BaseSplashActivity.this.startActivity(new Intent(BaseSplashActivity.this, BaseSplashActivity.this.getFollowUpActivity()));
				BaseSplashActivity.this.finish();
			}
		}));

		return splashScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected Camera getSplashCamera(final int pSplashwidth, final int pSplashHeight) {
		return new Camera(0, 0, pSplashwidth, pSplashHeight);
	}

	protected IResolutionPolicy getSplashResolutionPolicy(final int pSplashwidth, final int pSplashHeight) {
		return new RatioResolutionPolicy(pSplashwidth, pSplashHeight);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
