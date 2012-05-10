package org.andengine.entity.particle;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem.PseudoSprite;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.ColorUtils;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:39:46 - 10.05.2012
 */
public class BatchedPseudoSpriteParticleSystem extends BlendFunctionParticleSystem<PseudoSprite> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final SpriteBatch mSpriteBatch;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BatchedPseudoSpriteParticleSystem(final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
	}

	public BatchedPseudoSpriteParticleSystem(final float pX, final float pY, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, new IEntityFactory<PseudoSprite>() {
			@Override
			public PseudoSprite create(final float pX, final float pY) {
				return new PseudoSprite(pX, pY, pTextureRegion);
			}
		}, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);

		this.mSpriteBatch = new SpriteBatch(pTextureRegion.getTexture(), pParticlesMaximum, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteBatch.setIndex(0);

		final Particle<PseudoSprite>[] particles = this.mParticles;
		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			final PseudoSprite pseudoSprite = particles[i].getEntity();

			/* In order to support alpha changes of the sprites inside the spritebatch,
			 * we have to 'premultiply' the RGB channels of the sprite with its alpha channel. */
			final float alpha = pseudoSprite.getAlpha();
			final float colorABGRPackedInt = ColorUtils.convertRGBAToABGRPackedFloat(pseudoSprite.getRed() * alpha, pseudoSprite.getGreen() * alpha, pseudoSprite.getBlue() * alpha, alpha);

			this.mSpriteBatch.drawWithoutChecks(pseudoSprite.getTextureRegion(), pseudoSprite, colorABGRPackedInt);
		}
		this.mSpriteBatch.submit();

		this.mSpriteBatch.onDraw(pGLState, pCamera);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class PseudoSprite extends Entity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final ITextureRegion mTextureRegion;

		// ===========================================================
		// Constructors
		// ===========================================================

		public PseudoSprite(float pX, float pY, final ITextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight());

			this.mTextureRegion = pTextureRegion;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public ITextureRegion getTextureRegion() {
			return this.mTextureRegion;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
