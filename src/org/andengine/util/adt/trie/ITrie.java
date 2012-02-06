package org.andengine.util.adt.trie;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:18:44 - 30.01.2012
 */
public interface ITrie {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void add(final CharSequence pCharSequence);
	public void add(final CharSequence pCharSequence, final int pStart, final int pEnd);
	public boolean contains(final CharSequence pCharSequence);
	public boolean contains(final CharSequence pCharSequence, final int pStart, final int pEnd);
	/* TODO public void clear(); */
	/* TODO public boolean remove(final CharSequence pCharSequence); */
	/* TODO public boolean remove(final CharSequence pCharSequence, final int pStart, final int pEnd); */
}
