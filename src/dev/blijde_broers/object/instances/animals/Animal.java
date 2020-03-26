package dev.blijde_broers.object.instances.animals;

import java.awt.Graphics;

import dev.blijde_broers.main.Game;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.neuralNetwork.NEAT.AugmentableFFNN;
import dev.blijde_broers.neuralNetwork.NEAT.Genome;
import dev.blijde_broers.neuralNetwork.NEAT.InvalidGenomeException;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.GameObjectType;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.Handler;
import dev.blijde_broers.object.components.instances.RigidBody;
import dev.blijde_broers.object.components.instances.TextureComponent;
import dev.blijde_broers.object.instances.foods.Food;

public class Animal extends GameObject {

	protected double energy = 100;
	protected final double speed;
	protected final double size;
	protected final double matingEnergy = 100;
	private RigidBody rigidBody;
	private AugmentableFFNN nn;
	private static double standardSpeed = .5;
	private static final double standardEnergyUse = 0.1;
	private static final double sensoryDist = 500;
	private double closestFoodRot;

	public Animal(Transform transform, double energy, double size, double speed) {
		super(transform, 0, GameObjectType.Animal, GameState.Game);
		componentManager.addObjectComponent(new TextureComponent(this, "res\\textures\\TestObject.jpg",
				new Transform(new Vector2(), transform.getDimensions())));
		componentManager.addObjectComponent(new RigidBody(this, new Vector2(), 0, 0.2, 0.4, 1, false, false));
		rigidBody = componentManager.getRigidBody();
		Genome temp = new Genome(new AugmentableFFNN(4, 4));
		for (int i = 0; i < 10; i++) {
			temp.mutateRandom();
		}
		try {
			nn = new AugmentableFFNN(temp);
		} catch (InvalidGenomeException e) {
			e.printStackTrace();
		}
		this.energy = energy;
		this.size = size;
		this.speed = speed;
	}

	public Animal(Animal parent) {
		super(new Transform(parent.transform), 0, GameObjectType.Animal, GameState.Game);
		transform.setRotation(Math.random() * Math.PI * 2);
		componentManager.addObjectComponent(new TextureComponent(this, "res\\textures\\TestObject.jpg",
				new Transform(new Vector2(), transform.getDimensions())));
		componentManager.addObjectComponent(new RigidBody(this, new Vector2(), 0, 0.2, 0.4, 1, false, false));
		rigidBody = componentManager.getRigidBody();
		Genome g = parent.nn.genome;
		g.mutateRandom();
		try {
			nn = new AugmentableFFNN(g);
		} catch (InvalidGenomeException e) {
			e.printStackTrace();
		}
		this.energy = parent.matingEnergy;
		this.size = parent.size;
		this.speed = parent.speed;
	}

	@Override
	public void tick() {
		Food closestFood = null;
		double closestFoodDist = Double.MAX_VALUE;
		for (int i = 0; i < Handler.foods.size(); i++) {
			Food currentFood = Handler.foods.get(i);
			Transform cFt = currentFood.getTransform();
			double currentDist = new Vector2(transform.mid, cFt.mid).getDist();
			if (currentDist < closestFoodDist) {
				closestFood = currentFood;
				closestFoodDist = currentDist;
			}
		}
		if (closestFood == null)
			return;
		double closestFoodRot = (new Vector2(transform.mid, closestFood.getTransform().mid).getDirection()
				- transform.getRotation());
//		if (closestFoodDist > sensoryDist) {
//			closestFoodDist = sensoryDist;
//			closestFoodRot = 0;
//		}
		double[] inputs = new double[] { closestFoodRot / Math.PI, closestFoodDist / sensoryDist, energy, 1 };
		double[] decisions = new double[4];
		try {
			decisions = nn.calculate(inputs);
		} catch (StackOverflowError e) {
			System.out.println(nn.genome.toString());
			throw new StackOverflowError();
		}
		if (decisions[0] > 0.5) {
			rigidBody.addPosForce(new Vector2(speed * standardSpeed, 0).rotate(transform.getRotation()));
		}
		if (decisions[1] > 0.5) {
			rigidBody.addPosForce(new Vector2(speed * -standardSpeed, 0).rotate(transform.getRotation()));
		}
		if (decisions[2] > 0.5) {
			rigidBody.addRotForce(standardSpeed * speed / 5);
		}
		if (decisions[3] > 0.5) {
			rigidBody.addRotForce(-standardSpeed * speed / 5);
		}
		if (closestFoodDist < 50) {
			closestFood.handleConsumation(this);
		}
		if (energy > 1.5 * matingEnergy) {
			Handler.objects.add(new Animal(this));
			energy -= matingEnergy;
		}

		if (energy <= 0)
			die(-1);
		energy -= standardEnergyUse * speed * speed * size;

		if (transform.mid.x > Game.WORLD_TRANSFORM.getURCorner().x)
			transform.mid.x = Game.WORLD_TRANSFORM.getULCorner().x;
		if (transform.mid.x < Game.WORLD_TRANSFORM.getULCorner().x)
			transform.mid.x = Game.WORLD_TRANSFORM.getURCorner().x;
		if (transform.mid.y < Game.WORLD_TRANSFORM.getURCorner().y)
			transform.mid.y = Game.WORLD_TRANSFORM.getLRCorner().y;
		if (transform.mid.y > Game.WORLD_TRANSFORM.getLRCorner().y)
			transform.mid.y = Game.WORLD_TRANSFORM.getURCorner().y;
		this.closestFoodRot = closestFoodRot;
	}

	@Override
	public void render(Graphics g) {
//		Transform t = new Transform(transform);
//		t.setDimensions(t.getDimensions().divide(2));
//		t.setRotation(closestFoodRot);
//		TextureComponent.renderImage(g, Texture.dot.img, t);

	}

	public void die(int ownIndex) {
//		if(Math.random() < 0.1) Handler.objects.add(new Animal(this));
		if (ownIndex == -1)
			Handler.objects.remove(this);
		else
			Handler.objects.remove(ownIndex);
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public RigidBody getRigidBody() {
		return rigidBody;
	}

	public void setRigidBody(RigidBody rigidBody) {
		this.rigidBody = rigidBody;
	}

	public double getSpeed() {
		return speed;
	}

	public double getSize() {
		return size;
	}

}
