package org.anddev.andengine.util.data;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:59:55 - 14.07.2011
 */
public interface DataConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final int BITS_PER_BYTE = 8;

	public static final int BYTES_PER_BYTE = 1;
	public static final int BYTES_PER_SHORT = Short.SIZE / Byte.SIZE;
	public static final int BYTES_PER_INT = Integer.SIZE / Byte.SIZE;
	public static final int BYTES_PER_FLOAT = Float.SIZE / Byte.SIZE;
	public static final int BYTES_PER_LONG = Long.SIZE / Byte.SIZE;

	public static final int BYTES_PER_KILOBYTE = 1024;
	public static final int BYTES_PER_MEGABYTE = 1024 * DataConstants.BYTES_PER_KILOBYTE;

	public static final short UNSIGNED_BYTE_MAX_VALUE = (short)Byte.MAX_VALUE - (short)Byte.MIN_VALUE;
	public static final int UNSIGNED_SHORT_MAX_VALUE = (int)Short.MAX_VALUE - (int)Short.MIN_VALUE;
	public static final long UNSIGNED_INT_MAX_VALUE = (long)Integer.MAX_VALUE - (long)Integer.MIN_VALUE;

	// ===========================================================
	// Methods
	// ===========================================================
}
