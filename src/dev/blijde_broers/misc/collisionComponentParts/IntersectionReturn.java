package dev.blijde_broers.misc.collisionComponentParts;

import dev.blijde_broers.misc.math.Point;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.components.instances.CollisionComponent;

public class IntersectionReturn {
	
	public CollisionComponent cc;
	public Point[] points;
	public Vector2 wayOut;
	

	public IntersectionReturn(CollisionComponent cc, Point[] points, Vector2 wayOut) {
		this.cc = cc;
		this.points = points;
		this.wayOut = wayOut;
	}

}
