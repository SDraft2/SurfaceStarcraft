package com.leesb.surfacestarcraft;

/*
 
 public class Unit extends Rectangle{
 	
	public int x, y;
	public width, height;
	
	public Rectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this. width = width;
		this.height = height
	}
 
 
 
 */
public class Unit {
	public int x, y;
	protected int width, height, speed;
	
	public Unit(int x, int y, int width, int height, int speed)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	
}
