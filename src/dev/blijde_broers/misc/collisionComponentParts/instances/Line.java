package dev.blijde_broers.misc.collisionComponentParts.instances;

import dev.blijde_broers.misc.collisionComponentParts.CollisionTemplate;
import dev.blijde_broers.misc.math.Vector2;

public class Line extends CollisionTemplate {

	public Line(Vector2 v1, Vector2 v2) {
		super(new Vector2[] {v1, v2});
	}

}
