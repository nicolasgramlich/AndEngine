package org.anddev.andengine.entity.particle.modifier;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.entity.particle.Particle;

/**
 * <p>An {@link ExpireModifier} that holds other {@link BaseSingleValueSpanModifier}s, overriding their
 * <code>toTime</code>s to match the <code>lifeTime</code> of the particle based on the value from the
 * ExpireModifier.</p>
 * 
 * <p>(c) 2011 Nicolas Gramlich<br>
 * (c) 2011 Zynga Inc.</p>
 * 
 * @author Scott Kennedy
 * @since 21:01:00 - 08.08.2011
 */
public class ContainerExpireModifier extends ExpireModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final List<BaseSingleValueSpanModifier> mModifiers = new ArrayList<BaseSingleValueSpanModifier>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public ContainerExpireModifier(final float pLifeTime) {
		super(pLifeTime);
	}

	public ContainerExpireModifier(final float pMinLifeTime, final float pMaxLifeTime) {
		super(pMinLifeTime, pMaxLifeTime);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
    public void onUpdateParticle(final Particle pParticle) {
	    // Apply all the contained modifiers
	    for (final BaseSingleValueSpanModifier modifier : mModifiers) {
	        modifier.onUpdateParticle(pParticle, pParticle.getDeathTime());
	    }
    }

	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * <p>Adds the specified modifier to the contained modifier list.</p>
	 * 
	 * <p>The <code>toTime</code> in the modifier will be ignored, and replaced with the actual
	 * lifetime of the {@link Particle}.</p>
	 * 
	 * @param pModifier The modifier to add
	 */
	public void addParticleModifier(final BaseSingleValueSpanModifier pModifier) {
	    mModifiers.add(pModifier);
	}
	
	/**
	 * <p>Removes the specified modifier from the contained modifier list.</p>
	 * @param pModifier The modifier to remove
	 * @return true if the list was modified by this operation, false otherwise.
	 */
	public boolean removeParticleModifier(final BaseSingleValueSpanModifier pModifier) {
	    return mModifiers.remove(pModifier);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
