package org.andengine.engine.handler.runnable;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:24:39 - 18.06.2010
 */
public class RunnableHandler implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<Runnable> mRunnables = new ArrayList<Runnable>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public synchronized void onUpdate(final float pSecondsElapsed) {
		final ArrayList<Runnable> runnables = this.mRunnables;
		final int runnableCount = runnables.size();
		for(int i = runnableCount - 1; i >= 0; i--) {
			runnables.remove(i).run();
		}
	}

	@Override
	public synchronized void reset() {
		this.mRunnables.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized void postRunnable(final Runnable pRunnable) {
		this.mRunnables.add(pRunnable);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
