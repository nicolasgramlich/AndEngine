package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseLinear;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class CardinalSplineMoveModifierExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int COUNT = 400;
	private static final float DURATION = 4;
	private static final float SIZE = 25;

	private static final float[] CONTROLPOINT_1_XS = {
		2 * (CAMERA_WIDTH / 4),
		1 * (CAMERA_WIDTH / 4),
		1.5f * (CAMERA_WIDTH / 4),
		2 * (CAMERA_WIDTH / 4)
	};

	private static final float[] CONTROLPOINT_2_XS = {
		2 * (CAMERA_WIDTH / 4),
		3 * (CAMERA_WIDTH / 4),
		2.5f * (CAMERA_WIDTH / 4),
		2 * (CAMERA_WIDTH / 4)
	};

	private static final float[] CONTROLPOINT_YS = {
		3.5f * (CAMERA_HEIGHT / 4),
		2 * (CAMERA_HEIGHT / 4),
		1 * (CAMERA_HEIGHT / 4),
		1.5f * (CAMERA_HEIGHT / 4),
	};

	// ===========================================================
	// Fields
	// ===========================================================

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

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0, 0, 0));

		for(int i = 0; i < COUNT; i++) {
			final float tension = MathUtils.random(-0.5f, 0.5f);
			this.addRectangleWithTension(scene, tension, MathUtils.random(0, DURATION * 2f));
		}

		return scene;
	}

	private void addRectangleWithTension(final Scene pScene, final float pTension, float pDelay) {
		final Rectangle rectangle = new Rectangle(-SIZE, -SIZE, SIZE, SIZE, this.getVertexBufferObjectManager());
		rectangle.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		if(pTension < 0) {
			rectangle.setColor(1 - pTension, 0, 0, 0.5f);
		} else {
			rectangle.setColor(pTension, 0, 0, 0.5f);
		}

		final CardinalSplineMoveModifierConfig catmullRomMoveModifierConfig1 = new CardinalSplineMoveModifierConfig(CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS.length, pTension);
		final CardinalSplineMoveModifierConfig catmullRomMoveModifierConfig2 = new CardinalSplineMoveModifierConfig(CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS.length, pTension);

		for(int i = 0; i < CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS.length; i++) {
			catmullRomMoveModifierConfig1.setControlPoint(i, CardinalSplineMoveModifierExample.CONTROLPOINT_1_XS[i] - SIZE / 2, CardinalSplineMoveModifierExample.CONTROLPOINT_YS[i] - SIZE / 2);
			catmullRomMoveModifierConfig2.setControlPoint(i, CardinalSplineMoveModifierExample.CONTROLPOINT_2_XS[i] - SIZE / 2, CardinalSplineMoveModifierExample.CONTROLPOINT_YS[i] - SIZE / 2);
		}

		rectangle.registerEntityModifier(
			new SequenceEntityModifier(
				new DelayModifier(pDelay),
				new LoopEntityModifier(
					new SequenceEntityModifier(
						new ParallelEntityModifier(
							new CardinalSplineMoveModifier(CardinalSplineMoveModifierExample.DURATION, catmullRomMoveModifierConfig1, EaseLinear.getInstance()),
							new RotationModifier(CardinalSplineMoveModifierExample.DURATION, -45, -315)
						),
						new ParallelEntityModifier(
							new CardinalSplineMoveModifier(CardinalSplineMoveModifierExample.DURATION, catmullRomMoveModifierConfig2, EaseLinear.getInstance()),
							new RotationModifier(CardinalSplineMoveModifierExample.DURATION, 45, 315)
						)
					)
				)
			)
		);

		pScene.attachChild(rectangle);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
