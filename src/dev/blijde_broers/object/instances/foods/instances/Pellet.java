package dev.blijde_broers.object.instances.foods.instances;

import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObjectType;
import dev.blijde_broers.object.instances.foods.Food;

public class Pellet extends Food {

	public Pellet(Vector2 pos) {
		super(new Transform(pos, new Vector2(20, 20)), 0, GameObjectType.Pellet, "res\\textures\\Brammm.jpg", 40);
	}

}
