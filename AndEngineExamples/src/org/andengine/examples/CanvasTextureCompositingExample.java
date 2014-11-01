package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class CanvasTextureCompositingExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mDecoratedBalloonTextureRegion;

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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 190, 190, TextureOptions.BILINEAR);
		
		final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource(190, 190);
		final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {
			@Override
			protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
				this.mPaint.setColorFilter(new LightingColorFilter(Color.argb(128, 128, 128, 255), Color.TRANSPARENT));
				final Bitmap balloon = BitmapFactory.decodeStream(CanvasTextureCompositingExample.this.getAssets().open("gfx/texturecompositing/balloon.png"));
				pCanvas.drawBitmap(balloon, 0, 0, this.mPaint);
				this.mPaint.setColorFilter(null);

				this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				final Bitmap alphamask = BitmapFactory.decodeStream(CanvasTextureCompositingExample.this.getAssets().open("gfx/texturecompositing/alphamask.png"));
				pCanvas.drawBitmap(alphamask, 0, 0, this.mPaint);
				this.mPaint.setXfermode(null);

				final Bitmap zynga = BitmapFactory.decodeStream(CanvasTextureCompositingExample.this.getAssets().open("gfx/texturecompositing/zynga.png"));
				this.mPaint.setColorFilter(new LightingColorFilter(Color.RED, Color.TRANSPARENT));
				pCanvas.drawBitmap(zynga, 0, 0, this.mPaint);
			}
			
			@Override
			public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
				throw new DeepCopyNotSupportedException();
			}
		};

		this.mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.5f, 0.5f, 0.5f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mDecoratedBalloonTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mDecoratedBalloonTextureRegion.getHeight()) / 2;

		/* Create the balloon and add it to the scene. */
		final Sprite balloon = new Sprite(centerX, centerY, this.mDecoratedBalloonTextureRegion, this.getVertexBufferObjectManager());
		balloon.registerEntityModifier(new LoopEntityModifier(new RotationModifier(60, 0, 360)));
		scene.attachChild(balloon);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
