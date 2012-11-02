package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.color.Color;


/**
 * An {@link IParticleInitializer} that initialize each {@link Particle} with
 * a specific color or a random color within a specific range. It uses the
 * {@link IEntity#setColor(float, float, float)} method to set the color of
 * a particle.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:17:42 - 29.06.2010
 */
public class ColorParticleInitializer<T extends IEntity> extends BaseTripleValueParticleInitializer<T> {
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
	 * Create a new {@code ColorParticleInitializer} that gives each particle a
	 * specific color.
	 * 
	 * @param pColor The color for each new particle.
	 */
	public ColorParticleInitializer(final Color pColor) {
		super(pColor.getRed(), pColor.getRed(), pColor.getGreen(), pColor.getGreen(), pColor.getBlue(), pColor.getBlue());
	}
	
	/**
	 * Create a new {@code ColorParticleInitializer} that gives each particle a
	 * specific color.
	 * 
	 * @param pRed The red value (0.0 to 1.0) of the color.
	 * @param pGreen The green value (0.0 to 1.0) of the color.
	 * @param pBlue The blue value (0.0 to 1.0) of the color.
	 */
	public ColorParticleInitializer(final float pRed, final float pGreen, final float pBlue) {
		super(pRed, pRed, pGreen, pGreen, pBlue, pBlue);
	}

	/**
	 * Create a new {@code ColorParticleInitializer} that gives each particle a
	 * random color. The color will be between the two given colors, meaning each
	 * color component of the random color will lie inside the range of the components
	 * of the two given colors.
	 * 
	 * @param pMinColor The color, that should be used as a minimum value.
	 * @param pMaxColor The color, that should be used as a maximum value.
	 */
	public ColorParticleInitializer(final Color pMinColor, final Color pMaxColor) {
		super(pMinColor.getRed(), pMaxColor.getRed(), pMinColor.getGreen(), pMaxColor.getGreen(), pMinColor.getBlue(), pMaxColor.getBlue());
	}
	
	/**
	 * Create a new {@code ColorParticleInitializer} that gives each particle a
	 * random color.
	 * 
	 * @param pMinRed The minimum red value of the random color.
	 * @param pMaxRed The maximum red value of the random color.
	 * @param pMinGreen The minimum green value of the random color.
	 * @param pMaxGreen The maximum green value of the random color.
	 * @param pMinBlue The minium blue value of the random color.
	 * @param pMaxBlue The maximum blue value of the random color.
	 */
	public ColorParticleInitializer(final float pMinRed, final float pMaxRed, final float pMinGreen, final float pMaxGreen, final float pMinBlue, final float pMaxBlue) {
		super(pMinRed, pMaxRed, pMinGreen, pMaxGreen, pMinBlue, pMaxBlue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Will be called by the engine to set the color for a new particle.
	 */
	@Override
	protected void onInitializeParticle(final Particle<T> pParticle, final float pRed, final float pGreen, final float pBlue) {
		pParticle.getEntity().setColor(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
