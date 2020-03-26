package dev.blijde_broers.misc.math;

import java.awt.geom.Line2D;

public class Line {

	public Vector2 v1, v2;

	public Line(double x1, double y1, double x2, double y2) {
		v1 = new Vector2(x1, y1);
		v2 = new Vector2(x2, y2);
	}
	
	public Line(Vector2 v1, Vector2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Line(Point p1, Point p2) {
		v1 = p1.asVector2();
		v2 = p2.asVector2();
	}
	
	public Line() {
		v1 = new Vector2();
		v2 = new Vector2();
	}

	public Point intersection(Line l) {
		double d = (v1.x - v2.x) * (l.v1.y - l.v2.y) - (v1.y - v2.y) * (l.v1.x - l.v2.x);
		if (d == 0 || !Line2D.linesIntersect(v1.x, v1.y, v2.x, v2.y, l.v1.x, l.v1.y, l.v2.x, l.v2.y))
			return null;
		double xi = ((l.v1.x - l.v2.x) * (v1.x * v2.y - v1.y * v2.x) - (v1.x - v2.x) * (l.v1.x * l.v2.y - l.v1.y * l.v2.x)) / d;
		double yi = ((l.v1.y - l.v2.y) * (v1.x * v2.y - v1.y * v2.x) - (v1.y - v2.y) * (l.v1.x * l.v2.y - l.v1.y * l.v2.x)) / d;
		return new Point((int) xi, (int) yi);
	}
	
	public boolean intersects(Line l) {
		return Line2D.linesIntersect(v1.x, v1.y, v2.x, v2.y, l.v1.x, l.v1.y, l.v2.x, l.v2.y);
	}
	
	public Line add(Vector2 v) {
		return new Line(v1.plus(v), v2.plus(v));
	}

}
