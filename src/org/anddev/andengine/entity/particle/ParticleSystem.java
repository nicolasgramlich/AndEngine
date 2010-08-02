package org.anddev.andengine.entity.particle;

import static org.anddev.andengine.util.MathUtils.RANDOM;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.particle.modifier.IParticleInitializer;
import org.anddev.andengine.entity.particle.modifier.IParticleModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

import android.util.FloatMath;

/**
 * @author Nicolas Gramlich
 * @since 19:42:27 - 14.03.2010
 */
public class ParticleSystem extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Particle[] mParticles;

	private final ArrayList<IParticleInitializer> mParticleInitializers = new ArrayList<IParticleInitializer>();
	private final ArrayList<IParticleModifier> mParticleModifiers = new ArrayList<IParticleModifier>();

	private final float mMinRate;
	private final float mMaxRate;

	private final TextureRegion mTextureRegion;

	private float mParticlesDueToSpawn;
	private final int mMaxParticles;
	private int mParticlesAlive;

	private int mParticleModifierCount;
	private int mParticleInitializerCount;

	private RectangleVertexBuffer mSharedParticleVertexBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParticleSystem(final float pX, final float pY, final float pWidth, final float pHeight, final float pMinRate, final float pMaxRate, final int pMaxParticles, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);
		this.mParticles = new Particle[pMaxParticles];
		this.mMinRate = pMinRate;
		this.mMaxRate = pMaxRate;
		this.mMaxParticles = pMaxParticles;
		this.mTextureRegion = pTextureRegion;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onPositionChanged() {
		/* Nothing */
	}

	@Override
	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		final Particle[] particles = this.mParticles;
		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			particles[i].onDraw(pGL, pCamera);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.spawnParticles(pSecondsElapsed);

		final Particle[] particles = this.mParticles;

		final ArrayList<IParticleModifier> particleModifiers = this.mParticleModifiers;
		final int particleModifierCountMinusOne = this.mParticleModifierCount - 1;

		for(int i = this.mParticlesAlive - 1; i >= 0; i--) {
			final Particle particle = particles[i];

			/* Apply all particleModifiers */
			for(int j = particleModifierCountMinusOne; j >= 0; j--) {
				particleModifiers.get(j).onUpdateParticle(particle);
			}

			particle.onUpdate(pSecondsElapsed);
			if(particle.mDead){
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

	public void addParticleModifier(final IParticleModifier pParticleModifier) {
		this.mParticleModifiers.add(pParticleModifier);
		this.mParticleModifierCount++;
	}

	public void removeParticleModifier(final IParticleModifier pParticleModifier) {
		this.mParticleModifierCount--;
		this.mParticleModifiers.remove(pParticleModifier);
	}

	public void addParticleInitializer(final IParticleInitializer pParticleInitializer) {
		this.mParticleInitializers.add(pParticleInitializer);
		this.mParticleInitializerCount++;
	}

	public void removeParticleInitializer(final IParticleInitializer pParticleInitializer) {
		this.mParticleInitializerCount--;
		this.mParticleInitializers.remove(pParticleInitializer);
	}

	private void spawnParticles(final float pSecondsElapsed) {
		final float currentRate = this.determineCurrentRate();
		final float newParticlesThisFrame = currentRate * pSecondsElapsed;

		this.mParticlesDueToSpawn += newParticlesThisFrame;

		final int particlesToSpawnThisFrame = Math.min(this.mMaxParticles - this.mParticlesAlive, (int)FloatMath.floor(this.mParticlesDueToSpawn));
		this.mParticlesDueToSpawn -= particlesToSpawnThisFrame;

		for(int i = 0; i < particlesToSpawnThisFrame; i++){
			this.spawnParticle();
		}
	}

	private void spawnParticle() {
		final Particle[] particles = this.mParticles;

		final int particlesAlive = this.mParticlesAlive;
		if(particlesAlive < this.mMaxParticles){
			Particle particle = particles[particlesAlive];
			if(particle != null) {
				particle.reset();
			} else {
				/* New particle needs to be created. */
				final float x = this.getX() + RANDOM.nextFloat() * this.getWidthScaled();
				final float y = this.getY() + RANDOM.nextFloat() * this.getHeightScaled();

				if(particlesAlive == 0) {
					/* This is the very first particle. */
					particle = new Particle(x, y, this.mTextureRegion);
					this.mSharedParticleVertexBuffer = particle.getVertexBuffer();
				} else {
					particle = new Particle(x, y, this.mTextureRegion, this.mSharedParticleVertexBuffer);
				}
				particles[particlesAlive] = particle;
			}
			particle.setBlendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);

			/* Apply particle initializers. */
			{
				final ArrayList<IParticleInitializer> particleInitializers = this.mParticleInitializers;
				for(int i = this.mParticleInitializerCount - 1; i >= 0; i--) {
					particleInitializers.get(i).onInitializeParticle(particle);
				}

				final ArrayList<IParticleModifier> particleModifiers = this.mParticleModifiers;
				for(int i = this.mParticleModifierCount - 1; i >= 0; i--) {
					particleModifiers.get(i).onInitializeParticle(particle);
				}
			}

			this.mParticlesAlive++;
		}
	}

	private float determineCurrentRate() {
		if(this.mMinRate == this.mMaxRate){
			return this.mMinRate;
		} else {
			return (RANDOM.nextFloat() * (this.mMaxRate - this.mMinRate)) + this.mMinRate;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
