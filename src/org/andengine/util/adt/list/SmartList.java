package org.andengine.util.adt.list;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.IMatcher;
import org.andengine.util.call.ParameterCallable;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:20:08 - 27.12.2010
 */
public class SmartList<T> extends ArrayList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 8655669528273139819L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SmartList() {

	}

	public SmartList(final int pCapacity) {
		super(pCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void addFirst(final T pItem) {
		this.add(0, pItem);
	}

	public void addLast(final T pItem) {
		this.add(this.size(), pItem);
	}

	public T getFirst() throws IndexOutOfBoundsException {
		return this.get(0);
	}

	public T getLast() throws IndexOutOfBoundsException {
		return this.get(this.size() - 1);
	}

	public T get(final IMatcher<T> pMatcher) {
		final int size = this.size();
		for(int i = 0; i < size; i++) {
			final T item = this.get(i);
			if(pMatcher.matches(item)) {
				return item;
			}
		}
		return null;
	}

	public T removeFirst() throws IndexOutOfBoundsException {
		return this.remove(0);
	}

	public T removeLast() throws IndexOutOfBoundsException {
		return this.remove(this.size() - 1);
	}

	/**
	 * @param pItem the item to remove.
	 * @param pParameterCallable to be called with the removed item, if it was removed.
	 */
	public boolean remove(final T pItem, final ParameterCallable<T> pParameterCallable) {
		final boolean removed = this.remove(pItem);
		if(removed) {
			pParameterCallable.call(pItem);
		}
		return removed;
	}

	public T remove(final IMatcher<T> pMatcher) {
		for(int i = 0; i < this.size(); i++) {
			if(pMatcher.matches(this.get(i))) {
				return this.remove(i);
			}
		}
		return null;
	}

	public T remove(final IMatcher<T> pMatcher, final ParameterCallable<T> pParameterCallable) {
		for(int i = this.size() - 1; i >= 0; i--) {
			if(pMatcher.matches(this.get(i))) {
				final T removed = this.remove(i);
				pParameterCallable.call(removed);
				return removed;
			}
		}
		return null;
	}

	public boolean removeAll(final IMatcher<T> pMatcher) {
		boolean result = false;
		for(int i = this.size() - 1; i >= 0; i--) {
			if(pMatcher.matches(this.get(i))) {
				this.remove(i);
				result = true;
			}
		}
		return result;
	}

	/**
	 * @param pMatcher to find the items.
	 * @param pParameterCallable to be called with each matched item after it was removed.
	 */
	public boolean removeAll(final IMatcher<T> pMatcher, final ParameterCallable<T> pParameterCallable) {
		boolean result = false;
		for(int i = this.size() - 1; i >= 0; i--) {
			if(pMatcher.matches(this.get(i))) {
				final T removed = this.remove(i);
				pParameterCallable.call(removed);
				result = true;
			}
		}
		return result;
	}

	public void clear(final ParameterCallable<T> pParameterCallable) {
		for(int i = this.size() - 1; i >= 0; i--) {
			final T removed = this.remove(i);
			pParameterCallable.call(removed);
		}
	}

	public int indexOf(final IMatcher<T> pMatcher) {
		final int size = this.size();
		for(int i = 0; i < size; i++) {
			final T item = this.get(i);
			if(pMatcher.matches(item)) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(final IMatcher<T> pMatcher) {
		for(int i = this.size() - 1; i >= 0; i--) {
			final T item = this.get(i);
			if(pMatcher.matches(item)) {
				return i;
			}
		}
		return -1;
	}

	public ArrayList<T> query(final IMatcher<T> pMatcher) {
		return this.query(pMatcher, new ArrayList<T>());
	}

	public <L extends List<T>> L query(final IMatcher<T> pMatcher, final L pResult) {
		final int size = this.size();
		for(int i = 0; i < size; i++) {
			final T item = this.get(i);
			if(pMatcher.matches(item)) {
				pResult.add(item);
			}
		}

		return pResult;
	}

	public <S extends T> ArrayList<S> queryForSubclass(final IMatcher<T> pMatcher) {
		return this.queryForSubclass(pMatcher, new ArrayList<S>());
	}

	@SuppressWarnings("unchecked")
	public <L extends List<S>, S extends T> L queryForSubclass(final IMatcher<T> pMatcher, final L pResult) {
		final int size = this.size();
		for(int i = 0; i < size; i++) {
			final T item = this.get(i);
			if(pMatcher.matches(item)) {
				pResult.add((S)item);
			}
		}

		return pResult;
	}

	public void call(final ParameterCallable<T> pParameterCallable) {
		for(int i = this.size() - 1; i >= 0; i--) {
			final T item = this.get(i);
			pParameterCallable.call(item);
		}
	}

	public void call(final IMatcher<T> pMatcher, final ParameterCallable<T> pParameterCallable) {
		for(int i = this.size() - 1; i >= 0; i--) {
			final T item = this.get(i);
			if(pMatcher.matches(item)) {
				pParameterCallable.call(item);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
