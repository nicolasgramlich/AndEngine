package org.andengine.entity;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:49:11 - 01.05.2012
 */
public class TagEntityMatcher implements IEntityMatcher {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mTag;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TagEntityMatcher(final int pTag) {
		this.mTag = pTag;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getTag() {
		return this.mTag;
	}

	public void setTag(final int pTag) {
		this.mTag = pTag;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean matches(final IEntity pEntity) {
		return this.mTag == pEntity.getTag();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
