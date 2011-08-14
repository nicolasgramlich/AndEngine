package org.anddev.andengine.util.constants;


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

	// ===========================================================
	// Methods
	// ===========================================================
}
