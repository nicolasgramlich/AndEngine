package org.anddev.andengine.ui.activity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.SplashScene;
import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureFactory;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.app.Activity;
import android.content.Intent;

/**
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
	private ITextureSource mSplashTextureSource;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract ITextureSource onGetSplashTextureSource();

	protected abstract float getSplashDuration();

	protected abstract Class<? extends Activity> getFollowUpActivity();

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

		this.mCamera = new Camera(0, 0, width, height);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(width, height), this.mCamera));
	}

	@Override
	public void onLoadResources() {
	}

	@Override
	public Scene onLoadScene() {
		final Texture loadingScreenTexture = TextureFactory.createForTextureSourceSize(this.mSplashTextureSource, TextureOptions.BILINEAR);
		final TextureRegion loadingScreenTextureRegion = TextureRegionFactory.createFromSource(loadingScreenTexture, this.mSplashTextureSource, 0, 0);
		
		TextureManager.loadTexture(loadingScreenTexture);
		final float splashDuration = this.getSplashDuration();
		
		final SplashScene splashScene = new SplashScene(this.mCamera, loadingScreenTextureRegion, splashDuration, this.getSplashScaleTo());
		
		splashScene.registerPreFrameHandler(new TimerHandler(splashDuration, new ITimerCallback() {
			@Override
			public void onTimePassed() {				
				BaseSplashActivity.this.startActivity(new Intent(BaseSplashActivity.this, BaseSplashActivity.this.getFollowUpActivity()));
				BaseSplashActivity.this.finish();
			}
		}));
		return splashScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
