package dev.blijde_broers.object.components;

import java.awt.Graphics;

import dev.blijde_broers.object.GameObject;

public abstract class ObjectComponent {
	
	protected GameObject parent;

	public ObjectComponent(GameObject parent) {
		this.parent = parent;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}
}
