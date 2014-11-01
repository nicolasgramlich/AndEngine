package org.andengine.util.adt.trie;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Nicolas Gramlich
 * @since 22:31:38 - 16.09.2010
 */
public class TrieTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Trie mTrie;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void setUp() throws Exception {
		this.mTrie = new Trie();
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

	public void testAddEmpty() {
		this.mTrie.add("");
	}

	public void testAdd() {
		this.mTrie.add("H");
	}

	public void testAddExisting() {
		this.mTrie.add("HELLO");
		this.mTrie.add("HELLO");
	}

	public void testAddSubSequenceExisting() {
		this.mTrie.add("HELL");
		this.mTrie.add("HELLO");
	}
	
	public void testAddSubSequenceOfExisting() {
		this.mTrie.add("HELLO");
		this.mTrie.add("HELL");
	}
	
	public void testContains() {
		this.mTrie.add("HELL");
		Assert.assertTrue(this.mTrie.contains("HELL"));
	}
	
	public void testContainsSubSequenceExisting() {
		this.mTrie.add("HELL");
		Assert.assertFalse(this.mTrie.contains("HELLO"));
	}
	
	public void testContainsSubSequenceOfExisting() {
		this.mTrie.add("HELLO");
		Assert.assertFalse(this.mTrie.contains("HELL"));
	}

	public void testContainsAddingSubSequenceOfExisting() {
		this.mTrie.add("HELLO");
		this.mTrie.add("HELL");
		Assert.assertTrue(this.mTrie.contains("HELL"));
	}
	
	public void testContainsAddingSubSequenceExisting() {
		this.mTrie.add("HELL");
		this.mTrie.add("HELLO");
		Assert.assertTrue(this.mTrie.contains("HELL"));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
