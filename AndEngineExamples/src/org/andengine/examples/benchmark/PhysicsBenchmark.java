package org.andengine.examples.benchmark;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsBenchmark extends BaseBenchmark implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int COUNT_HORIZONTAL = 17;
	private static final int COUNT_VERTICAL = 15;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;

	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;
	private int mFaceCount = 0;

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
		return PHYSICSBENCHMARK_ID;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	protected float getBenchmarkDuration() {
		return 20;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0, 0, 0));
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 2 * SensorManager.GRAVITY_EARTH / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), false);

		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, this.getVertexBufferObjectManager());
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, this.getVertexBufferObjectManager());

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);

		for(int x = 1; x < COUNT_HORIZONTAL; x++) {
			for(int y = 1; y < COUNT_VERTICAL; y++) {
				final float pX = (((float)CAMERA_WIDTH) / COUNT_HORIZONTAL) * x + y;
				final float pY = (((float)CAMERA_HEIGHT) / COUNT_VERTICAL) * y;
				this.addFace(this.mScene, pX - 16, pY - 16);
			}
		}

		this.mScene.registerUpdateHandler(new TimerHandler(2, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				PhysicsBenchmark.this.mScene.unregisterUpdateHandler(pTimerHandler);
				PhysicsBenchmark.this.mScene.registerUpdateHandler(PhysicsBenchmark.this.mPhysicsWorld);
				PhysicsBenchmark.this.mScene.registerUpdateHandler(new TimerHandler(10, new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						final Vector2 gravity = Vector2Pool.obtain(0, -SensorManager.GRAVITY_EARTH / 32);
						PhysicsBenchmark.this.mPhysicsWorld.setGravity(gravity);
						Vector2Pool.recycle(gravity);
					}
				}));
			}
		}));

		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(this.mPhysicsWorld != null) {
			if(pSceneTouchEvent.isActionDown()) {
				this.addFace(pScene, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				return true;
			}
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void addFace(final Scene pScene, final float pX, final float pY) {
		this.mFaceCount++;

		final AnimatedSprite face;
		final Body body;


		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

		if(this.mFaceCount % 2 == 0) {
			face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, objectFixtureDef);
		} else {
			face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, face, BodyType.DynamicBody, objectFixtureDef);
		}

		pScene.attachChild(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
