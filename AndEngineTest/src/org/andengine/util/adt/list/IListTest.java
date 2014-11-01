package org.andengine.util.adt.list;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.util.math.MathUtils;

/**
 * @author Nicolas Gramlich
 * @since 15:00:30 - 24.01.2012
 */
public abstract class IListTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CORRECTNESS_ITERATIONS = 100;
	private static final int CORRECTNESS_ITERATIONS_CLEAR = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private IList<String> mList;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void setUp() throws Exception {
		this.mList = this.newList(1);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract IList<String> newList(final int pInitialCapacity);

	// ===========================================================
	// Methods
	// ===========================================================

	public void testIsEmpty() {
		Assert.assertEquals(true, this.mList.isEmpty());
	}

	public void testIsNotEmpty() {
		this.mList.add("A");
		Assert.assertEquals(false, this.mList.isEmpty());
	}

	public void testAdd() {
		this.mList.add("A");
		Assert.assertEquals(1, this.mList.size());
	}

	public void testAddMany() {
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");
		this.mList.add("E");

		Assert.assertEquals(5, this.mList.size());
	}

	public void testAddAndRemove() {
		this.mList.add("A");

		Assert.assertEquals(1, this.mList.size());

		Assert.assertEquals("A", this.mList.remove(0));

		Assert.assertEquals(0, this.mList.size());
	}

	public void testAddAndRemoveMany() {
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");
		this.mList.add("E");

		Assert.assertEquals(5, this.mList.size());

		Assert.assertEquals("A", this.mList.remove(0));
		Assert.assertEquals("B", this.mList.remove(0));
		Assert.assertEquals("C", this.mList.remove(0));
		Assert.assertEquals("D", this.mList.remove(0));
		Assert.assertEquals("E", this.mList.remove(0));

		Assert.assertEquals(0, this.mList.size());
	}

	public void testAddAndRemoveAndAddMany() {
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");
		this.mList.add("E");

		Assert.assertEquals(5, this.mList.size());

		Assert.assertEquals("A", this.mList.remove(0));
		Assert.assertEquals("B", this.mList.remove(0));
		Assert.assertEquals("C", this.mList.remove(0));
		Assert.assertEquals("D", this.mList.remove(0));
		Assert.assertEquals("E", this.mList.remove(0));

		Assert.assertEquals(0, this.mList.size());

		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");
		this.mList.add("E");
	}

	public void testAutoShift() {
		this.mList.add("A");
		this.mList.add("B");

		Assert.assertEquals(2, this.mList.size());

		Assert.assertEquals("A", this.mList.remove(0));
		Assert.assertEquals("B", this.mList.remove(0));

		Assert.assertEquals(0, this.mList.size());

		this.mList.add("A");
	}

	public void testShiftNulling1() {
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");

		this.mList.remove(0);
		this.mList.remove(0);
		this.mList.remove(0);

		this.mList.add("A");
		this.mList.add("B");
	}

	public void testShiftNulling2() {
		this.mList = this.newList(3);

		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");

		this.mList.remove(0);
		this.mList.remove(0);
		this.mList.remove(0);

		this.mList.add("A");
		this.mList.add("B");
	}

	public void testShiftNulling3() {
		this.mList = this.newList(4);

		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");

		this.mList.remove(0);

		this.mList.add("D");
	}

	public void testClear() {
		this.mList = this.newList(1);

		this.mList.add("A");

		this.mList.clear();

		Assert.assertEquals(0, this.mList.size());
	}

	public void testClearMany() {
		this.mList = this.newList(3);

		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");

		this.mList.clear();

		Assert.assertEquals(0, this.mList.size());
	}

	public void testClearComplex() {
		this.mList = this.newList(3);

		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");

		this.mList.remove(0);

		this.mList.add("D");

		this.mList.clear();

		Assert.assertEquals(0, this.mList.size());
	}

	public void testGetExactSIze() {
		this.mList = this.newList(3);
		
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");

		Assert.assertEquals("A", this.mList.get(0));
		Assert.assertEquals("B", this.mList.get(1));
		Assert.assertEquals("C", this.mList.get(2));
	}

	public void testGet() {
		this.mList = this.newList(2);
		
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		
		Assert.assertEquals("A", this.mList.get(0));
		Assert.assertEquals("B", this.mList.get(1));
		Assert.assertEquals("C", this.mList.get(2));
	}

	public void testIndexOfExactSize() {
		this.mList = this.newList(2);
		
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		
		Assert.assertEquals(0, this.mList.indexOf("A"));
		Assert.assertEquals(1, this.mList.indexOf("B"));
		Assert.assertEquals(2, this.mList.indexOf("C"));
	}

	public void testIndexOfWithRemove() {
		this.mList = this.newList(2);
		
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");
		this.mList.add("E");

		this.mList.remove("B");

		this.mList.add("F");
		
		Assert.assertEquals(0, this.mList.indexOf("A"));
		Assert.assertEquals(1, this.mList.indexOf("C"));
		Assert.assertEquals(2, this.mList.indexOf("D"));
		Assert.assertEquals(3, this.mList.indexOf("E"));
		Assert.assertEquals(4, this.mList.indexOf("F"));
	}

	public void testIndexOfWithRemove2() {
		this.mList = this.newList(2);
		
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		this.mList.add("D");
		this.mList.add("E");
		
		this.mList.remove("C");
		
		this.mList.add("F");
		
		Assert.assertEquals(0, this.mList.indexOf("A"));
		Assert.assertEquals(1, this.mList.indexOf("B"));
		Assert.assertEquals(2, this.mList.indexOf("D"));
		Assert.assertEquals(3, this.mList.indexOf("E"));
		Assert.assertEquals(4, this.mList.indexOf("F"));
	}

	public void testIndexOfWithPoll() {
		this.mList = this.newList(2);
		
		this.mList.add("A");
		this.mList.add("B");
		this.mList.add("C");
		
		this.mList.remove(0);
		
		this.mList.add("D");
		
		Assert.assertEquals(0, this.mList.indexOf("B"));
		Assert.assertEquals(1, this.mList.indexOf("C"));
		Assert.assertEquals(2, this.mList.indexOf("D"));
	}

	public void testWeird() {
		final int[] values = new int[] { 0, 0, 499, 470, 421, 346, 37, 445, 752, 9, 297, 936, 910, 423, 725, 754, 240, 450, 512, 464, 952, 563, 170, 862, 870, 660, 956, 339, 768, 433, 378, 984, 154, 795, 805, 486, 162, 181, 729, 695, 284, 499, 400, 856, 151, 111, 561, 90, 287, 295, 901, 899, 185, 134, 513, 21, 998, 304, 85, 862, 569, 841, 577, 208, 481, 349, 869, 816, 960, 884, 664, 838, 586, 239, 0, 18, 838, 167, 925, 707, 212, 981, 764, 347, 67, 303, 602, 347, 864, 591, 865, 171 };

		final IList<String> list = newList(5);
		final ArrayList<String> arraylist = new ArrayList<String>(5);
		
		for(int i = 0; i < values.length; i++) {
			String valueOf = String.valueOf(values[i]);
			list.add(valueOf);
			arraylist.add(valueOf);
		}
		
		list.removeFirst();
		list.removeFirst();
		arraylist.remove(0);
		arraylist.remove(0);
		
		testIdentical(list, arraylist);
		
		list.remove(8);
		arraylist.remove(8);

		testIdentical(list, arraylist);
	}

	public void testCorrectness() throws Exception {
		MathUtils.RANDOM.setSeed(1234567890);
		final IList<String> list = newList(5);
		final ArrayList<String> arraylist = new ArrayList<String>(5);
		
		for(int i = 0; i < CORRECTNESS_ITERATIONS; i++) {
			final int random = MathUtils.random(0, 1000);
			final String randomString = String.valueOf(random);

			int caze = MathUtils.random(0, 20);
			switch(caze) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					list.add(randomString);
					arraylist.add(randomString);
					break;
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
					final int size = arraylist.size();
					if(random < size) {
						list.remove(random);
						arraylist.remove(random);
					}
					break;
				case 20:
					if(i % CORRECTNESS_ITERATIONS_CLEAR == 0) {
						arraylist.clear();
						list.clear();
					}
					break;
			}

			testIdentical(list, arraylist);
		}
	}

	private void testIdentical(final IList<String> list, final ArrayList<String> arraylist) {
		final int size = arraylist.size();
		Assert.assertEquals(size, list.size());
		for(int j = 0; j < size; j++) {
			Assert.assertEquals(arraylist.get(j), list.get(j));
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
