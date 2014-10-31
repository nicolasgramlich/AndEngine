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
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class TextureOptionsExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mBitmapTextureAtlasBilinear;
	private BitmapTextureAtlas mBitmapTextureAtlasRepeating;

	private ITextureRegion mFaceTextureRegion;
	private ITextureRegion mFaceTextureRegionBilinear;
	private ITextureRegion mFaceTextureRegionRepeating;

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

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();

		this.mBitmapTextureAtlasBilinear = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegionBilinear = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasBilinear, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlasBilinear.load();

		this.mBitmapTextureAtlasRepeating = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.REPEATING_NEAREST);
		this.mFaceTextureRegionRepeating = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasRepeating, this, "face_box.png", 0, 0);
		/* The following statement causes the BitmapTextureAtlas to be printed horizontally 10x on any Sprite that uses it.
		 * So we will later increase the width of such a sprite by the same factor to avoid distortion. */
		this.mFaceTextureRegionRepeating.setTextureWidth(10 * this.mFaceTextureRegionRepeating.getWidth());
		this.mBitmapTextureAtlasRepeating.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		final Sprite face = new Sprite(centerX - 160, centerY - 40, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face.setScale(4);

		final Sprite faceBilinear = new Sprite(centerX + 160, centerY - 40, this.mFaceTextureRegionBilinear, this.getVertexBufferObjectManager());
		faceBilinear.setScale(4);

		/* Make sure sprite has the same size as mTextureRegionRepeating.
		 * Giving the sprite twice the height shows you'd also have to change the height of the TextureRegion! */
		final Sprite faceRepeating = new Sprite(centerX - 160, centerY + 100, this.mFaceTextureRegionRepeating.getWidth(), this.mFaceTextureRegionRepeating.getHeight() * 2, this.mFaceTextureRegionRepeating, this.getVertexBufferObjectManager());

		scene.attachChild(face);
		scene.attachChild(faceBilinear);
		scene.attachChild(faceRepeating);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
