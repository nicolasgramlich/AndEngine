package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleAsyncGameActivity;
import org.andengine.util.progress.IProgressListener;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:52:38 - 05.01.2012
 */
public class AsyncGameActivityExample extends SimpleAsyncGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

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
		final Camera camera = new Camera(0, 0, AsyncGameActivityExample.CAMERA_WIDTH, AsyncGameActivityExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(AsyncGameActivityExample.CAMERA_WIDTH, AsyncGameActivityExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResourcesAsync(final IProgressListener pProgressListener) throws Exception {
		/* Comfortably load the resources asynchronously, adding artificial pauses between each step. */
		pProgressListener.onProgressChanged(0);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(20);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		Thread.sleep(1000);
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		pProgressListener.onProgressChanged(40);
		Thread.sleep(1000);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AsyncGameActivityExample.this.mBitmapTextureAtlas, AsyncGameActivityExample.this, "face_box.png", 0, 0);
		pProgressListener.onProgressChanged(60);
		Thread.sleep(1000);
		this.mBitmapTextureAtlas.load();
		pProgressListener.onProgressChanged(80);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(100);
	}

	@Override
	public Scene onCreateSceneAsync(final IProgressListener pProgressListener) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		return scene;
	}

	@Override
	public void onPopulateSceneAsync(final Scene pScene, final IProgressListener pProgressListener) throws Exception {
		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (AsyncGameActivityExample.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (AsyncGameActivityExample.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		pScene.attachChild(face);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
