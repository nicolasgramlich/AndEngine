package org.andengine.ui.activity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
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
import android.util.DisplayMetrics;

public abstract class BaseSplashActivity extends SimpleBaseGameActivity {
	private TextureRegion mLoadingScreenTextureRegion;
	private int mWidth;
	private int mHeight;
	protected abstract SplashActivityOptions onCreateSplashOptions();

	public EngineOptions onCreateEngineOptions() {
		// Get screen width and height
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWidth = metrics.heightPixels;
		mHeight = metrics.widthPixels;
		return new EngineOptions(true,this.onCreateSplashOptions().getOrientation(),
				new RatioResolutionPolicy(this.mWidth,this.mHeight),
				new Camera(0,0,this.mWidth,this.mHeight));
	}
	@Override
	protected void onCreateResources() {
		// Create splash texture
		final BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(),2046,2046, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mLoadingScreenTextureRegion = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, this.onCreateSplashOptions().getTexturePath(), 0, 0);
		this.mEngine.getTextureManager().loadTexture(atlas);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Scene onCreateScene() {
		final Scene scene = new Scene();
		Sprite backgroundSprite = new Sprite(0,0,this.mWidth, this.mHeight, this.mLoadingScreenTextureRegion, this.getVertexBufferObjectManager());
		scene.setBackground(new Background(0,0,0));
		scene.attachChild(backgroundSprite);
		backgroundSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		backgroundSprite.setAlpha(255);
		float duration = this.onCreateSplashOptions().getDuration();
		if(this.onCreateSplashOptions().getFade()){
			SequenceEntityModifier SplashFadeModifier = new SequenceEntityModifier(new FadeInModifier(duration / 4), new DelayModifier(duration / 2), new FadeOutModifier(duration / 4));
			SplashFadeModifier.addModifierListener(new IModifierListener(){
				@Override
				public void onModifierStarted(IModifier pModifier, Object pItem) {
				}
		
				@Override
				public void onModifierFinished(IModifier pModifier, Object pItem) {
					Intent intent = new Intent(BaseSplashActivity.this, BaseSplashActivity.this.onCreateSplashOptions().getFollowUpActivity());
					BaseSplashActivity.this.finish();
					BaseSplashActivity.this.startActivity(intent);
					}
				});
			backgroundSprite.registerEntityModifier(SplashFadeModifier);
		}else{
			DelayModifier delay = new DelayModifier(duration);
			delay.addModifierListener(new IModifierListener(){
				@Override
				public void onModifierStarted(IModifier pModifier, Object pItem) {
				}
		
				@Override
				public void onModifierFinished(IModifier pModifier, Object pItem) {
					Intent intent = new Intent(BaseSplashActivity.this, BaseSplashActivity.this.onCreateSplashOptions().getFollowUpActivity());
					BaseSplashActivity.this.finish();
					BaseSplashActivity.this.startActivity(intent);
					}
				});
			backgroundSprite.registerEntityModifier(delay);
		}
		return scene;
	}
	// Splash options class
	public class SplashActivityOptions{
		ScreenOrientation orientation;
		String texturePath;
		float splashDuration;
		boolean fade;
		Class<? extends Activity> followUpActivity;
		public SplashActivityOptions(ScreenOrientation orientation, String splashTexturePath, float duration,boolean fade, Class<? extends Activity> followUpActivity){
			this.orientation = orientation;
			this.texturePath = splashTexturePath;
			this.splashDuration = duration;
			this.followUpActivity = followUpActivity;
			this.fade = fade;
		}
		ScreenOrientation getOrientation(){
			return this.orientation;
		}
		String getTexturePath(){
			return this.texturePath;
		}
		float getDuration(){
			return this.splashDuration;
		}
		boolean getFade(){
			return this.fade;
		}
		Class<? extends Activity> getFollowUpActivity(){
			return this.followUpActivity;
		}
	}
}