package org.andengine.util.adt.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:54:24 - 07.11.2010
 */
public class MultiKeyHashMap<K, V> extends HashMap<MultiKey<K>, V> {
	// ===========================================================
	// Constants
	// ==========================================================

	private static final long serialVersionUID = -6262447639526561122L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public V get(final K ... pKeys) {
		final int hashCode = MultiKey.hash(pKeys);

		final Iterator<Map.Entry<MultiKey<K>, V>> it = this.entrySet().iterator();
		while(it.hasNext()) {
			final Map.Entry<MultiKey<K>, V> entry = it.next();
			final MultiKey<K> entryKey = entry.getKey();
			if (entryKey.hashCode() == hashCode && this.isEqualKey(entryKey.getKeys(), pKeys)) {
				return entry.getValue();
			}
		}
		return null;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private boolean isEqualKey(final K[] pKeysA, final K[] pKeysB) {
		if (pKeysA.length != pKeysB.length) {
			return false;
		} else {
			for (int i = 0; i < pKeysA.length; i++) {
				final K keyA = pKeysA[i];
				final K keyB = pKeysB[i];
				if(keyA == null) {
					if(keyB != null) {
						return false;
					}
				} else {
					if(!keyA.equals(keyB)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
