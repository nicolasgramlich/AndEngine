package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.color.Color;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:17:42 - 29.06.2010
 */
public class ColorInitializer<T extends IEntity> extends BaseTripleValueInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorInitializer(final Color pColor) {
		super(pColor.getRed(), pColor.getRed(), pColor.getGreen(), pColor.getGreen(), pColor.getBlue(), pColor.getBlue());
	}
	
	public ColorInitializer(final float pRed, final float pGreen, final float pBlue) {
		super(pRed, pRed, pGreen, pGreen, pBlue, pBlue);
	}

	public ColorInitializer(final Color pMinColor, final Color pMaxColor) {
		super(pMinColor.getRed(), pMaxColor.getRed(), pMinColor.getGreen(), pMaxColor.getGreen(), pMinColor.getBlue(), pMaxColor.getBlue());
	}
	
	public ColorInitializer(final float pMinRed, final float pMaxRed, final float pMinGreen, final float pMaxGreen, final float pMinBlue, final float pMaxBlue) {
		super(pMinRed, pMaxRed, pMinGreen, pMaxGreen, pMinBlue, pMaxBlue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

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
