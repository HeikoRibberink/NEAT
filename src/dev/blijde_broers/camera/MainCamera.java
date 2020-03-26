package dev.blijde_broers.camera;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import dev.blijde_broers.input.KeyManager;
import dev.blijde_broers.input.MouseWheelManager;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.GameObjectType;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.Handler;
import dev.blijde_broers.object.components.instances.RigidBody;

public class MainCamera extends GameObject {
	
	public double zoom;
	private RigidBody rigidBody;
	private boolean followPlayer = true;
	
	public static GameState[] STATES = new GameState[] {
			GameState.Game
	};

	public MainCamera(Transform transform) {
		super(transform, 0, GameObjectType.MainCamera, STATES);
		componentManager.addObjectComponent(new RigidBody(this, new Vector2(), 0, 0.2f, 0, 1f, false, false));
		rigidBody = componentManager.getRigidBody();
	}
	
	@Override
	public void tick() {
		zoom =  1 / (Math.pow(((double) MouseWheelManager.mouseWheelRotation + 9.9), 2) / 100);
		if(KeyManager.pressed[KeyEvent.VK_DOWN]) {
//			transform.increment(new Vector2(0, -3));
			rigidBody.addPosForce(new Vector2(0f, (float) (2f / zoom)));
			followPlayer = false;
		}
		if(KeyManager.pressed[KeyEvent.VK_UP]) {
//			transform.increment(new Vector2(0, 3));
			rigidBody.addPosForce(new Vector2(0f, (float) -(2f / zoom)));
			followPlayer = false;
		}
		if(KeyManager.pressed[KeyEvent.VK_RIGHT]) {
//			transform.increment(new Vector2(-3, 0));
			rigidBody.addPosForce(new Vector2((float) (2f / zoom), 0f));
			followPlayer = false;
		}
		if(KeyManager.pressed[KeyEvent.VK_LEFT]) {
//			transform.increment(new Vector2(3, 0));
			rigidBody.addPosForce(new Vector2((float) -(2f / zoom), 0f));
			followPlayer = false;
		}
		if(KeyManager.pressed[KeyEvent.VK_F1]) {
			followPlayer = true;
		}
		if(followPlayer) {
			followPlayer();
		}
	}

	@Override
	public void render(Graphics g) {
		
	}
	
	private void followPlayer() {
		transform.mid = new Vector2(Handler.objects.get(0).getTransform().mid);
	}
	
	
	
}
