package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 14:33:38 - 03.10.2010
 */
public class PhysicsCollisionFilteringExample extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	/* The categories. */
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_BOX = 2;
	public static final short CATEGORYBIT_CIRCLE = 4;

	/* And what should collide with what. */
	public static final short MASKBITS_WALL = CATEGORYBIT_WALL + CATEGORYBIT_BOX + CATEGORYBIT_CIRCLE;
	public static final short MASKBITS_BOX = CATEGORYBIT_WALL + CATEGORYBIT_BOX; // Missing: CATEGORYBIT_CIRCLE
	public static final short MASKBITS_CIRCLE = CATEGORYBIT_WALL + CATEGORYBIT_CIRCLE; // Missing: CATEGORYBIT_BOX

	public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
	public static final FixtureDef BOX_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_BOX, MASKBITS_BOX, (short)0);
	public static final FixtureDef CIRCLE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_CIRCLE, MASKBITS_CIRCLE, (short)0);

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
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();
		Toast.makeText(this, "Boxes will only collide with boxes.\nCircles will only collide with circles.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0, 0, 0));
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, this.getVertexBufferObjectManager());
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, this.getVertexBufferObjectManager());

		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, WALL_FIXTURE_DEF);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, WALL_FIXTURE_DEF);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, WALL_FIXTURE_DEF);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, WALL_FIXTURE_DEF);

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);

		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(this.mPhysicsWorld != null) {
			if(pSceneTouchEvent.isActionDown()) {
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
		this.mFaceCount++;
		Debug.d("Faces: " + this.mFaceCount);

		final AnimatedSprite face;
		final Body body;

		if(this.mFaceCount % 2 == 0) {
			face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, BOX_FIXTURE_DEF);
		} else {
			face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, face, BodyType.DynamicBody, CIRCLE_FIXTURE_DEF);
		}

		face.animate(200);

		this.mScene.attachChild(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
