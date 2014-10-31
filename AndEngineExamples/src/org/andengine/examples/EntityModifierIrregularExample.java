package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.IModifier;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 21:42:39 - 06.07.2010
 */
public class EntityModifierIrregularExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mFaceTextureRegion;

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
		Toast.makeText(this, "Shapes can have variable rotation and scale centers.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		final AnimatedSprite face1 = new AnimatedSprite(centerX - 100, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face1.setRotationCenter(0, 0);
		face1.setScaleCenter(0, 0);
		face1.animate(100);

		final AnimatedSprite face2 = new AnimatedSprite(centerX + 100, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face2.animate(100);

		final SequenceEntityModifier entityModifier = new SequenceEntityModifier(
				new IEntityModifierListener() {
					@Override
					public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
						EntityModifierIrregularExample.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(EntityModifierIrregularExample.this, "Sequence started.", Toast.LENGTH_LONG).show();
							}
						});
					}

					@Override
					public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
						EntityModifierIrregularExample.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(EntityModifierIrregularExample.this, "Sequence finished.", Toast.LENGTH_LONG).show();
							}
						});
					}
				},
				new ScaleModifier(2, 1.0f, 0.75f, 1.0f, 2.0f),
				new ScaleModifier(2, 0.75f, 2.0f, 2.0f, 1.25f),
				new ParallelEntityModifier(
						new ScaleModifier(3, 2.0f, 5.0f, 1.25f, 5.0f),
						new RotationByModifier(3, 180)
				),
				new ParallelEntityModifier(
						new ScaleModifier(3, 5, 1),
						new RotationModifier(3, 180, 0)
				)
		);

		face1.registerEntityModifier(entityModifier);
		face2.registerEntityModifier(entityModifier.deepCopy());

		scene.attachChild(face1);
		scene.attachChild(face2);

		/* Create some not-modified sprites, that act as fixed references to the modified ones. */
		final AnimatedSprite face1Reference = new AnimatedSprite(centerX - 100, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		final AnimatedSprite face2Reference = new AnimatedSprite(centerX + 100, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());

		scene.attachChild(face1Reference);
		scene.attachChild(face2Reference);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
