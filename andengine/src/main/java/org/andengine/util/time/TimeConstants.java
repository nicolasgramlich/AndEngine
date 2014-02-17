package org.andengine.util.time;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:49:25 - 26.07.2010
 */
public interface TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int MONTHS_PER_YEAR = 12;

	public static final int DAYS_PER_WEEK = 7;

	public static final int DAYS_PER_MONTH = 30;

	public static final int HOURS_PER_DAY = 24;

	public static final int MINUTES_PER_HOUR = 60;

	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int MICROSECONDS_PER_SECOND = 1000 * 1000;
	public static final long NANOSECONDS_PER_SECOND = 1000 * 1000 * 1000;

	public static final long MICROSECONDS_PER_MILLISECOND = MICROSECONDS_PER_SECOND / MILLISECONDS_PER_SECOND;

	public static final long NANOSECONDS_PER_MICROSECOND = NANOSECONDS_PER_SECOND / MICROSECONDS_PER_SECOND;
	public static final long NANOSECONDS_PER_MILLISECOND = NANOSECONDS_PER_SECOND / MILLISECONDS_PER_SECOND;

	public static final float SECONDS_PER_NANOSECOND = 1f / NANOSECONDS_PER_SECOND;
	public static final float MICROSECONDS_PER_NANOSECOND = 1f / NANOSECONDS_PER_MICROSECOND;
	public static final float MILLISECONDS_PER_NANOSECOND = 1f / NANOSECONDS_PER_MILLISECOND;

	public static final float SECONDS_PER_MICROSECOND = 1f / MICROSECONDS_PER_SECOND;
	public static final float MILLISECONDS_PER_MICROSECOND = 1f / MICROSECONDS_PER_MILLISECOND;

	public static final float SECONDS_PER_MILLISECOND = 1f / MILLISECONDS_PER_SECOND;

	public static final int SECONDS_PER_MINUTE = 60;
	public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
	public static final int SECONDS_PER_WEEK = SECONDS_PER_DAY * DAYS_PER_WEEK;
	public static final int SECONDS_PER_MONTH = SECONDS_PER_DAY * DAYS_PER_MONTH;
	public static final int SECONDS_PER_YEAR = SECONDS_PER_MONTH * MONTHS_PER_YEAR;

	// ===========================================================
	// Methods
	// ===========================================================
}
