package org.andengine.extension.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * A subclass of {@link PhysicsWorld} that tries to achieve a specific amount of steps per second.
 * When the time since the last step is bigger long the steplength, additional steps are executed.
 * 
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:39:42 - 25.07.2010
 */
public class FixedStepPhysicsWorld extends PhysicsWorld {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int STEPSPERSECOND_DEFAULT = 60;

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mTimeStep;
	private final int mMaximumStepsPerUpdate;
	private float mSecondsElapsedAccumulator;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedStepPhysicsWorld(final int pStepsPerSecond, final Vector2 pGravity, final boolean pAllowSleep) {
		this(pStepsPerSecond, Integer.MAX_VALUE, pGravity, pAllowSleep);
	}

	public FixedStepPhysicsWorld(final int pStepsPerSecond, final int pMaximumStepsPerUpdate, final Vector2 pGravity, final boolean pAllowSleep) {
		super(pGravity, pAllowSleep);
		this.mTimeStep = 1.0f / pStepsPerSecond;
		this.mMaximumStepsPerUpdate = pMaximumStepsPerUpdate;
	}

	public FixedStepPhysicsWorld(final int pStepsPerSecond, final Vector2 pGravity, final boolean pAllowSleep, final int pVelocityIterations, final int pPositionIterations) {
		this(pStepsPerSecond, Integer.MAX_VALUE, pGravity, pAllowSleep, pVelocityIterations, pPositionIterations);
	}

	public FixedStepPhysicsWorld(final int pStepsPerSecond, final int pMaximumStepsPerUpdate, final Vector2 pGravity, final boolean pAllowSleep, final int pVelocityIterations, final int pPositionIterations) {
		super(pGravity, pAllowSleep, pVelocityIterations, pPositionIterations);
		this.mTimeStep = 1.0f / pStepsPerSecond;
		this.mMaximumStepsPerUpdate = pMaximumStepsPerUpdate;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mRunnableHandler.onUpdate(pSecondsElapsed);
		this.mSecondsElapsedAccumulator += pSecondsElapsed;

		final int velocityIterations = this.mVelocityIterations;
		final int positionIterations = this.mPositionIterations;

		final World world = this.mWorld;
		final float stepLength = this.mTimeStep;
		
		int stepsAllowed = this.mMaximumStepsPerUpdate;
		
		while(this.mSecondsElapsedAccumulator >= stepLength && stepsAllowed > 0) {
			world.step(stepLength, velocityIterations, positionIterations);
			this.mSecondsElapsedAccumulator -= stepLength;
			stepsAllowed--;
		}
		
		this.mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
