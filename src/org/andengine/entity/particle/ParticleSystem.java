package org.andengine.entity.particle;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;

/**
 * 
 * A {@code ParticleSystem} is a system responsible for spawning {@link Particle Particles}
 * on the screen. A {@code Particle} can be any {@link IEntity}. The kind of
 * spawned particles is controlled by the generic type of this class.
 * <p>
 * You can use a specific {@link IParticleEmitter} set via the constructor to control 
 * the emitting of the particles.
 * <p>
 * You can add several {@link IParticleInitializer} via the 
 * {@link #addParticleInitializer(org.andengine.entity.particle.initializer.IParticleInitializer)}
 * method to control the initialization state of each particle.
 * <p>
 * You can add several {@link IParticleModifier} via the
 * {@link #addParticleModifier(org.andengine.entity.particle.modifier.IParticleModifier)}
 * method to control modification of particles during their lifetime.
 * <p>
 * A {@code ParticleSystem} will continue spawning {@code Particles} until you 
 * call {@link #setParticlesSpawnEnabled(boolean) setParticlesSpawnEnabled(false)}.
 * <p>
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @see Particle
 * @see IParticleEmitter
 * @see IParticleInitializer
 * @see IParticleModifier
 * 
 * @author Nicolas Gramlich
 * @since 19:42:27 - 14.03.2010
 */
