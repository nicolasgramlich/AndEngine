package com.actions;

public interface IBoneAnimation {
	public String boneName = "";
	public int frame = 0;
	public float x = 0;
	public float y = 0;
	public float angle = 0;
	public float time = 0;
	
	public int getFrame();
	public void setFrame(int pFrame);
	
	public float getTime();
	public void setTime(float pTime);
	
	public float getAngle();
	public void setAngle(float pAngle);
	
	public String getBoneName();
	public void setBoneName(String pName);
	
	public float getX();
	public void setX(float pX);
	
	public float getY();
	public void setY(float pY);
}
