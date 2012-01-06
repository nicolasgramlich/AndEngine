package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:03:29 - 19.11.2011
 */
public class ScaleInitializer<T extends IEntity> extends BaseSingleValueInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleInitializer(final float pScale) {
		super(pScale, pScale);
	}

	public ScaleInitializer(final float pMinScale, final float pMaxScale) {
		super(pMinScale, pMaxScale);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onInitializeParticle(final Particle<T> pParticle, final float pScale) {
		pParticle.getEntity().setScale(pScale);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