public class ParticleSystem<T extends IEntity> extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float[] POSITION_OFFSET_CONTAINER = new float[2];

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IEntityFactory<T> mEntityFactory;
	protected final IParticleEmitter mParticleEmitter;

	protected final Particle<T>[] mParticles;

	protected final ArrayList<IParticleInitializer<T>> mParticleInitializers = new ArrayList<IParticleInitializer<T>>();
	protected final ArrayList<IParticleModifier<T>> mParticleModifiers = new ArrayList<IParticleModifier<T>>();

	private final float mRateMinimum;
	private final float mRateMaximum;

	private boolean mParticlesSpawnEnabled = true;

	protected final int mParticlesMaximum;
	protected int mParticlesAlive;
	private float mParticlesDueToSpawn;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Create a new {@link ParticleSystem} with the given parameters.
	 * 
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
	 *		their lifespan (set by adding a {@link org.andengine.entity.particle.modifier.ExpireParticleInitializer}) 
	 *		to the {@code ParticleSystem}.
	 */
	public ParticleSystem(final IEntityFactory<T> pEntityFactory, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum) {
		this(0, 0, pEntityFactory, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);
	}

	/**
	 * Create a new {@link ParticleSystem} with the given parameters.
	 * 
	 * @param pX The x-position of the {@code ParticleSystem}.
	 * @param pY The y-position of the {@code ParticleSystem}.
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
	@SuppressWarnings("unchecked")
	public ParticleSystem(final float pX, final float pY, final IEntityFactory<T> pEntityFactory, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum) {
		super(pX, pY);

		this.mEntityFactory = pEntityFactory;
		this.mParticleEmitter = pParticleEmitter;
		this.mParticles = (Particle<T>[])new Particle[pParticlesMaximum];
		this.mRateMinimum = pRateMinimum;
		this.mRateMaximum = pRateMaximum;
		this.mParticlesMaximum = pParticlesMaximum;

		this.registerUpdateHandler(this.mParticleEmitter);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Returns whether spawning of particles if currently enabled in this system.
	 * 
	 * @return Whether this system is currently allowed to spawn new particles.
	 */
	public boolean isParticlesSpawnEnabled() {
		return this.mParticlesSpawnEnabled;
	}

	/**
	 * Enables or disables the spawning of new particles by this {@link ParticleSystem}.
	 * Use this to stop the {@code ParticleSystem} creating new particles.
	 * 
	 * @param pParticlesSpawnEnabled Whether the system should spawn new particles.
	 */
	public void setParticlesSpawnEnabled(final boolean pParticlesSpawnEnabled) {
		this.mParticlesSpawnEnabled = pParticlesSpawnEnabled;
	}

	/**
	 * Returns the {@link IEntityFactory} that is used to create new particles.
	 * 
	 * @return The {@code IEntityFactory} that is used to create new particles.
	 */
	public IEntityFactory<T> getParticleFactory() {
		return this.mEntityFactory;
	}

	/**
	 * Returns the {@link IParticleEmitter} that is used to determine the position
	 * of new emitted particles.
	 * 
	 * @return The {@code IParticleEmitter} of this {@code ParticleSystem}.
	 */
	public IParticleEmitter getParticleEmitter() {
		return this.mParticleEmitter;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Resets the {@code ParticleSystem}. This will erase all currently living 
	 * particles of this system. 
	 */
	@Override
	public void reset() {
		super.reset();

		this.mParticlesDueToSpawn = 0;
		this.mParticlesAlive = 0;
	}

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			this.mParticles[i].onDraw(pGLState, pCamera);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if(this.isParticlesSpawnEnabled()) {
			this.spawnParticles(pSecondsElapsed);
		}

		final int particleModifierCountMinusOne = this.mParticleModifiers.size() - 1;
		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			final Particle<T> particle = this.mParticles[i];

			/* Apply all particleModifiers */
			for(int j = particleModifierCountMinusOne; j >= 0; j--) {
				this.mParticleModifiers.get(j).onUpdateParticle(particle);
			}

			particle.onUpdate(pSecondsElapsed);
			if(particle.mExpired){
				this.mParticlesAlive--;

				this.moveParticleToEnd(i);
			}
		}
	}

	protected void moveParticleToEnd(final int pIndex) {
		final Particle<T> particle = this.mParticles[pIndex];

		final int particlesToCopy = this.mParticlesAlive - pIndex;
		if(particlesToCopy > 0) {
			System.arraycopy(this.mParticles, pIndex + 1, this.mParticles, pIndex, particlesToCopy);
		}
		this.mParticles[this.mParticlesAlive] = particle;

		/* This mode of swapping particles is faster than copying tons of array elements, 
		 * but it doesn't respect the 'lifetime' of the particles. */
//		particles[i] = particles[this.mParticlesAlive];
//		particles[this.mParticlesAlive] = particle;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Adds an {@link IParticleModifier} to this {@code ParticleSystem}. An
	 * {@code IParticleModifier} is responsible for changing particles during
	 * their lifespan.
	 * 
	 * @param pParticleModifier The {@link IParticleModifier} to add to this system.
	 * 	 
	 * @see IParticleModifier
	 */
	public void addParticleModifier(final IParticleModifier<T> pParticleModifier) {
		this.mParticleModifiers.add(pParticleModifier);
	}

	/**
	 * Removes an {@link IParticleModifier} from this {@code ParticleSystem}.
	 * 
	 * @param pParticleModifier The {@link IParticleModifier} to remove from this system.
	 * 
	 * @see IParticleModifier
	 */
	public void removeParticleModifier(final IParticleModifier<T> pParticleModifier) {
		this.mParticleModifiers.remove(pParticleModifier);
	}

	/**
	 * Adds an {@link IParticleInitializer} to this {@code ParticleSystem}. An 
	 * {@link IParticleInitializer} is responsible for setting the initial state
	 * of a new emitted {@link Particle}.
	 * 
	 * @param pParticleInitializer The {@link IParticleInitializer} to add to this system.
	 * 
	 * @see IParticleInitializer
	 */
	public void addParticleInitializer(final IParticleInitializer<T> pParticleInitializer) {
		this.mParticleInitializers.add(pParticleInitializer);
	}

	/**
	 * Removes an {@link IParticleInitializer} to this {@code ParticleSystem}. 
	 * 
	 * @param pParticleInitializer The {@link IParticleInitializer} to remove from this system.
	 * 
	 * @see IParticleInitializer
	 */
	public void removeParticleInitializer(final IParticleInitializer<T> pParticleInitializer) {
		this.mParticleInitializers.remove(pParticleInitializer);
	}

	/**
	 * Spawns new {@link Particle Particles} depending on the passed time.
	 *
	 * @param pSecondsElapsed The elapsed time.
	 */
	private void spawnParticles(final float pSecondsElapsed) {
		final float currentRate = this.determineCurrentRate();
		final float newParticlesThisFrame = currentRate * pSecondsElapsed;

		this.mParticlesDueToSpawn += newParticlesThisFrame;

		final int particlesToSpawnThisFrame = Math.min(this.mParticlesMaximum - this.mParticlesAlive, (int)FloatMath.floor(this.mParticlesDueToSpawn));
		this.mParticlesDueToSpawn -= particlesToSpawnThisFrame;

		for(int i = 0; i < particlesToSpawnThisFrame; i++){
			this.spawnParticle();
		}
	}

	/**
	 * Spawns a new particle.
	 */
	private void spawnParticle() {
		if(this.mParticlesAlive < this.mParticlesMaximum){
			Particle<T> particle = this.mParticles[this.mParticlesAlive];

			/* New particle needs to be created. */
			this.mParticleEmitter.getPositionOffset(ParticleSystem.POSITION_OFFSET_CONTAINER);

			final float x = ParticleSystem.POSITION_OFFSET_CONTAINER[Constants.VERTEX_INDEX_X];
			final float y = ParticleSystem.POSITION_OFFSET_CONTAINER[Constants.VERTEX_INDEX_Y];

			if(particle == null) {
				particle = new Particle<T>();
				this.mParticles[this.mParticlesAlive] = particle;
				particle.setEntity(this.mEntityFactory.create(x, y));
			} else {
				particle.reset();
				particle.getEntity().setPosition(x, y);
			}

			/* Apply particle initializers. */
			{
				for(int i = this.mParticleInitializers.size() - 1; i >= 0; i--) {
					this.mParticleInitializers.get(i).onInitializeParticle(particle);
				}

				for(int i = this.mParticleModifiers.size() - 1; i >= 0; i--) {
					this.mParticleModifiers.get(i).onInitializeParticle(particle);
				}
			}

			this.mParticlesAlive++;
		}
	}

	protected float determineCurrentRate() {
		if(this.mRateMinimum == this.mRateMaximum){
			return this.mRateMinimum;
		} else {
			return MathUtils.random(this.mRateMinimum, this.mRateMaximum);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
