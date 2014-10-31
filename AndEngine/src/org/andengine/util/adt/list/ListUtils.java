package org.andengine.util.adt.list;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.adt.queue.IQueue;
import org.andengine.util.adt.queue.concurrent.SynchronizedQueue;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:43:39 - 11.03.2010
 */
public final class ListUtils {
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

	// ===========================================================
	// Methods
	// ===========================================================

	public static final <T> IQueue<T> synchronizedQueue(final IQueue<T> pQueue) {
		return new SynchronizedQueue<T>(pQueue);
	}

	public static final <T> T random(final List<T> pList) {
		return pList.get(MathUtils.random(0, pList.size() - 1));
	}

	public static final <T> ArrayList<? extends T> toList(final T pItem) {
		final ArrayList<T> out = new ArrayList<T>();
		out.add(pItem);
		return out;
	}

	public static final <T> ArrayList<? extends T> toList(final T ... pItems) {
		final ArrayList<T> out = new ArrayList<T>();
		final int itemCount = pItems.length;
		for(int i = 0; i < itemCount; i++) {
			out.add(pItems[i]);
		}
		return out;
	}

	public static <T> void swap(final List<T> pItems, final int pIndexA, final int pIndexB) {
		final T tmp = pItems.get(pIndexA);
		pItems.set(pIndexA, pItems.get(pIndexB));
		pItems.set(pIndexB, tmp);
	}

	public static <T> void swap(final IList<T> pItems, final int pIndexA, final int pIndexB) {
		final T tmp = pItems.get(pIndexA);
		pItems.set(pIndexA, pItems.get(pIndexB));
		pItems.set(pIndexB, tmp);
	}

	public static final int encodeInsertionIndex(final int pIndex) {
		return (-pIndex) - 1;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
