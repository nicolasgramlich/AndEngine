package org.helllabs.android.xmp;


public class Xmp {
	public native int init();
	public native int deinit();
	public native int testModule(String name);
	public native int loadModule(String name);
	public native int releaseModule();
	public native int startPlayer();
	public native int endPlayer();
	public native int playFrame();	
	public native int softmixer();
	public native short[] getBuffer(int size, short buffer[]);
	public native int nextOrd();
	public native int prevOrd();
	public native int setOrd(int n);
	public native int stopModule();
	public native int restartModule();
	public native int stopTimer();
	public native int restartTimer();
	public native int incGvol();
	public native int decGvol();
	public native int seek(long time);
	public native ModInfo getModInfo(String name);
	public native int time();
	public native int seek(int seconds);
	public native int getPlayTempo();
	public native int getPlayBpm();
	public native int getPlayPos();
	public native int getPlayPat();
	public native String getVersion();
	public native int getFormatCount();
	public native String[] getFormats();
	
	static {
		System.loadLibrary("xmp");
	}
}
