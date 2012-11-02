package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * An {@link IParticleModifier} that scales each particle during its lifetime.
 * See the documentation of the {@link IParticleModifier} for usage hints.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @see IParticleModifier
 * 
 * @author Nicolas Gramlich
 * @since 20:37:27 - 04.05.2010
 */
public class ScaleParticleModifier<T extends IEntity> extends BaseDoubleValueSpanParticleModifier<T> {
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
	 * Creates a new {@code ScaleParticleModifier} that scales a particle during its
	 * lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the scale value will be done linear.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromScale Scale factor at {@code pFromTime}. (1.0 = 100% size)
	 * @param pToScale Scale factor at {@code pToTime}. (1.0 = 100% size)
	 */
	public ScaleParticleModifier(final float pFromTime, final float pToTime, final float pFromScale, final float pToScale) {
		this(pFromTime, pToTime, pFromScale, pToScale, EaseLinear.getInstance());
	}

	/**
	 * Creates a new {@code ScaleParticleModifier} that scales a particle during its
	 * lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the scale value is controlled by the specified {@link IEaseFunction}.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromScale Scale factor at {@code pFromTime}. (1.0 = 100% size)
	 * @param pToScale Scale factor at {@code pToTime}. (1.0 = 100% size)
	 * @param pEaseFunction The {@link IEaseFunction} to use for interpolation of scale value.
	 */
	public ScaleParticleModifier(final float pFromTime, final float pToTime, final float pFromScale, final float pToScale, final IEaseFunction pEaseFunction) {
		this(pFromTime, pToTime, pFromScale, pToScale, pFromScale, pToScale, pEaseFunction);
	}

	/**
	 * Creates a new {@code ScaleParticleModifier} that scales a particle during its
	 * lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the scale value will be done linear.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromScaleX X scale factor at {@code pFromTime}. (1.0 = 100% size)
	 * @param pToScaleX X scale factor at {@code pToTime}. (1.0 = 100% size)
	 * @param pFromScaleY Y scale factor at {@code pFromTime}. (1.0 = 100% size)
	 * @param pToScaleY Y scale factor at {@code pToTime}. (1.0 = 100% size)
	 */
	public ScaleParticleModifier(final float pFromTime, final float pToTime, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY) {
		this(pFromTime, pToTime, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, EaseLinear.getInstance());
	}

	/**
	 * Creates a new {@code ScaleParticleModifier} that scales a particle during its
	 * lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the scale value is controlled by the specified {@link IEaseFunction}.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromScaleX X scale factor at {@code pFromTime}. (1.0 = 100% size)
	 * @param pToScaleX X scale factor at {@code pToTime}. (1.0 = 100% size)
	 * @param pFromScaleY Y scale factor at {@code pFromTime}. (1.0 = 100% size)
	 * @param pToScaleY Y scale factor at {@code pToTime}. (1.0 = 100% size)
	 * @param pEaseFunction The {@link IEaseFunction} to use for interpolation of scale value.
	 */
	public ScaleParticleModifier(final float pFromTime, final float pToTime, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetInitialValues(final Particle<T> pParticle, final float pScaleX, final float pScaleY) {
		pParticle.getEntity().setScale(pScaleX, pScaleY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetValues(final Particle<T> pParticle, final float pPercentageDone, final float pScaleX, final float pScaleY) {
		pParticle.getEntity().setScale(pScaleX, pScaleY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
