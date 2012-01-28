package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:58:29 - 04.05.2010
 */
public abstract class BaseDoubleValueParticleInitializer<T extends IEntity> extends BaseSingleValueParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mMinValueB;
	protected float mMaxValueB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDoubleValueParticleInitializer(final float pMinValueA, final float pMaxValueA, final float pMinValueB, final float pMaxValueB) {
		super(pMinValueA, pMaxValueA);
		this.mMinValueB = pMinValueB;
		this.mMaxValueB = pMaxValueB;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInitializeParticle(final Particle<T> pParticle, final float pValueA, final float pValueB);

	@Override
	protected final void onInitializeParticle(final Particle<T> pParticle, final float pValueA) {
		this.onInitializeParticle(pParticle, pValueA, this.getRandomValueB());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected float getRandomValueB() {
		if(this.mMinValueB == this.mMaxValueB) {
			return this.mMaxValueB;
		} else {
			return MathUtils.random(this.mMinValueB, this.mMaxValueB);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
