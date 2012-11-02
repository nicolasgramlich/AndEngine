package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.math.MathUtils;

/**
 * An abstract {@link IParticleInitializer} that can be subclassed for an initializer,
 * that need only three {@code float} values to be set. This abstract class stores
 * the minimum and maximum value and pass a random value to the subclass. The subclass
 * only needs to apply the value to the particle.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:58:29 - 04.05.2010
 */
public abstract class BaseTripleValueParticleInitializer<T extends IEntity> extends BaseDoubleValueParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mMinValueC;
	protected float mMaxValueC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueParticleInitializer(final float pMinValueA, final float pMaxValueA, final float pMinValueB, final float pMaxValueB, final float pMinValueC, final float pMaxValueC) {
		super(pMinValueA, pMaxValueA, pMinValueB, pMaxValueB);
		this.mMinValueC = pMinValueC;
		this.mMaxValueC = pMaxValueC;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * The implementation of this method gets a {@link Particle} and the values.
	 * Use this values to modify the object.
	 * 
	 * @param pParticle The particle to modify.
	 * @param pValueA The first value to set.
	 * @param pValueB The second value to set.
	 * @param pValueC The third value to set.
	 */
	protected abstract void onInitializeParticle(final Particle<T> pParticle, final float pValueA, final float pValueB, final float pValueC);

	@Override
	protected final void onInitializeParticle(final Particle<T> pParticle, final float pValueA, final float pValueB) {
		this.onInitializeParticle(pParticle, pValueA, pValueB, this.getRandomValueC());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected float getRandomValueC() {
		if(this.mMinValueC == this.mMaxValueC) {
			return this.mMaxValueC;
		} else {
			return MathUtils.random(this.mMinValueC, this.mMaxValueC);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
