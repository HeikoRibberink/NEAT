package dev.blijde_broers.misc.math;

public class Math2D {

	public static double dist(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
	
	public static double distSqr(Point p1, Point p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}
	
	public static Point randomPoint(Vector2 min, Vector2 max) {
		return new Point(ExtendedMath.random(min.x, max.x), ExtendedMath.random(min.y, max.y));
	}
	
	public static Point randomPoint() {
		return new Point(Math.random(), Math.random());
	}

}
