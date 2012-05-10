package org.andengine.ui.activity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLES20;
import android.view.Display;

public abstract class BaseSplashActivity extends SimpleBaseGameActivity {
	private TextureRegion mLoadingScreenTextureRegion;
	Display d;
	private int mWidth = 720;
	private int mHeight = 480;
	protected abstract ScreenOrientation getScreenOrientation();
	protected abstract String onGetSplashTexturePath();		
	protected abstract float onGetSplashDuration();
	protected abstract Class<? extends Activity> getFollowUpActivity();
	
	@SuppressWarnings("deprecation")
	public EngineOptions onCreateEngineOptions() {
		Display display = getWindowManager().getDefaultDisplay();
		mWidth = display.getWidth();
		mHeight = display.getHeight();
		return new EngineOptions(true,this.getScreenOrientation(),
		new RatioResolutionPolicy(this.mWidth,this.mHeight),
		new Camera(0,0,this.mWidth,this.mHeight));
	
	}
	@Override
	protected void onCreateResources() {
		final BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(),2046,2046, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mLoadingScreenTextureRegion = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, onGetSplashTexturePath(), 0, 0);
		this.mEngine.getTextureManager().loadTexture(atlas);
	
	}
	@Override
	protected Scene onCreateScene() {
		final Scene scene = new Scene();
		Sprite backgroundSprite = new Sprite(0,0,this.mWidth, this.mHeight, this.mLoadingScreenTextureRegion, this.getVertexBufferObjectManager());
		scene.setBackground(new Background(0,0,0));
		scene.attachChild(backgroundSprite);
		backgroundSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		backgroundSprite.setAlpha(255);
		SequenceEntityModifier SplashFadeModifier = new SequenceEntityModifier(new FadeInModifier(this.onGetSplashDuration() / 4), new DelayModifier(this.onGetSplashDuration() / 2), new FadeOutModifier(this.onGetSplashDuration() / 4));
		SplashFadeModifier.addModifierListener(new IModifierListener(){
			@Override
			public void onModifierStarted(IModifier pModifier, Object pItem) {
			}
	
			@Override
			public void onModifierFinished(IModifier pModifier, Object pItem) {
				Intent intent = new Intent(BaseSplashActivity.this, BaseSplashActivity.this.getFollowUpActivity());
				BaseSplashActivity.this.finish();
				BaseSplashActivity.this.startActivity(intent);
				}
			});
		backgroundSprite.registerEntityModifier(SplashFadeModifier);
		return scene;
	}

}