package dev.blijde_broers.object.instances.foods;

import java.awt.Graphics;

import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.GameObjectType;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.Handler;
import dev.blijde_broers.object.components.instances.TextureComponent;
import dev.blijde_broers.object.instances.animals.Animal;

public abstract class Food extends GameObject {
	
	private double energyValue;

	public Food(Transform transform, int id, GameObjectType type, String texture, double energyValue) {
		super(transform, id, type, GameState.Game);
		componentManager.addObjectComponent(new TextureComponent(this, texture,
				new Transform(new Vector2(), transform.getDimensions())));
		this.energyValue = energyValue;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub

	}
	
	public void handleConsumation(Animal animal) {
		animal.setEnergy(animal.getEnergy() + energyValue);
		Handler.foods.remove(this);
	}
	
	public double getEnergyValue() {
		return energyValue;
	}

	public void setEnergyValue(double energyValue) {
		this.energyValue = energyValue;
	}

}
