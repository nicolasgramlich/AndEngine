package org.andengine.entity.particle;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;

/**
 * TODO Check if SpriteBatch can be used here to improve performance.
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:42:27 - 14.03.2010
 */
public class ParticleSystem<T extends Entity> extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	private final float[] POSITION_OFFSET = new float[2];

	// ===========================================================
	// Fields
	// ===========================================================

	private final IEntityFactory<T> mEntityFactory;
	private final IParticleEmitter mParticleEmitter;

	private final Particle<T>[] mParticles;

	private final ArrayList<IParticleInitializer<T>> mParticleInitializers = new ArrayList<IParticleInitializer<T>>();
	private final ArrayList<IParticleModifier<T>> mParticleModifiers = new ArrayList<IParticleModifier<T>>();

	private final float mRateMinimum;
	private final float mRateMaximum;

	private boolean mParticlesSpawnEnabled = true;

	private final int mParticlesMaximum;
	private int mParticlesAlive;
	private float mParticlesDueToSpawn;

	private int mParticleModifierCount;
	private int mParticleInitializerCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParticleSystem(final IEntityFactory<T> pEntityFactory, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum) {
		this(0, 0, pEntityFactory, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);
	}

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

	public boolean isParticlesSpawnEnabled() {
		return this.mParticlesSpawnEnabled;
	}

	public void setParticlesSpawnEnabled(final boolean pParticlesSpawnEnabled) {
		this.mParticlesSpawnEnabled = pParticlesSpawnEnabled;
	}

	public IEntityFactory<T> getParticleFactory() {
		return this.mEntityFactory;
	}

	public IParticleEmitter getParticleEmitter() {
		return this.mParticleEmitter;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void reset() {
		super.reset();

		this.mParticlesDueToSpawn = 0;
		this.mParticlesAlive = 0;
	}

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		final Particle<T>[] particles = this.mParticles;
		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			particles[i].onDraw(pGLState, pCamera);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if(this.isParticlesSpawnEnabled()) {
			this.spawnParticles(pSecondsElapsed);
		}

		final Particle<T>[] particles = this.mParticles;

		final ArrayList<IParticleModifier<T>> particleModifiers = this.mParticleModifiers;
		final int particleModifierCountMinusOne = this.mParticleModifierCount - 1;

		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			final Particle<T> particle = particles[i];

			/* Apply all particleModifiers */
			for(int j = particleModifierCountMinusOne; j >= 0; j--) {
				particleModifiers.get(j).onUpdateParticle(particle);
			}

			particle.onUpdate(pSecondsElapsed);
			if(particle.mExpired){
				this.mParticlesAlive--;
				final int particlesAlive = this.mParticlesAlive;
				particles[i] = particles[particlesAlive];
				particles[particlesAlive] = particle;
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addParticleModifier(final IParticleModifier<T> pParticleModifier) {
		this.mParticleModifiers.add(pParticleModifier);
		this.mParticleModifierCount++;
	}

	public void removeParticleModifier(final IParticleModifier<T> pParticleModifier) {
		this.mParticleModifierCount--;
		this.mParticleModifiers.remove(pParticleModifier);
	}

	public void addParticleInitializer(final IParticleInitializer<T> pParticleInitializer) {
		this.mParticleInitializers.add(pParticleInitializer);
		this.mParticleInitializerCount++;
	}

	public void removeParticleInitializer(final IParticleInitializer<T> pParticleInitializer) {
		this.mParticleInitializerCount--;
		this.mParticleInitializers.remove(pParticleInitializer);
	}

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

	private void spawnParticle() {
		final Particle<T>[] particles = this.mParticles;

		final int particlesAlive = this.mParticlesAlive;
		if(particlesAlive < this.mParticlesMaximum){
			Particle<T> particle = particles[particlesAlive];

			/* New particle needs to be created. */
			this.mParticleEmitter.getPositionOffset(this.POSITION_OFFSET);

			final float x = this.POSITION_OFFSET[Constants.VERTEX_INDEX_X];
			final float y = this.POSITION_OFFSET[Constants.VERTEX_INDEX_Y];

			if(particle == null) {
				particle = new Particle<T>();
				particles[particlesAlive] = particle;
				particle.setEntity(this.mEntityFactory.create(x, y));
			} else {
				particle.reset();
				particle.getEntity().setPosition(x, y);
			}

			/* Apply particle initializers. */
			{
				final ArrayList<IParticleInitializer<T>> particleInitializers = this.mParticleInitializers;
				for(int i = this.mParticleInitializerCount - 1; i >= 0; i--) {
					particleInitializers.get(i).onInitializeParticle(particle);
				}

				final ArrayList<IParticleModifier<T>> particleModifiers = this.mParticleModifiers;
				for(int i = this.mParticleModifierCount - 1; i >= 0; i--) {
					particleModifiers.get(i).onInitializeParticle(particle);
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
