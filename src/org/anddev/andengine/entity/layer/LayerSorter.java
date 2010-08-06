package org.anddev.andengine.entity.layer;

import java.util.Comparator;

import org.anddev.andengine.util.sort.InsertionSorter;

public class LayerSorter extends InsertionSorter<ILayer> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Comparator<ILayer> mLayerComparator = new Comparator<ILayer>() {
		@Override
		public int compare(ILayer pLayerA, ILayer pLayerB) {
			return pLayerA.getZIndex() - pLayerB.getZIndex();
		}
	};

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

	public void sort(final ILayer[] pLayers) {
		this.sort(pLayers, this.mLayerComparator);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}