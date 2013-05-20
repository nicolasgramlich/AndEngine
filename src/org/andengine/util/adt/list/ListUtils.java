package org.andengine.util.adt.list;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.IMatcher;
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

	private ListUtils() {

	}

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
		for (int i = 0; i < itemCount; i++) {
			out.add(pItems[i]);
		}
		return out;
	}

	public static final <T> ArrayList<T> filter(final List<T> pItems, final IMatcher<T> pMatcher) {
		final ArrayList<T> out = new ArrayList<T>();
		final int itemCount = pItems.size();
		for (int i = 0; i < itemCount; i++) {
			final T item = pItems.get(i);
			if (pMatcher.matches(item)) {
				out.add(item);
			}
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

	public static <T> String toString(final IList<T> pItems) {
		final int size = pItems.size();
		if (size == 0) {
			return "[]";
		} else {
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append('[');
			for (int i = 0; i < size; i++) {
				final T item = pItems.get(i);

				stringBuilder.append(item.toString());

				if (i < size - 1) {
					stringBuilder.append(", ");
				} else {
					break;
				}
			}
			stringBuilder.append(']');

			return stringBuilder.toString();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
