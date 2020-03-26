package dev.blijde_broers.object.components;

import java.awt.Graphics;
import java.util.ArrayList;

import dev.blijde_broers.misc.collisionComponentParts.instances.EmptyCollisionTemplate;
import dev.blijde_broers.object.components.instances.CollisionComponent;
import dev.blijde_broers.object.components.instances.RigidBody;

public class ObjectComponentManager {
	// Deze class zorgt ervoor dat de components - de eigenschappen van een object -
	// allemaal geupdate worden.
	
	private int collisionComponentIndex = -1, rigidBodyIndex = -1;
	
	private ArrayList<ObjectComponent> objectComponents = new ArrayList<ObjectComponent>();
	
	public ObjectComponentManager() {
		
	}

	public void tick() {
		for (int i = 0; i < objectComponents.size(); i++) {
			objectComponents.get(i).tick();
		}
	}

	public void render(Graphics g) {
		for (int i = 0; i < objectComponents.size(); i++) {
			objectComponents.get(i).render(g);
		}
	}

	public ArrayList<ObjectComponent> getObjectComponents() {
		return objectComponents;
	}

	public void setObjectComponents(ArrayList<ObjectComponent> objectComponents) {
		this.objectComponents = objectComponents;
	}
	
	public void addObjectComponent(ObjectComponent c) {
		if(c instanceof CollisionComponent) {
			collisionComponentIndex = objectComponents.size();
		}
		if(c instanceof RigidBody) {
			rigidBodyIndex = objectComponents.size();
		}
		objectComponents.add(c);
	}
	
	public CollisionComponent getCollisionComponent() {
		if (collisionComponentIndex != -1) {
			CollisionComponent component;
			if ((component = (CollisionComponent) objectComponents.get(collisionComponentIndex)) != null) {
				return component;
			} 
		}
		return new CollisionComponent(null, new EmptyCollisionTemplate());
		
	}
	
	public RigidBody getRigidBody() {
		if (rigidBodyIndex != -1) {
			RigidBody component;
			if ((component = (RigidBody) objectComponents.get(rigidBodyIndex)) != null) {
				return component;
			} 
		}
		return null;
	}
	
	
}
