package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.pool.RunnablePoolItem;
import org.andengine.util.adt.pool.RunnablePoolUpdateHandler;

import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 14:56:22 - 15.06.2011
 */
public class RunnablePoolUpdateHandlerExample extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int FACE_COUNT = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	private int mTargetFaceIndex = 0;
	private final Sprite[] mFaces = new Sprite[FACE_COUNT];

	private final RunnablePoolUpdateHandler<FaceRotateRunnablePoolItem> mFaceRotateRunnablePoolUpdateHandler = new RunnablePoolUpdateHandler<FaceRotateRunnablePoolItem>() {
		@Override
		protected FaceRotateRunnablePoolItem onAllocatePoolItem() {
			return new FaceRotateRunnablePoolItem();
		}
	};

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
		Toast.makeText(this, "Touch the screen to rotate the sprites using RunnablePoolItems.", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		scene.registerUpdateHandler(this.mFaceRotateRunnablePoolUpdateHandler);

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		this.mFaces[0] = new Sprite(centerX - 50, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		this.mFaces[1] = new Sprite(centerX + 50, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(this.mFaces[0]);
		scene.attachChild(this.mFaces[1]);

		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			this.mTargetFaceIndex = (this.mTargetFaceIndex + 1) % FACE_COUNT;

			final FaceRotateRunnablePoolItem faceRotateRunnablePoolItem = this.mFaceRotateRunnablePoolUpdateHandler.obtainPoolItem();
			faceRotateRunnablePoolItem.setTargetFace(this.mFaces[this.mTargetFaceIndex]);
			this.mFaceRotateRunnablePoolUpdateHandler.postPoolItem(faceRotateRunnablePoolItem);
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class FaceRotateRunnablePoolItem extends RunnablePoolItem {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private Sprite mTargetFace;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setTargetFace(final Sprite pTargetFace) {
			this.mTargetFace = pTargetFace;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void run() {
			this.mTargetFace.setRotation(this.mTargetFace.getRotation() + 45);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
