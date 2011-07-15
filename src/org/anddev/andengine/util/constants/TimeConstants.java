package org.anddev.andengine.util.constants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:49:25 - 26.07.2010
 */
public interface TimeConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final int MONTHSPERYEAR = 12;

	public static final int DAYSPERWEEK = 7;

	public static final int DAYSPERMONTH = 30;

	public static final int HOURSPERDAY = 24;

	public static final int MINUTESPERHOUR = 60;

	public static final int MILLISECONDSPERSECOND = 1000;
	public static final int MICROSECONDSPERSECOND = 1000000;
	public static final long NANOSECONDSPERSECOND = 1000000000;

	public static final long MICROSECONDSPERMILLISECOND = MICROSECONDSPERSECOND / MILLISECONDSPERSECOND;

	public static final long NANOSECONDSPERMICROSECOND = NANOSECONDSPERSECOND / MICROSECONDSPERSECOND;
	public static final long NANOSECONDSPERMILLISECOND = NANOSECONDSPERSECOND / MILLISECONDSPERSECOND;

	public static final float SECONDSPERNANOSECOND = 1f / NANOSECONDSPERSECOND;
	public static final float SECONDSPERMICROSECOND = 1f / MICROSECONDSPERSECOND;
	public static final float SECONDSPERMILLISECOND = 1f / MILLISECONDSPERSECOND;
	public static final int SECONDSPERMINUTE = 60;
	public static final int SECONDSPERHOUR = SECONDSPERMINUTE * MINUTESPERHOUR;
	public static final int SECONDSPERDAY = SECONDSPERHOUR * HOURSPERDAY;
	public static final int SECONDSPERWEEK = SECONDSPERDAY * DAYSPERWEEK;
	public static final int SECONDSPERMONTH = SECONDSPERDAY * DAYSPERMONTH;
	public static final int SECONDSPERYEAR = SECONDSPERMONTH * MONTHSPERYEAR;
	// ===========================================================
	// Methods
	// ===========================================================
}
