package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:22:26 - 29.06.2010
 */
public class ColorParticleModifier<T extends IEntity> extends BaseTripleValueSpanParticleModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorParticleModifier(final float pFromTime, final float pToTime, final Color pFromColor, final Color pToColor) {
		this(pFromTime, pToTime, pFromColor.getRed(), pToColor.getRed(), pFromColor.getGreen(), pToColor.getGreen(), pFromColor.getBlue(), pToColor.getBlue());
	}

	public ColorParticleModifier(final float pFromTime, final float pToTime, final Color pFromColor, final Color pToColor, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromColor.getRed(), pToColor.getRed(), pFromColor.getGreen(), pToColor.getGreen(), pFromColor.getBlue(), pToColor.getBlue(), pEaseFunction);
	}

	public ColorParticleModifier(final float pFromTime, final float pToTime, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue) {
		this(pFromTime, pToTime, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, EaseLinear.getInstance());
	}

	public ColorParticleModifier(final float pFromTime, final float pToTime, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, pEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final Particle<T> pParticle, final float pRed, final float pGreen, final float pBlue) {
		pParticle.getEntity().setColor(pRed, pGreen, pBlue);
	}

	@Override
	protected void onSetValues(final Particle<T> pParticle, final float pPercentageDone, final float pRed, final float pGreen, final float pBlue) {
		pParticle.getEntity().setColor(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
