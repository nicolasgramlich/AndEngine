package org.anddev.andengine.util;

import org.anddev.andengine.util.pool.GenericPool;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:07:53 - 23.02.2011
 */
public class TransformationPool {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private static final GenericPool<Transformation> POOL = new GenericPool<Transformation>() {
		@Override
		protected Transformation onAllocatePoolItem() {
			return new Transformation();
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	private TransformationPool() {}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	public static Transformation obtain() {
		return POOL.obtainPoolItem();
	}
	
	public static void recycle(final Transformation pTransformation) {
		pTransformation.setToIdentity();
		POOL.recyclePoolItem(pTransformation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}