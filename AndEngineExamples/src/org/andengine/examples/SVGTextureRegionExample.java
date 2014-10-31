package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.svg.adt.ISVGColorMapper;
import org.andengine.extension.svg.adt.SVGDirectColorMapper;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 13:58:12 - 21.05.2011
 */
public class SVGTextureRegionExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SIZE = 128;

	private static final int COUNT = 12;
	private static final int COLUMNS = 4;
	private static final int ROWS = (int)Math.ceil((float)SVGTextureRegionExample.COUNT / SVGTextureRegionExample.COLUMNS);

	// ===========================================================
	// Fields
	// ===========================================================

	private BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas;
	private ITextureRegion[] mSVGTestTextureRegions;

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
		final Camera camera = new Camera(0, 0, SVGTextureRegionExample.CAMERA_WIDTH, SVGTextureRegionExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(SVGTextureRegionExample.CAMERA_WIDTH, SVGTextureRegionExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mSVGTestTextureRegions = new BaseTextureRegion[SVGTextureRegionExample.COUNT];
		int i = 0;
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 16, 16);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 32, 32);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 64, 64);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "chick.svg", 128, 128);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 16, 16);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 64, 64);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 128, 128, new ISVGColorMapper() {
			@Override
			public Integer mapColor(final Integer pColor) {
				if(pColor == null) {
					return null;
				} else {
					/* Swap blue and green channel. */
					return Color.argb(0, Color.red(pColor), Color.blue(pColor), Color.green(pColor));
				}
			}
		});
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "badge.svg", 256, 256, new ISVGColorMapper() {
			@Override
			public Integer mapColor(final Integer pColor) {
				if(pColor == null) {
					return null;
				} else {
					/* Swap red and green channel. */
					return Color.argb(0, Color.green(pColor), Color.red(pColor), Color.blue(pColor));
				}
			}
		});
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid.svg", 64, 64, 2, 2);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid.svg", 256, 256, 2, 2);
		final SVGDirectColorMapper angryPacDroidSVGColorMapper = new SVGDirectColorMapper();
		angryPacDroidSVGColorMapper.addColorMapping(0xA7CA4A, 0xEA872A);
		angryPacDroidSVGColorMapper.addColorMapping(0xC1DA7F, 0xFAA15F);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid.svg", 256, 256, angryPacDroidSVGColorMapper, 2, 2);
		this.mSVGTestTextureRegions[i++] = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBuildableBitmapTextureAtlas, this, "pacdroid_apples.svg", 256, 256, 2, 2);

		try {
			this.mBuildableBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.mBuildableBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.5f, 0.5f, 0.5f));

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		for(int i = 0; i < SVGTextureRegionExample.COUNT; i++) {
			final int row = i / SVGTextureRegionExample.COLUMNS;
			final int column = i % SVGTextureRegionExample.COLUMNS;

			final float centerX = CAMERA_WIDTH / (SVGTextureRegionExample.COLUMNS + 1) * (column + 1);
			final float centerY = CAMERA_HEIGHT / (SVGTextureRegionExample.ROWS + 1) * (row + 1);

			final float x = centerX - SVGTextureRegionExample.SIZE * 0.5f;
			final float y = centerY - SVGTextureRegionExample.SIZE * 0.5f;
			final ITextureRegion textureRegion = this.mSVGTestTextureRegions[i];
			if(textureRegion instanceof TiledTextureRegion) {
				final TiledTextureRegion tiledTextureRegion = (TiledTextureRegion)textureRegion;
				final AnimatedSprite animatedSprite = new AnimatedSprite(x, y, SVGTextureRegionExample.SIZE, SVGTextureRegionExample.SIZE, tiledTextureRegion, vertexBufferObjectManager);
				animatedSprite.animate(500);
				scene.attachChild(animatedSprite);
			} else { 
				scene.attachChild(new Sprite(x, y, SVGTextureRegionExample.SIZE, SVGTextureRegionExample.SIZE, textureRegion, vertexBufferObjectManager));
			}
		}

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
