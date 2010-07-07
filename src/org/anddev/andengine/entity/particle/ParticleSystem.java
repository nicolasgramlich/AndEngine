package org.anddev.andengine.entity.particle;

import static org.anddev.andengine.util.MathUtils.RANDOM;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.particle.modifier.IParticleInitializer;
import org.anddev.andengine.entity.particle.modifier.IParticleModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

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

	private final ArrayList<Particle> mParticles;
	private final ArrayList<Particle> mParticlesToRecycle;

	private final ArrayList<IParticleInitializer> mParticleInitializers = new ArrayList<IParticleInitializer>();
	private final ArrayList<IParticleModifier> mParticleModifiers = new ArrayList<IParticleModifier>();

	private final float mMinRate;
	private final float mMaxRate;

	private final TextureRegion mTextureRegion;

	private float mParticlesDueToSpawn;
	private final int mMaxParticles;
	private int mParticleModifierCount;
	private int mParticleInitializerCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParticleSystem(final float pX, final float pY, final float pWidth, final float pHeight, final float pMinRate, final float pMaxRate, final int pMaxParticles, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);
		this.mParticles = new ArrayList<Particle>(pMaxParticles);
		this.mParticlesToRecycle = new ArrayList<Particle>(pMaxParticles);
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
	protected void onManagedDraw(final GL10 pGL) {
		final ArrayList<Particle> particles = this.mParticles;
		for(int i = particles.size() - 1; i >= 0; i--) {
			particles.get(i).onDraw(pGL);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.spawnParticles(pSecondsElapsed);

		final ArrayList<Particle> particles = this.mParticles;
		final ArrayList<Particle> particlesToRecycle = this.mParticlesToRecycle;

		for(int i = particles.size() - 1; i >= 0; i--) {
			final Particle particle = particles.get(i);

			this.applyParticleModifiersOnUpdate(particle);
			particle.onUpdate(pSecondsElapsed);
			if(particle.isDead()){
				particles.remove(i);
				particlesToRecycle.add(particle);
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

		final int particlesToSpawnThisFrame = Math.min(this.mMaxParticles - this.mParticles.size(), (int)FloatMath.floor(this.mParticlesDueToSpawn));
		this.mParticlesDueToSpawn -= particlesToSpawnThisFrame;

		for(int i = 0; i < particlesToSpawnThisFrame; i++){
			this.spawnParticle();
		}
	}

	private void spawnParticle() {
		final ArrayList<Particle> particles = this.mParticles;
		final ArrayList<Particle> particlesToRecycle = this.mParticlesToRecycle;
		final Particle particle;

		if(!particlesToRecycle.isEmpty()){
			particle = particlesToRecycle.remove(particlesToRecycle.size() - 1);
			particle.reset();
		}else{
			final float x = this.getX() + RANDOM.nextFloat() * this.getWidthScaled();
			final float y = this.getY() + RANDOM.nextFloat() * this.getHeightScaled();
			if(particles.size() > 0) {
				particle = new Particle(x, y, this.mTextureRegion, particles.get(0).getVertexBuffer());
			} else {
				particle = new Particle(x, y, this.mTextureRegion);
			}
		}
		particle.setBlendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);

		this.applyParticleInitializersOnInitialize(particle);
		this.applyParticleModifiersOnInitialize(particle);

		particles.add(particle);
	}

	private float determineCurrentRate() {
		if(this.mMinRate == this.mMaxRate){
			return this.mMinRate;
		} else {
			return (RANDOM.nextFloat() * (this.mMaxRate - this.mMinRate)) + this.mMinRate;
		}
	}

	private void applyParticleInitializersOnInitialize(final Particle pParticle) {
		final ArrayList<IParticleInitializer> particleInitializers = this.mParticleInitializers;
		for(int i = this.mParticleInitializerCount - 1; i >= 0; i--) {
			particleInitializers.get(i).onInitializeParticle(pParticle);
		}
	}

	private void applyParticleModifiersOnInitialize(final Particle pParticle) {
		final ArrayList<IParticleModifier> particleModifiers = this.mParticleModifiers;
		for(int i = this.mParticleModifierCount - 1; i >= 0; i--) {
			particleModifiers.get(i).onInitializeParticle(pParticle);
		}
	}

	private void applyParticleModifiersOnUpdate(final Particle pParticle) {
		final ArrayList<IParticleModifier> particleModifiers = this.mParticleModifiers;
		for(int i = this.mParticleModifierCount - 1; i >= 0; i--) {
			particleModifiers.get(i).onUpdateParticle(pParticle);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
