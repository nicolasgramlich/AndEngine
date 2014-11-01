package org.andengine.examples;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 13:58:48 - 19.07.2010
 */
public class TMXTiledMapExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private BoundCamera mBoundChaseCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	private TMXTiledMap mTMXTiledMap;
	protected int mCactusCount;

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
		Toast.makeText(this, "The tile the player is walking on will be highlighted.", Toast.LENGTH_LONG).show();

		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "player.png", 0, 0, 3, 4);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();

		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					/* We are going to count the tiles that have the property "cactus=true" set. */
					if(pTMXTileProperties.containsTMXProperty("cactus", "true")) {
						TMXTiledMapExample.this.mCactusCount++;
					}
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/desert.tmx");

			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(TMXTiledMapExample.this, "Cactus count in this TMXTiledMap: " + TMXTiledMapExample.this.mCactusCount, Toast.LENGTH_LONG).show();
				}
			});
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		scene.attachChild(tmxLayer);

		/* Make the camera not exceed the bounds of the TMXEntity. */
		this.mBoundChaseCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
		this.mBoundChaseCamera.setBoundsEnabled(true);

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getHeight()) / 2;

		/* Create the sprite and add it to the scene. */
		final AnimatedSprite player = new AnimatedSprite(centerX, centerY, this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
		this.mBoundChaseCamera.setChaseEntity(player);

		final Path path = new Path(5).to(0, 160).to(0, 500).to(600, 500).to(600, 160).to(0, 160);

		player.registerEntityModifier(new LoopEntityModifier(new PathModifier(30, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {

			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				switch(pWaypointIndex) {
					case 0:
						player.animate(new long[]{200, 200, 200}, 6, 8, true);
						break;
					case 1:
						player.animate(new long[]{200, 200, 200}, 3, 5, true);
						break;
					case 2:
						player.animate(new long[]{200, 200, 200}, 0, 2, true);
						break;
					case 3:
						player.animate(new long[]{200, 200, 200}, 9, 11, true);
						break;
				}
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {

			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {

			}
		})));

		/* Now we are going to create a rectangle that will  always highlight the tile below the feet of the pEntity. */
		final Rectangle currentTileRectangle = new Rectangle(0, 0, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), this.getVertexBufferObjectManager());
		currentTileRectangle.setColor(1, 0, 0, 0.25f);
		scene.attachChild(currentTileRectangle);

		scene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				/* Get the scene-coordinates of the players feet. */
				final float[] playerFootCordinates = player.convertLocalToSceneCoordinates(12, 31);

				/* Get the tile the feet of the player are currently waking on. */
				final TMXTile tmxTile = tmxLayer.getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y]);
				if(tmxTile != null) {
					// tmxTile.setTextureRegion(null); <-- Rubber-style removing of tiles =D
					currentTileRectangle.setPosition(tmxTile.getTileX(), tmxTile.getTileY());
				}
			}
		});
		scene.attachChild(player);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
