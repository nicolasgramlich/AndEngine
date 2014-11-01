package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.algorithm.collision.ShapeCollisionChecker;
import org.andengine.util.algorithm.hull.JarvisMarch;
import org.andengine.util.color.Color;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class HullAlgorithmExample extends SimpleBaseGameActivity implements OnClickListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private float[] mMeshVertices = new float[] { 0, 100, Color.WHITE_ABGR_PACKED_FLOAT, -100, -100, Color.WHITE_ABGR_PACKED_FLOAT, 0, 0, Color.WHITE_ABGR_PACKED_FLOAT, 100, -100,
			Color.WHITE_ABGR_PACKED_FLOAT };
	private int mMeshVertexCount = 4;
	private Mesh mMesh;
	private Mesh mHull;
	private float[] mHullVertices;
	private int mHullVertexCount;

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
		Toast.makeText(this, "Touch the screen to add points to the screen.", Toast.LENGTH_LONG).show();
		Toast.makeText(this, "Move your finger on the screen to perform point-in-polygon tests.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, HullAlgorithmExample.CAMERA_WIDTH, HullAlgorithmExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(HullAlgorithmExample.CAMERA_WIDTH, HullAlgorithmExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();

		this.buildMeshAndHull();
	}

	@Override
	public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(HullAlgorithmExample.this, "Clicked", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionUp()) {
			final float[] coords = this.mMesh.convertSceneToLocalCoordinates(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			this.disposeMeshAndHull();
			this.addMeshVertex(coords[Constants.VERTEX_INDEX_X], coords[Constants.VERTEX_INDEX_Y]);
			this.buildMeshAndHull();
		} else if (pSceneTouchEvent.isActionMove()) {
			{ /* Point-in-polygon test for the mesh. */
				final float[] coords = this.mMesh.convertSceneToLocalCoordinates(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				if (ShapeCollisionChecker.checkContains(this.mMeshVertices, this.mMeshVertexCount, 0, 1, 3, coords[Constants.VERTEX_INDEX_X], coords[Constants.VERTEX_INDEX_Y])) {
					this.mMesh.setColor(Color.GREEN);
				} else {
					this.mMesh.setColor(Color.RED);
				}
			}
			{ /* Point-in-polygon test for the hull. */
				final float[] coords = this.mHull.convertSceneToLocalCoordinates(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				if (ShapeCollisionChecker.checkContains(this.mHullVertices, this.mHullVertexCount, 0, 1, 3, coords[Constants.VERTEX_INDEX_X], coords[Constants.VERTEX_INDEX_Y])) {
					this.mHull.setColor(Color.GREEN);
				} else {
					this.mHull.setColor(Color.RED);
				}
			}
		}

		return true;
	}

	private void disposeMeshAndHull() {
		this.mMesh.getVertexBufferObject().dispose();
		this.mMesh.detachSelf();

		this.mHull.getVertexBufferObject().dispose();
		this.mHull.detachSelf();
	}

	private void addMeshVertex(final float pX, final float pY) {
		final float[] newMeshVertices = new float[this.mMeshVertices.length + 3];

		System.arraycopy(this.mMeshVertices, 0, newMeshVertices, 0, this.mMeshVertices.length);
		newMeshVertices[newMeshVertices.length - 3] = pX;
		newMeshVertices[newMeshVertices.length - 2] = pY;
		newMeshVertices[newMeshVertices.length - 1] = Color.WHITE_ABGR_PACKED_FLOAT;

		this.mMeshVertices = newMeshVertices;
		this.mMeshVertexCount++;
	}

	private void buildMeshAndHull() {
		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
		final float centerX = HullAlgorithmExample.CAMERA_WIDTH * 0.5f;
		final float centerY = HullAlgorithmExample.CAMERA_HEIGHT * 0.5f;

		this.mMesh = new Mesh(centerX, centerY, this.mMeshVertices, this.mMeshVertexCount, DrawMode.LINE_LOOP, this.getVertexBufferObjectManager(), DrawType.STATIC);
		this.getEngine().getScene().attachChild(this.mMesh);

		this.mHullVertices = new float[this.mMeshVertices.length];
		System.arraycopy(this.mMeshVertices, 0, this.mHullVertices, 0, this.mMeshVertices.length);
		this.mHullVertexCount = new JarvisMarch().computeHull(this.mHullVertices, this.mMeshVertexCount, 0, 1, 3);

		this.mHull = new Mesh(centerX, centerY, this.mHullVertices, this.mHullVertexCount, DrawMode.LINE_LOOP, this.getVertexBufferObjectManager(), DrawType.STATIC);
		this.mHull.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(1, 0.95f, 1.05f), new ScaleModifier(1, 1.05f, 0.95f))));
		this.mHull.setColor(Color.RED);
		this.getEngine().getScene().attachChild(this.mHull);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
