package org.andengine.examples;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.engine.splitscreen.SingleSceneSplitScreenEngine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class SplitScreenExample extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 400;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Camera mChaseCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mBoxFaceTextureRegion;

	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;

	private int mFaceCount;

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
		Toast.makeText(this, "Touch the screen to add boxes.", Toast.LENGTH_LONG).show();

		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, 0, CAMERA_WIDTH, 0, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH * 2, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new SingleSceneSplitScreenEngine(pEngineOptions, this.mChaseCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 32, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);

		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if (this.mPhysicsWorld != null) {
			if (pSceneTouchEvent.isActionDown()) {
				this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				return true;
			}
		}
		return false;
	}

	@Override
	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void addFace(final float pX, final float pY) {
		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

		final AnimatedSprite face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
		face.animate(100);
		final Body body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, objectFixtureDef);

		this.mScene.attachChild(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));

		if (this.mFaceCount == 0) {
			this.mChaseCamera.setChaseEntity(face);
		}

		this.mFaceCount++;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
