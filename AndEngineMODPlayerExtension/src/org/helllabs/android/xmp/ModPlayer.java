package org.helllabs.android.xmp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;


public class ModPlayer {
	private static ModPlayer instance = null;
	private Xmp xmp = new Xmp();
	private int minSize = AudioTrack.getMinBufferSize(44100,
			AudioFormat.CHANNEL_CONFIGURATION_STEREO,
			AudioFormat.ENCODING_PCM_16BIT);
	private AudioTrack audio = new AudioTrack(
			AudioManager.STREAM_MUSIC, 44100,
			AudioFormat.CHANNEL_CONFIGURATION_STEREO,
			AudioFormat.ENCODING_PCM_16BIT,
			minSize < 4096 ? 4096 : minSize,
			AudioTrack.MODE_STREAM);
	protected boolean paused;
	private Thread playThread;
	
	protected ModPlayer() {
	      // empty
	}
	
	public static ModPlayer getInstance() {
		if(instance == null) {
			instance = new ModPlayer();
			instance.xmp.init();
			instance.paused = false;
		}
		return instance;
	}
	    
	private class PlayRunnable implements Runnable {
    	public void run() {
    		short buffer[] = new short[minSize];
       		while (xmp.playFrame() == 0) {
       			int size = xmp.softmixer();
       			buffer = xmp.getBuffer(size, buffer);
       			audio.write(buffer, 0, size / 2);
       			
       			while (paused) {
       				try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						break;
					}
       			}
       		}
       		
       		audio.stop();
       		xmp.endPlayer();
       		xmp.releaseModule();
    	}
    }


	protected void finalize() {
    	xmp.stopModule();
    	paused = false;
    	try {
			playThread.join();
		} catch (InterruptedException e) { }

    	xmp.deinit();
    }
   
    public void play(String file) {
   		if (xmp.loadModule(file) < 0) {
   			return;
   		}
   		audio.play();
   		xmp.startPlayer();
   		
   		PlayRunnable playRunnable = new PlayRunnable();
   		playThread = new Thread(playRunnable);
   		playThread.start();
    }
    
    public void stop() {
    	xmp.stopModule();
    	paused = false;
    }
    
    public void pause() {
    	paused = !paused;
    }
    
    public int time() {
    	return xmp.time();
    }

	public void seek(int seconds) {
		xmp.seek(seconds);
	}
	
	public int getPlayTempo() {
		return xmp.getPlayTempo();
	}
	
	public int getPlayBpm() {
		return xmp.getPlayBpm();
	}
	
	public int getPlayPos() {
		return xmp.getPlayPos();
	}
	
	public int getPlayPat() {
		return xmp.getPlayPat();
	}
	
	public boolean isPaused() {
		return this.paused;
	}
}

