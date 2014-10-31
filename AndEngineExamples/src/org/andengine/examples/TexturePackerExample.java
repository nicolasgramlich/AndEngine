package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.spritesheets.TexturePackerExampleSpritesheet;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

/**
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 9:55:51 - 02.08.2011
 */
public class TexturePackerExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private TexturePackTextureRegionLibrary mSpritesheetTexturePackTextureRegionLibrary;

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
		try {
			final TexturePack spritesheetTexturePack = new TexturePackLoader(this.getTextureManager(), "gfx/spritesheets/").loadFromAsset(this.getAssets(), "texturepackerexample.xml");
			spritesheetTexturePack.loadTexture();
			this.mSpritesheetTexturePackTextureRegionLibrary = spritesheetTexturePack.getTexturePackTextureRegionLibrary();
		} catch (final TexturePackParseException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(1, 1, 1));

		TextureRegion faceTextureRegion = this.mSpritesheetTexturePackTextureRegionLibrary.get(TexturePackerExampleSpritesheet.FACE_BOX_ID);
		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - faceTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - faceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		Sprite entity = new Sprite(centerX, centerY, faceTextureRegion, this.getVertexBufferObjectManager());
		entity.setScale(20);
		scene.attachChild(entity);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
