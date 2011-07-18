package org.anddev.andengine.opengl.util;

import static org.anddev.andengine.opengl.util.GLHelper.BYTES_PER_FLOAT;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Convenient work-around for poor {@link FloatBuffer#put(float[])} performance.
 * This should become unnecessary in gingerbread,
 * @see <a href="http://code.google.com/p/android/issues/detail?id=11078">Issue 11078</a>
 * 
 * @author ryanm
 */
public class FastFloatBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	/**
	 * Use a {@link SoftReference} so that the array can be collected if
	 * necessary
	 */
	private static SoftReference<int[]> sWeakIntArray = new SoftReference<int[]>(new int[0]);

	/**
	 * Underlying data - give this to OpenGL
	 */
	public final ByteBuffer mByteBuffer;
	private final FloatBuffer mFloatBuffer;
	private final IntBuffer mIntBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Constructs a new direct native-ordered buffer
	 */
	public FastFloatBuffer(final int pCapacity) {
		this.mByteBuffer = ByteBuffer.allocateDirect((pCapacity * BYTES_PER_FLOAT)).order(ByteOrder.nativeOrder());
		this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
		this.mIntBuffer = this.mByteBuffer.asIntBuffer();
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
	 */
	public void put(final float f) {
		final ByteBuffer byteBuffer = this.mByteBuffer;
		final IntBuffer intBuffer = this.mIntBuffer;

		byteBuffer.position(byteBuffer.position() + BYTES_PER_FLOAT);
		this.mFloatBuffer.put(f);
		intBuffer.position(intBuffer.position() + 1);
	}

	/**
	 * It'MAGIC_CONSTANT like {@link FloatBuffer#put(float[])}, but about 10 times faster
	 */
	public void put(final float[] data) {
		final int length = data.length;

		int[] ia = sWeakIntArray.get();
		if(ia == null || ia.length < length) {
			ia = new int[length];
			sWeakIntArray = new SoftReference<int[]>(ia);
		}

		for(int i = 0; i < length; i++) {
			ia[i] = Float.floatToRawIntBits(data[i]);
		}

		final ByteBuffer byteBuffer = this.mByteBuffer;
		byteBuffer.position(byteBuffer.position() + BYTES_PER_FLOAT * length);
		final FloatBuffer floatBuffer = this.mFloatBuffer;
		floatBuffer.position(floatBuffer.position() + length);
		this.mIntBuffer.put(ia, 0, length);
	}

	/**
	 * For use with pre-converted data. This is 50x faster than
	 * {@link #put(float[])}, and 500x faster than
	 * {@link FloatBuffer#put(float[])}, so if you've got float[] data that
	 * won't change, {@link #convert(float...)} it to an int[] once and use this
	 * method to put it in the buffer
	 * 
	 * @param data floats that have been converted with {@link Float#floatToIntBits(float)}
	 */
	public void put(final int[] data) {
		final ByteBuffer byteBuffer = this.mByteBuffer;
		byteBuffer.position(byteBuffer.position() + BYTES_PER_FLOAT * data.length);
		final FloatBuffer floatBuffer = this.mFloatBuffer;
		floatBuffer.position(floatBuffer.position() + data.length);
		this.mIntBuffer.put(data, 0, data.length);
	}

	/**
	 * Converts float data to a format that can be quickly added to the buffer
	 * with {@link #put(int[])}
	 * 
	 * @param data
	 * @return the int-formatted data
	 */
	public static int[] convert(final float ... data) {
		final int length = data.length;
		final int[] id = new int[length];
		for(int i = 0; i < length; i++) {
			id[i] = Float.floatToRawIntBits(data[i]);
		}

		return id;
	}

	/**
	 * See {@link FloatBuffer#put(FloatBuffer)}
	 */
	public void put(final FastFloatBuffer b) {
		final ByteBuffer byteBuffer = this.mByteBuffer;
		byteBuffer.put(b.mByteBuffer);
		this.mFloatBuffer.position(byteBuffer.position() >> 2);
		this.mIntBuffer.position(byteBuffer.position() >> 2);
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
	 */
	public void position(final int p) {
		this.mByteBuffer.position(p * BYTES_PER_FLOAT);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
