package org.andengine.examples;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.ease.EaseQuadInOut;

import android.widget.Toast;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:55:18 - 06.11.2011
 */
public class MotionStreakExample extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mMotionStreaking = true;

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
		final Camera camera = new Camera(0, 0, MotionStreakExample.CAMERA_WIDTH, MotionStreakExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(MotionStreakExample.CAMERA_WIDTH, MotionStreakExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new Engine(pEngineOptions) {
			private static final int RENDERTEXTURE_COUNT = 2;

			private boolean mRenderTextureInitialized;

			private final RenderTexture[] mRenderTextures = new RenderTexture[RENDERTEXTURE_COUNT];
			private final Sprite[] mRenderTextureSprites = new Sprite[RENDERTEXTURE_COUNT];

			private int mCurrentRenderTextureIndex = 0;

			@Override
			public void onDrawFrame(final GLState pGLState) throws InterruptedException {
				final boolean firstFrame = !this.mRenderTextureInitialized;

				if(firstFrame) {
					this.initRenderTextures(pGLState);
					this.mRenderTextureInitialized = true;
				}

				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				final int currentRenderTextureIndex = this.mCurrentRenderTextureIndex;
				final int otherRenderTextureIndex = (currentRenderTextureIndex + 1) % RENDERTEXTURE_COUNT;

				this.mRenderTextures[currentRenderTextureIndex].begin(pGLState, false, true);
				{
					/* Draw current frame. */
					super.onDrawFrame(pGLState);

					/* Draw previous frame with reduced alpha. */
					if(!firstFrame) {
						if(MotionStreakExample.this.mMotionStreaking) {
							this.mRenderTextureSprites[otherRenderTextureIndex].setAlpha(0.9f);
							this.mRenderTextureSprites[otherRenderTextureIndex].onDraw(pGLState, this.mCamera);
						}
					}
				}
				this.mRenderTextures[currentRenderTextureIndex].end(pGLState);

				/* Draw combined frame with full alpha. */
				{
					pGLState.pushProjectionGLMatrix();
					pGLState.orthoProjectionGLMatrixf(0, surfaceWidth, 0, surfaceHeight, -1, 1);
					{
						this.mRenderTextureSprites[otherRenderTextureIndex].setAlpha(1);
						this.mRenderTextureSprites[otherRenderTextureIndex].onDraw(pGLState, this.mCamera);
					}
					pGLState.popProjectionGLMatrix();
				}

				/* Flip RenderTextures. */
				this.mCurrentRenderTextureIndex = otherRenderTextureIndex;
			}

			private void initRenderTextures(final GLState pGLState) {
				final int surfaceWidth = this.mCamera.getSurfaceWidth();
				final int surfaceHeight = this.mCamera.getSurfaceHeight();

				final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
				for(int i = 0; i <= 1; i++) {
					this.mRenderTextures[i] = new RenderTexture(MotionStreakExample.this.getTextureManager(), surfaceWidth, surfaceHeight);
					this.mRenderTextures[i].init(pGLState);

					final ITextureRegion renderTextureATextureRegion = TextureRegionFactory.extractFromTexture(this.mRenderTextures[i]);
					this.mRenderTextureSprites[i] = new Sprite(0, 0, renderTextureATextureRegion, vertexBufferObjectManager);
				}
			}
		};
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		/* Create a nice scene with some rectangles. */
		final Scene scene = new Scene();

		final Entity rectangleGroup = new Entity(MotionStreakExample.CAMERA_WIDTH / 2, MotionStreakExample.CAMERA_HEIGHT / 2);

		rectangleGroup.attachChild(this.makeColoredRectangle(-180, -180, 1, 0, 0));
		rectangleGroup.attachChild(this.makeColoredRectangle(0, -180, 0, 1, 0));
		rectangleGroup.attachChild(this.makeColoredRectangle(0, 0, 0, 0, 1));
		rectangleGroup.attachChild(this.makeColoredRectangle(-180, 0, 1, 1, 0));

		/* Spin the rectangles. */
		rectangleGroup.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new RotationModifier(10, 0, 7200, EaseQuadInOut.getInstance()), new DelayModifier(2))));

		scene.attachChild(rectangleGroup);
		
		/* TouchListener */
		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			MotionStreakExample.this.mMotionStreaking = !MotionStreakExample.this.mMotionStreaking;

			MotionStreakExample.this.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(MotionStreakExample.this, "MotionStreaking " + (MotionStreakExample.this.mMotionStreaking ? "enabled." : "disabled."), Toast.LENGTH_SHORT).show();
				}
			});
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	private Rectangle makeColoredRectangle(final float pX, final float pY, final float pRed, final float pGreen, final float pBlue) {
		final Rectangle coloredRect = new Rectangle(pX, pY, 180, 180, this.getVertexBufferObjectManager());
		coloredRect.setColor(pRed, pGreen, pBlue);
		return coloredRect;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
