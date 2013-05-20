package org.andengine.entity.particle;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.ColorUtils;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:32:29 - 09.05.2012
 */
public class BatchedSpriteParticleSystem extends BlendFunctionParticleSystem<UncoloredSprite> {
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

	public BatchedSpriteParticleSystem(final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
	}

	public BatchedSpriteParticleSystem(final float pX, final float pY, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, new IEntityFactory<UncoloredSprite>() {
			@Override
			public UncoloredSprite create(final float pX, final float pY) {
				/* We can create an uncolored sprite, since */
				return new UncoloredSprite(pX, pY, pTextureRegion, pVertexBufferObjectManager);
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

		final Particle<UncoloredSprite>[] particles = this.mParticles;
		for (int i = this.mParticlesAlive - 1; i >= 0; i--) {
			final Sprite sprite = particles[i].getEntity();

			/* In order to support alpha changes of the sprites inside the spritebatch,
			 * we have to 'premultiply' the RGB channels of the sprite with its alpha channel. */
			final float alpha = sprite.getAlpha();
			final float colorABGRPackedInt = ColorUtils.convertRGBAToABGRPackedFloat(sprite.getRed() * alpha, sprite.getGreen() * alpha, sprite.getBlue() * alpha, alpha);

			this.mSpriteBatch.drawWithoutChecks(sprite, colorABGRPackedInt);
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
}
