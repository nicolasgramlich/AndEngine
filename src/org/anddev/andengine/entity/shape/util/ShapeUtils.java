package org.anddev.andengine.entity.shape.util;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.MathUtils;

import android.util.FloatMath;

/**
 * @author Nicolas Gramlich
 * @since 01:09:43 - 06.10.2010
 */
public class ShapeUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Not tested for now!
	 */
	@Deprecated
	public void setVelocityRespectingRotation(final IShape pShape, final float pVelocityX, final float pVelocityY) {
		final float rotation = pShape.getRotation();
		final float rotationRad = MathUtils.degToRad(rotation);
		
		final float sin = FloatMath.sin(rotationRad);
		final float cos = FloatMath.cos(rotationRad);

		final float velocityX = sin * -pVelocityY + cos * pVelocityX;
		final float velocityY = cos * pVelocityY + sin * pVelocityX;
		
		pShape.setVelocity(velocityX, velocityY);
	}

	/**
	 * Not tested for now!
	 */
	@Deprecated
	public void accelerateRespectingRotation(final IShape pShape, final float pAccelerationX, final float pAccelerationY) {
		final float rotation = pShape.getRotation();
		final float rotationRad = MathUtils.degToRad(rotation);
		
		final float sin = FloatMath.sin(rotationRad);
		final float cos = FloatMath.cos(rotationRad);
		
		final float accelerationX = sin * -pAccelerationY + cos * pAccelerationX;
		final float accelerationY = cos * pAccelerationY + sin * pAccelerationX;
		
		pShape.setAcceleration(accelerationX, accelerationY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
