package org.andengine.examples;


import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ParticleSystemCoolExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mParticleTextureRegion;

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
		this.mCamera = new Camera(0, 0, ParticleSystemCoolExample.CAMERA_WIDTH, ParticleSystemCoolExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(ParticleSystemCoolExample.CAMERA_WIDTH, ParticleSystemCoolExample.CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "particle_fire.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.0f, 0.0f, 0.0f));

		/* Left to right Particle System. */
		{
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(0, ParticleSystemCoolExample.CAMERA_HEIGHT), 6, 10, 200, this.mParticleTextureRegion, this.getVertexBufferObjectManager());
			particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(15, 22, -60, -90));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 15));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1.0f, 0.0f, 0.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(11.5f));

			particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(2.5f, 3.5f, 1.0f, 0.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(3.5f, 4.5f, 0.0f, 1.0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 11.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(4.5f, 11.5f, 1.0f, 0.0f));

			scene.attachChild(particleSystem);
		}

		/* Right to left Particle System. */
		{
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(ParticleSystemCoolExample.CAMERA_WIDTH - 32, ParticleSystemCoolExample.CAMERA_HEIGHT), 8, 12, 200, this.mParticleTextureRegion, this.getVertexBufferObjectManager());
			particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-15, -22, -60, -90));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(0.0f, 0.0f, 1.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(11.5f));

			particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(2.5f, 3.5f, 1.0f, 0.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(3.5f, 4.5f, 0.0f, 1.0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 11.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(4.5f, 11.5f, 1.0f, 0.0f));

			scene.attachChild(particleSystem);
		}

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
