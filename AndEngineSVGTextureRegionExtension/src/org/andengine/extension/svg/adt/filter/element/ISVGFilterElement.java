package org.andengine.extension.svg.adt.filter.element;

import android.graphics.Paint;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:54:15 - 26.05.2011
 */
public interface ISVGFilterElement {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void apply(final Paint pPaint);
}
