package dev.blijde_broers.object.instances;

import java.awt.Graphics;

import dev.blijde_broers.misc.Texture;
import dev.blijde_broers.misc.collisionComponentParts.instances.Rectangle;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.GameObjectType;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.components.instances.CollisionComponent;
import dev.blijde_broers.object.components.instances.RigidBody;
import dev.blijde_broers.object.components.instances.TextureComponent;

public class Wall extends GameObject {

	public Wall(Transform transform) {
		super(transform, 0, GameObjectType.Wall, GameState.Game);
		componentManager.addObjectComponent(new CollisionComponent(this, new Rectangle(new Transform(new Vector2(), transform.getDimensions()))));
		componentManager.addObjectComponent(new TextureComponent(this, Texture.dot, new Transform(new Vector2(), transform.getDimensions())));
		componentManager.addObjectComponent(new RigidBody(this, new Vector2(), 0, 1000, 1000, Double.POSITIVE_INFINITY, false, false));
	}

	@Override
	public void tick() {
//		System.out.println(transform.mid);

	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub

	}

}
