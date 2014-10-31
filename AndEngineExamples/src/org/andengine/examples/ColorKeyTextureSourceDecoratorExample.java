package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.ColorKeyBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.RectangleBitmapTextureAtlasSourceDecoratorShape;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ColorKeyTextureSourceDecoratorExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private ITextureRegion mChromaticCircleTextureRegion;
	private ITextureRegion mChromaticCircleColorKeyedTextureRegion;

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
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);

		/* The actual AssetTextureSource. */
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		final AssetBitmapTextureAtlasSource baseTextureSource = AssetBitmapTextureAtlasSource.create(this.getAssets(), "gfx/chromatic_circle.png");

		this.mChromaticCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, baseTextureSource, 0, 0);

		/* We will remove both the red and the green segment of the chromatic circle,
		 * by nesting two ColorKeyTextureSourceDecorators around the actual baseTextureSource. */
		final int colorKeyRed = Color.rgb(255, 0, 51); // Red segment
		final int colorKeyGreen = Color.rgb(0, 179, 0); // Green segment
		final ColorKeyBitmapTextureAtlasSourceDecorator colorKeyBitmapTextureAtlasSource = new ColorKeyBitmapTextureAtlasSourceDecorator(new ColorKeyBitmapTextureAtlasSourceDecorator(baseTextureSource, RectangleBitmapTextureAtlasSourceDecoratorShape.getDefaultInstance(), colorKeyRed), RectangleBitmapTextureAtlasSourceDecoratorShape.getDefaultInstance(), colorKeyGreen);

		this.mChromaticCircleColorKeyedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, colorKeyBitmapTextureAtlasSource, 128, 0);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		final float centerX = (CAMERA_WIDTH - this.mChromaticCircleTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mChromaticCircleTextureRegion.getHeight()) / 2;

		final Sprite chromaticCircle = new Sprite(centerX - 80, centerY, this.mChromaticCircleTextureRegion, this.getVertexBufferObjectManager());

		final Sprite chromaticCircleColorKeyed = new Sprite(centerX + 80, centerY, this.mChromaticCircleColorKeyedTextureRegion, this.getVertexBufferObjectManager());

		scene.attachChild(chromaticCircle);
		scene.attachChild(chromaticCircleColorKeyed);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
