package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 14:27:22 - 14.06.2011
 */
public class SpriteBatchExample extends SimpleBaseGameActivity {
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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		
		final Sprite faceSprite1 = new Sprite(-50, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager()); 
		final Sprite faceSprite2 = new Sprite(50, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		
		faceSprite1.setScale(2);
		faceSprite2.setRotation(45);

		/* Create the face and add it to the scene. */
		final SpriteBatch dynamicSpriteBatch = new DynamicSpriteBatch(this.mBitmapTextureAtlas, 2, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onUpdateSpriteBatch() {
				this.draw(faceSprite1);
				this.draw(faceSprite2);

				return true;
			}
		};
		
		final SpriteBatch staticSpriteBatch = new SpriteBatch(this.mBitmapTextureAtlas, 2, this.getVertexBufferObjectManager());
		staticSpriteBatch.draw(this.mFaceTextureRegion, -50, 0, this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight(), 2, 2, 1, 1, 1, 1);
		staticSpriteBatch.draw(this.mFaceTextureRegion, 50, 0, this.mFaceTextureRegion.getWidth(), this.mFaceTextureRegion.getHeight(), 45, 1, 1, 1, 1);
		staticSpriteBatch.submit();

		dynamicSpriteBatch.setPosition(centerX, centerY - 50);
		staticSpriteBatch.setPosition(centerX, centerY + 50);
		
		scene.attachChild(dynamicSpriteBatch);
		scene.attachChild(staticSpriteBatch);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
