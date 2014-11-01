package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier.ILoopEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
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
import org.andengine.util.modifier.LoopModifier;

import android.opengl.GLES20;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class EntityModifierExample extends SimpleBaseGameActivity {
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

		final Rectangle rect = new Rectangle(centerX + 100, centerY, 32, 32, this.getVertexBufferObjectManager());
		rect.setColor(1, 0, 0);

		final AnimatedSprite face = new AnimatedSprite(centerX - 100, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face.animate(100);
		face.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		final LoopEntityModifier entityModifier =
			new LoopEntityModifier(
					new IEntityModifierListener() {
						@Override
						public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
							EntityModifierExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(EntityModifierExample.this, "Sequence started.", Toast.LENGTH_SHORT).show();
								}
							});
						}

						@Override
						public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
							EntityModifierExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(EntityModifierExample.this, "Sequence finished.", Toast.LENGTH_SHORT).show();
								}
							});
						}
					},
					2,
					new ILoopEntityModifierListener() {
						@Override
						public void onLoopStarted(final LoopModifier<IEntity> pLoopModifier, final int pLoop, final int pLoopCount) {
							EntityModifierExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(EntityModifierExample.this, "Loop: '" + (pLoop + 1) + "' of '" + pLoopCount + "' started.", Toast.LENGTH_SHORT).show();
								}
							});
						}

						@Override
						public void onLoopFinished(final LoopModifier<IEntity> pLoopModifier, final int pLoop, final int pLoopCount) {
							EntityModifierExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(EntityModifierExample.this, "Loop: '" + (pLoop + 1) + "' of '" + pLoopCount + "' finished.", Toast.LENGTH_SHORT).show();
								}
							});
						}
					},
					new SequenceEntityModifier(
//							new RotationModifier(1, 0, 90),
							new AlphaModifier(2, 1, 0),
							new AlphaModifier(1, 0, 1),
							new ScaleModifier(2, 1, 0.5f),
							new DelayModifier(0.5f),
							new ParallelEntityModifier(
									new ScaleModifier(3, 0.5f, 5),
									new RotationByModifier(3, 90)
							),
							new ParallelEntityModifier(
									new ScaleModifier(3, 5, 1),
									new RotationModifier(3, 180, 0)
							)
					)
			);

		face.registerEntityModifier(entityModifier);
		rect.registerEntityModifier(entityModifier.deepCopy());

		scene.attachChild(face);
		scene.attachChild(rect);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
