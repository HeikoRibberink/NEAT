package dev.blijde_broers.misc.collisionComponentParts.instances;

import dev.blijde_broers.misc.collisionComponentParts.CollisionTemplate;
import dev.blijde_broers.misc.math.Line;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;

public class MultiCorner extends CollisionTemplate {
	
	public MultiCorner(Transform transform, int numOfCorners) {
		corners = new Vector2[numOfCorners];
		double rotBetweenCorners = (2 * Math.PI) / (double) numOfCorners;
		for(int rot = 0; rot < numOfCorners; rot++) {
			double x = (float) Math.cos((double) rot * rotBetweenCorners) * (transform.getDimensions().x / 2);
			double y = (float) Math.sin((double) rot * rotBetweenCorners) * (transform.getDimensions().y / 2);
			Vector2 temp = new Vector2(x, y).plus(transform.mid).rotate(transform.getRotation());
			corners[rot] = temp;
		}
		setRibs(new Line[corners.length]);
		setDiagonals(new Line[corners.length]);
		calcRadius();
		
	}

}
