package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 20:06:15 - 08.07.2010
 */
public class ChangeableTextExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Font mFont;

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
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48);
		this.mFont.load();
	}

	@Override
	public Scene onCreateScene() {
		final FPSCounter fpsCounter = new FPSCounter();
		this.mEngine.registerUpdateHandler(fpsCounter);

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final Text elapsedText = new Text(100, 160, this.mFont, "Seconds elapsed:", "Seconds elapsed: XXXXX".length(), this.getVertexBufferObjectManager());
		final Text fpsText = new Text(250, 240, this.mFont, "FPS:", "FPS: XXXXX".length(), this.getVertexBufferObjectManager());

		scene.attachChild(elapsedText);
		scene.attachChild(fpsText);

		scene.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				elapsedText.setText("Seconds elapsed: " + ChangeableTextExample.this.mEngine.getSecondsElapsedTotal());
				fpsText.setText("FPS: " + fpsCounter.getFPS());
			}
		}));

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
