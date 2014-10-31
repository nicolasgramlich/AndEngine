package org.andengine.extension.physics.box2d;

import java.util.Iterator;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.runnable.RunnableHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.DestructionListener;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:18:19 - 15.07.2010
 */
public class PhysicsWorld implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	static {
		System.loadLibrary( "andenginephysicsbox2dextension" );
	}

	public static final int VELOCITY_ITERATIONS_DEFAULT = 8;
	public static final int POSITION_ITERATIONS_DEFAULT = 8;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final PhysicsConnectorManager mPhysicsConnectorManager = new PhysicsConnectorManager();
	protected final RunnableHandler mRunnableHandler = new RunnableHandler();
	protected final World mWorld;

	protected int mVelocityIterations = VELOCITY_ITERATIONS_DEFAULT;
	protected int mPositionIterations = POSITION_ITERATIONS_DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PhysicsWorld(final Vector2 pGravity, final boolean pAllowSleep) {
		this(pGravity, pAllowSleep, VELOCITY_ITERATIONS_DEFAULT, POSITION_ITERATIONS_DEFAULT);
	}

	public PhysicsWorld(final Vector2 pGravity, final boolean pAllowSleep, final int pVelocityIterations, final int pPositionIterations) {
		this.mWorld = new World(pGravity, pAllowSleep);
		this.mVelocityIterations = pVelocityIterations;
		this.mPositionIterations = pPositionIterations;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	//	public World getWorld() {
	//		return this.mWorld;
	//	}

	public int getPositionIterations() {
		return this.mPositionIterations;
	}

	public void setPositionIterations(final int pPositionIterations) {
		this.mPositionIterations = pPositionIterations;
	}

	public int getVelocityIterations() {
		return this.mVelocityIterations;
	}

	public void setVelocityIterations(final int pVelocityIterations) {
		this.mVelocityIterations = pVelocityIterations;
	}

	public PhysicsConnectorManager getPhysicsConnectorManager() {
		return this.mPhysicsConnectorManager;
	}

	public void clearPhysicsConnectors() {
		this.mPhysicsConnectorManager.clear();
	}

	public void registerPhysicsConnector(final PhysicsConnector pPhysicsConnector) {
		this.mPhysicsConnectorManager.add(pPhysicsConnector);
	}

	public void unregisterPhysicsConnector(final PhysicsConnector pPhysicsConnector) {
		this.mPhysicsConnectorManager.remove(pPhysicsConnector);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mRunnableHandler.onUpdate(pSecondsElapsed);
		this.mWorld.step(pSecondsElapsed, this.mVelocityIterations, this.mPositionIterations);
		this.mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	}

	@Override
	public void reset() {
		// TODO Reset all native physics objects !?!??!
		this.mPhysicsConnectorManager.reset();
		this.mRunnableHandler.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void postRunnable(final Runnable pRunnable) {
		this.mRunnableHandler.postRunnable(pRunnable);
	}

	public void clearForces() {
		this.mWorld.clearForces();
	}

	public Body createBody(final BodyDef pDef) {
		return this.mWorld.createBody(pDef);
	}

	public Joint createJoint(final JointDef pDef) {
		return this.mWorld.createJoint(pDef);
	}

	public void destroyBody(final Body pBody) {
		this.mWorld.destroyBody(pBody);
	}

	public void destroyJoint(final Joint pJoint) {
		this.mWorld.destroyJoint(pJoint);
	}

	public void dispose() {
		this.mWorld.dispose();
	}

	public boolean getAutoClearForces() {
		return this.mWorld.getAutoClearForces();
	}

	public Iterator<Body> getBodies() {
		return this.mWorld.getBodies();
	}

	public int getBodyCount() {
		return this.mWorld.getBodyCount();
	}

	public int getContactCount() {
		return this.mWorld.getContactCount();
	}

	public List<Contact> getContactList() {
		return this.mWorld.getContactList();
	}

	public Vector2 getGravity() {
		return this.mWorld.getGravity();
	}

	public Iterator<Joint> getJoints() {
		return this.mWorld.getJoints();
	}

	public int getJointCount() {
		return this.mWorld.getJointCount();
	}

	public int getProxyCount() {
		return this.mWorld.getProxyCount();
	}

	public boolean isLocked() {
		return this.mWorld.isLocked();
	}

	public void QueryAABB(final QueryCallback pCallback, final float pLowerX, final float pLowerY, final float pUpperX, final float pUpperY) {
		this.mWorld.QueryAABB(pCallback, pLowerX, pLowerY, pUpperX, pUpperY);
	}

	public void setAutoClearForces(final boolean pFlag) {
		this.mWorld.setAutoClearForces(pFlag);
	}

	public void setContactFilter(final ContactFilter pFilter) {
		this.mWorld.setContactFilter(pFilter);
	}

	public void setContactListener(final ContactListener pListener) {
		this.mWorld.setContactListener(pListener);
	}

	public void setContinuousPhysics(final boolean pFlag) {
		this.mWorld.setContinuousPhysics(pFlag);
	}

	public void setDestructionListener(final DestructionListener pListener) {
		this.mWorld.setDestructionListener(pListener);
	}

	public void setGravity(final Vector2 pGravity) {
		this.mWorld.setGravity(pGravity);
	}

	public void setWarmStarting(final boolean pFlag) {
		this.mWorld.setWarmStarting(pFlag);
	}

	public void rayCast(final RayCastCallback pRayCastCallback, final Vector2 pPoint1, final Vector2 pPoint2) {
		this.mWorld.rayCast(pRayCastCallback, pPoint1, pPoint2);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
