package org.andengine.extension.robotium.example;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:08:42 - 14.02.2012
 */
public class AndEngineRobotiumExtensionExample extends SimpleLayoutGameActivity implements RobotiumDebugTags {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 72;
	private static final int CAMERA_HEIGHT = 48;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	private EditText mEditTextValueA;
	private EditText mEditTextValueB;
	private TextView mTextViewResult;
	private Button mButtonCalculate;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void onSetContentView() {
		super.onSetContentView();

		this.mEditTextValueA = (EditText) this.findViewById(R.id.EditText01);

		this.mEditTextValueB = (EditText) this.findViewById(R.id.EditText02);

		this.mTextViewResult = (TextView) this.findViewById(R.id.TextView01);
		this.mTextViewResult.setText("0.00");

		this.mButtonCalculate = (Button) this.findViewById(R.id.Button01);

		// Adding listener to button
		this.mButtonCalculate.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				final float num1 = Float.parseFloat(AndEngineRobotiumExtensionExample.this.mEditTextValueA.getText().toString());
				final float num2 = Float.parseFloat(AndEngineRobotiumExtensionExample.this.mEditTextValueB.getText().toString());
				final float result = num1 * num2;
				AndEngineRobotiumExtensionExample.this.mTextViewResult.setText(String.valueOf(result));
			}
		});
	}

	@Override
	protected int getLayoutID() {
		return R.layout.main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.main_rendersurfaceview;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, AndEngineRobotiumExtensionExample.CAMERA_WIDTH, AndEngineRobotiumExtensionExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(AndEngineRobotiumExtensionExample.CAMERA_WIDTH, AndEngineRobotiumExtensionExample.CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);

		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "box.png", 0, 0);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	protected Scene onCreateScene() {
		final Scene scene = new Scene();

		final float boxCenterX = (AndEngineRobotiumExtensionExample.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float boxCenterY = (AndEngineRobotiumExtensionExample.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		final Sprite box = new Sprite(boxCenterX, boxCenterY, this.mFaceTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setVisible(false);
				}
				return true;
			}
		};
		box.setUserData(RobotiumDebugTags.ROBOTIUM_TAG_BOX);
		scene.registerTouchArea(box);
		scene.attachChild(box);

		return scene;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
