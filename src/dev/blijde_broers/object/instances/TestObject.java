package dev.blijde_broers.object.instances;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import dev.blijde_broers.input.KeyManager;
import dev.blijde_broers.misc.collisionComponentParts.instances.Rectangle;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.GameObjectType;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.components.instances.CollisionComponent;
import dev.blijde_broers.object.components.instances.RigidBody;
import dev.blijde_broers.object.components.instances.TextureComponent;

public class TestObject extends GameObject {

	private RigidBody rigidBody;

	private boolean temp = true;

	public TestObject(Transform transform) {
		super(transform, 0, GameObjectType.TestObject, GameState.Game);
		componentManager.addObjectComponent(new CollisionComponent(this,
				new Rectangle(new Transform(new Vector2(), transform.getDimensions().multiply(1)))));
		componentManager.addObjectComponent(new TextureComponent(this, "res\\textures\\TestObject.jpg",
				new Transform(new Vector2(), transform.getDimensions())));
		componentManager.addObjectComponent(new RigidBody(this, new Vector2(), 0, 0.01, 0.01, 1, true, true));
		// GameObject temp = Handler.objects.get(0);
		// componentManager.addObjectComponent(new SpringJoint(this,
		// new Vector2(),
		// temp.getComponentManager().getRigidBody(),
		// new Vector2(), 600, 1E-1));
		rigidBody = componentManager.getRigidBody();
	}

	@Override
	public void tick() {
		if (KeyManager.pressed[KeyEvent.VK_K]) {
			// transform.increment(new Vector2(0, 3).rotate(transform.getRotation()));
			// rigidBody.addPosForce((new Vector2(0, 3).rotate(transform.getRotation())));
			rigidBody.addPosForce(new Vector2(0, 10));
		}
		if (KeyManager.pressed[KeyEvent.VK_I]) {
			// transform.increment(new Vector2(0, -3).rotate(transform.getRotation()));
			// rigidBody.addPosForce((new Vector2(0, -3).rotate(transform.getRotation())));
			rigidBody.addPosForce(new Vector2(0, -10));
		}
		if (KeyManager.pressed[KeyEvent.VK_L]) {
			// transform.rotate(0.05);
			rigidBody.addRotForce(.04);
			// rigidBody.addPosForce(new Vector2(10, 0));
		}
		if (KeyManager.pressed[KeyEvent.VK_J]) {
			// transform.rotate(-0.05);
			rigidBody.addRotForce(-.04);
			// rigidBody.addPosForce(new Vector2(-10, 0));
		}
		if (KeyManager.pressed[KeyEvent.VK_SPACE] && temp) {
			// componentManager.getObjectComponents().remove(componentManager.getObjectComponents().size()
			// - 1);
			// temp = false;
		}

	}

	@Override
	public void render(Graphics g) {
		// transform.render(g);
	}

}
