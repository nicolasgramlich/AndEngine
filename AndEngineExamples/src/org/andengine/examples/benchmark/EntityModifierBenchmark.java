package org.andengine.examples.benchmark;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemorySpriteVertexBufferObject;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 20:24:17 - 27.06.2010
 */
public class EntityModifierBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 2000;
	
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
	protected int getBenchmarkID() {
		return ENTITYMODIFIERBENCHMARK_ID;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	protected float getBenchmarkDuration() {
		return 10;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

//		this.drawUsingSprites(scene);
//		this.drawUsingSpritesWithSharedVertexBuffer(scene);
		this.drawUsingSpriteBatch(scene);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void drawUsingSprites(final Scene pScene) {
		final IEntityModifier faceEntityModifier = new SequenceEntityModifier(
				new RotationByModifier(2, 90),
				new AlphaModifier(1.5f, 1, 0),
				new AlphaModifier(1.5f, 0, 1),
				new ScaleModifier(2.5f, 1, 0.5f),
				new DelayModifier(0.5f),
				new ParallelEntityModifier(
						new ScaleModifier(2f, 0.5f, 5),
						new RotationByModifier(2, 90)
				),
				new ParallelEntityModifier(
						new ScaleModifier(2f, 5, 1),
						new RotationModifier(2f, 180, 0)
				)
		);

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Sprite face = new Sprite((CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, vertexBufferObjectManager);
			face.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

			face.registerEntityModifier(faceEntityModifier.deepCopy());

			pScene.attachChild(face);
		}
	}

	private void drawUsingSpritesWithSharedVertexBuffer(final Scene pScene) {
		final IEntityModifier faceEntityModifier = new SequenceEntityModifier(
				new RotationByModifier(2, 90),
				new AlphaModifier(1.5f, 1, 0),
				new AlphaModifier(1.5f, 0, 1),
				new ScaleModifier(2.5f, 1, 0.5f),
				new DelayModifier(0.5f),
				new ParallelEntityModifier(
						new ScaleModifier(2f, 0.5f, 5),
						new RotationByModifier(2, 90)
				),
				new ParallelEntityModifier(
						new ScaleModifier(2f, 5, 1),
						new RotationModifier(2f, 180, 0)
				)
		);

		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
		final ISpriteVertexBufferObject sharedVertexBuffer = new LowMemorySpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Sprite face = new Sprite((CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, sharedVertexBuffer);
			face.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			face.registerEntityModifier(faceEntityModifier.deepCopy());

			pScene.attachChild(face);
		}
	}

	private void drawUsingSpriteBatch(final Scene pScene) {
		final IEntityModifier faceEntityModifier = new SequenceEntityModifier(
				new RotationByModifier(2, 90),
				new AlphaModifier(1.5f, 1, 0),
				new AlphaModifier(1.5f, 0, 1),
				new ScaleModifier(2.5f, 1, 0.5f),
				new DelayModifier(0.5f),
				new ParallelEntityModifier(
						new ScaleModifier(2f, 0.5f, 5),
						new RotationByModifier(2, 90)
				),
				new ParallelEntityModifier(
						new ScaleModifier(2f, 5, 1),
						new RotationModifier(2f, 180, 0)
				)
		);

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final SpriteGroup spriteGroup = new SpriteGroup(this.mBitmapTextureAtlas, EntityModifierBenchmark.SPRITE_COUNT, vertexBufferObjectManager);
		spriteGroup.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		for(int i = 0; i < EntityModifierBenchmark.SPRITE_COUNT; i++) {
			final Sprite face = new Sprite((EntityModifierBenchmark.CAMERA_WIDTH - 32) * this.mRandom.nextFloat(), (EntityModifierBenchmark.CAMERA_HEIGHT - 32) * this.mRandom.nextFloat(), this.mFaceTextureRegion, vertexBufferObjectManager);
			face.registerEntityModifier(faceEntityModifier.deepCopy());

			spriteGroup.attachChild(face);
		}

		pScene.attachChild(spriteGroup);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
