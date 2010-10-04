package org.anddev.andengine.opengl.util;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Convenient work-around for poor {@link FloatBuffer#put(float[])} performance.
 * This should become unnecessary in gingerbread, @see <a href
 * ="http://code.google.com/p/android/issues/detail?id=11078">Issue 11078</a>
 * 
 * @author ryanm
 */
public class FastFloatBuffer {
	/**
	 * Underlying data - give this to OpenGL
	 */
	ByteBuffer mByteBuffer;

	private final FloatBuffer mFloatBuffer;

	private final IntBuffer mIntBuffer;

	/**
	 * Use a {@link SoftReference} so that the array can be collected if
	 * necessary
	 */
	private static SoftReference<int[]> intArray = new SoftReference<int[]>(new int[0]);

	/**
	 * Constructs a new direct native-ordered buffer
	 * 
	 * @param capacity
	 *            the number of floats
	 */
	public FastFloatBuffer(final int capacity) {
		this.mByteBuffer = ByteBuffer.allocateDirect((capacity * 4)).order(ByteOrder.nativeOrder());
		this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
		this.mIntBuffer = this.mByteBuffer.asIntBuffer();
	}

	/**
	 * See {@link FloatBuffer#flip()}
	 */
	public void flip() {
		this.mByteBuffer.flip();
		this.mFloatBuffer.flip();
		this.mIntBuffer.flip();
	}

	/**
	 * See {@link FloatBuffer#put(float)}
	 * 
	 * @param f
	 */
	public void put(final float f) {
		this.mByteBuffer.position(this.mByteBuffer.position() + 4);
		this.mFloatBuffer.put(f);
		this.mIntBuffer.position(this.mIntBuffer.position() + 1);
	}

	/**
	 * It's like {@link FloatBuffer#put(float[])}, but about 10 times faster
	 * 
	 * @param data
	 */
	public void put(final float[] data) {
		int[] ia = intArray.get();
		if(ia == null || ia.length < data.length) {
			ia = new int[data.length];
			intArray = new SoftReference<int[]>(ia);
		}

		for(int i = 0; i < data.length; i++) {
			ia[i] = Float.floatToRawIntBits(data[i]);
		}

		this.mByteBuffer.position(this.mByteBuffer.position() + 4 * data.length);
		this.mFloatBuffer.position(this.mFloatBuffer.position() + data.length);
		this.mIntBuffer.put(ia, 0, data.length);
	}

	/**
	 * For use with pre-converted data. This is 50x faster than
	 * {@link #put(float[])}, and 500x faster than
	 * {@link FloatBuffer#put(float[])}, so if you've got float[] data that
	 * won't change, {@link #convert(float...)} it to an int[] once and use this
	 * method to put it in the buffer
	 * 
	 * @param data
	 *            floats that have been converted with
	 *            {@link Float#floatToIntBits(float)}
	 */
	public void put(final int[] data) {
		this.mByteBuffer.position(this.mByteBuffer.position() + 4 * data.length);
		this.mFloatBuffer.position(this.mFloatBuffer.position() + data.length);
		this.mIntBuffer.put(data, 0, data.length);
	}

	/**
	 * Converts float data to a format that can be quickly added to the buffer
	 * with {@link #put(int[])}
	 * 
	 * @param data
	 * @return the int-formatted data
	 */
	public static int[] convert(final float... data) {
		final int[] id = new int[data.length];
		for(int i = 0; i < data.length; i++) {
			id[i] = Float.floatToRawIntBits(data[i]);
		}

		return id;
	}

	/**
	 * See {@link FloatBuffer#put(FloatBuffer)}
	 * 
	 * @param b
	 */
	public void put(final FastFloatBuffer b) {
		this.mByteBuffer.put(b.mByteBuffer);
		this.mFloatBuffer.position(this.mByteBuffer.position() >> 2);
		this.mIntBuffer.position(this.mByteBuffer.position() >> 2);
	}

	/**
	 * @return See {@link FloatBuffer#capacity()}
	 */
	public int capacity() {
		return this.mFloatBuffer.capacity();
	}

	/**
	 * @return See {@link FloatBuffer#position()}
	 */
	public int position() {
		return this.mFloatBuffer.position();
	}

	/**
	 * See {@link FloatBuffer#position(int)}
	 * 
	 * @param p
	 */
	public void position(final int p) {
		this.mByteBuffer.position(4 * p);
		this.mFloatBuffer.position(p);
		this.mIntBuffer.position(p);
	}

	/**
	 * @return See {@link FloatBuffer#slice()}
	 */
	public FloatBuffer slice() {
		return this.mFloatBuffer.slice();
	}

	/**
	 * @return See {@link FloatBuffer#remaining()}
	 */
	public int remaining() {
		return this.mFloatBuffer.remaining();
	}

	/**
	 * @return See {@link FloatBuffer#limit()}
	 */
	public int limit() {
		return this.mFloatBuffer.limit();
	}

	/**
	 * See {@link FloatBuffer#clear()}
	 */
	public void clear() {
		this.mByteBuffer.clear();
		this.mFloatBuffer.clear();
		this.mIntBuffer.clear();
	}
}
