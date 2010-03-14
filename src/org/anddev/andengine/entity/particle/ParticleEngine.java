package org.anddev.andengine.entity.particle;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.DynamicEntity;
import org.anddev.andengine.opengl.texture.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 19:42:27 - 14.03.2010
 */
public class ParticleEngine extends DynamicEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<Particle> mParticles = new ArrayList<Particle>();
	private final ArrayList<IParticleModifier> mParticleModifiers = new ArrayList<IParticleModifier>();

	private final LinkedList<Particle> mParticlesToRecycle = new LinkedList<Particle>();

	private final float mMinRate;
	private final float mMaxRate;

	private final TextureRegion mTextureRegion;
	
	private float mParticlesDueToSpawn;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParticleEngine(final float pX, final float pY, final int pWidth, final int pHeight, final float pMinRate, final float pMaxRate, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);
		this.mMinRate = pMinRate;
		this.mMaxRate = pMaxRate;
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
		final LinkedList<Particle> particlesToRecycle = this.mParticlesToRecycle;
		for(int i = particles.size() - 1; i >= 0; i--) {
			final Particle particle = particles.get(i);
			
			this.applyParticleModifiersOnUpdate(particle);
			particle.onUpdate(pSecondsElapsed);
			if(particle.isDead()){
				particlesToRecycle.add(particle);
			}
		}
		
		particles.removeAll(particlesToRecycle);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void addParticleModifier(final IParticleModifier pParticleModifier) {
		this.mParticleModifiers.add(pParticleModifier);
	}
	
	public void removeParticleModifier(final IParticleModifier pParticleModifier) {
		this.mParticleModifiers.remove(pParticleModifier);
	}

	private void spawnParticles(final float pSecondsElapsed) {
		final float currentRate = determineCurrentRate();
		final float newParticlesThisFrame = currentRate * pSecondsElapsed;
		
		this.mParticlesDueToSpawn += newParticlesThisFrame;
		
		final int particlesToSpawnThisFrame = (int)Math.floor(this.mParticlesDueToSpawn);
		this.mParticlesDueToSpawn -= particlesToSpawnThisFrame;
		
		for(int i = 0; i < particlesToSpawnThisFrame; i++){
			this.spawnParticle();
		}
	}

	private void spawnParticle() {
		final Particle particle;
		if(!this.mParticlesToRecycle.isEmpty()){
			particle = this.mParticlesToRecycle.poll();
			particle.reset();
		}else{
			final float x = this.getX() + (float)Math.random() * this.getWidth();
			final float y = this.getY() + (float)Math.random() * this.getHeight();
			particle = new Particle(x, y, this.mTextureRegion);
		}
		applyParticleModifiersOnInitialize(particle);
		this.mParticles.add(particle);
	}

	private float determineCurrentRate() {
		if(this.mMinRate == this.mMaxRate){
			return this.mMinRate;
		} else {
			return (float)(Math.random() * (this.mMaxRate - this.mMinRate)) + this.mMinRate;
		}
	}

	private void applyParticleModifiersOnUpdate(final Particle pParticle) {
		final ArrayList<IParticleModifier> particleModifiers = this.mParticleModifiers;
		for(int i = particleModifiers.size() - 1; i >= 0; i--) {
			particleModifiers.get(i).onUpdateParticle(pParticle);
		}
	}
	
	private void applyParticleModifiersOnInitialize(final Particle pParticle) {
		final ArrayList<IParticleModifier> particleModifiers = this.mParticleModifiers;
		for(int i = particleModifiers.size() - 1; i >= 0; i--) {
			particleModifiers.get(i).onInitializeParticle(pParticle);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
