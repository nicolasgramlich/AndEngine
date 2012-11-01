package org.andengine.entity.particle;

import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * The {@code SpriteParticleSystem} is a specific {@link ParticleSystem}, that
 * spawns {@link Sprite Sprites} as particles.
 * <p>
 * (c) Zynga 2011
 * 
 * @see ParticleSystem
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:00:23 - 04.12.2011
 */
public class SpriteParticleSystem extends ParticleSystem<Sprite> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Creates a new {@code SpriteParticleSystem}.
	 * 
	 * @param pParticleEmitter Responsible for calculating the emitting position of particles.
	 * @param pRateMinimum The minimum rate (roughly particles per second) at which particles 
	 *		should be spawned.
	 * @param pRateMaximum The maximum rate (roughly particles per second) at which particles
	 *		should be spawned.
	 * @param pParticlesMaximum The maximum amount of particles, that should be spawned
	 *		by that {@code ParticleSystem}. If there are too many particles on the screen,
	 *		the {@code ParticleSystem} will pause spawning, till some particles has expired
	 *		their lifespan (set by adding a {@link org.andengine.entity.particle.modifier.ExpireParticleInitializer}) 
	 *		to the {@code ParticleSystem}.
	 * @param pTextureRegion The {@link ITextureRegion} of the {@code Sprite}.
	 * @param pVertexBufferObjectManager The {@link VertexBufferObjectManager} that should be used
	 *		for drawing the sprites.
	 */
	public SpriteParticleSystem(final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
	}

	/**
	 * Creates a new {@code SpriteParticleSystem}.
	 * 
	 * @param pX The x-position of the {@code SpriteParticleSystem}.
	 * @param pY The y-position of the {@code SpriteParticleSystem}.
	 * @param pParticleEmitter Responsible for calculating the emitting position of particles.
	 * @param pRateMinimum The minimum rate (roughly particles per second) at which particles 
	 *		should be spawned.
	 * @param pRateMaximum The maximum rate (roughly particles per second) at which particles
	 *		should be spawned.
	 * @param pParticlesMaximum The maximum amount of particles, that should be spawned
	 *		by that {@code ParticleSystem}. If there are too many particles on the screen,
	 *		the {@code ParticleSystem} will pause spawning, till some particles has expired
	 *		their lifespan (set by adding a {@link org.andengine.entity.particle.modifier.ExpireParticleInitializer}) 
	 *		to the {@code ParticleSystem}.
	 * @param pTextureRegion The {@link ITextureRegion} of the {@code Sprite}.
	 * @param pVertexBufferObjectManager The {@link VertexBufferObjectManager} that should be used
	 *		for drawing the sprites.
	 */
	public SpriteParticleSystem(final float pX, final float pY, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, new IEntityFactory<Sprite>() {
			@Override
			public Sprite create(final float pX, final float pY) {
				return new Sprite(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			}
		}, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);
	}

	/**
	 * Create a new {@link SpriteParticleSystem} with the given parameters.
	 * 
	 * @param pX The x-position of the {@code SpriteParticleSystem}.
	 * @param pY The y-position of the {@code SpriteParticleSystem}.
	 * @param pEntityFactory Responsible for creating the {@link IEntity} 
	 *		for each particle.
	 * @param pParticleEmitter Responsible for calculating the emitting position of particles.
	 * @param pRateMinimum The minimum rate (roughly particles per second) at which particles 
	 *		should be spawned.
	 * @param pRateMaximum The maximum rate (roughly particles per second) at which particles
	 *		should be spawned.
	 * @param pParticlesMaximum The maximum amount of particles, that should be spawned
	 *		by that {@code ParticleSystem}. If there are too many particles on the screen,
	 *		the {@code ParticleSystem} will pause spawning, till some particles has expired
	 *		their lifespan (set by adding a {@link ExpireParticleInitializer}) to the 
	 *		{@code ParticleSystem}.
	 */
	protected SpriteParticleSystem(final float pX, final float pY, final IEntityFactory<Sprite> pEntityFactory, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum) {
		super(pX, pY, pEntityFactory, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
