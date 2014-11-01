package com.actions;

public class Translate implements IBoneAnimation {
	public String boneName = "";
	public int frame = 0;
	public float x = 0;
	public float y = 0;
	public float angle = 0;
	public float time = 0;
	
	public int getFrame() {
		return frame;
	}

	public void setFrame(int pFrame) {
		frame = pFrame;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float pTime) {
		time = pTime;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float pAngle) {
		angle = pAngle;
	}

	public String getBoneName() {
		return boneName;
	}

	public void setBoneName(String pName) {
		boneName = pName;
	}

	public float getX() {
		return x;
	}

	public void setX(float pX) {
		x = pX;
	}

	public float getY() {
		return y;
	}

	public void setY(float pY) {
		y = pY;
	}
	
	public String toString() {
		return "(IBoneAnimation@"+this.boneName + ": x="+x+";y:"+y+";angle:"+angle+";frame:"+frame+")";
	}
}
