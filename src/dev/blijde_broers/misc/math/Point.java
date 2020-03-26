package dev.blijde_broers.misc.math;

public class Point {
	
	public double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2 asVector2() {
		return new Vector2(x, y);
	}

}
