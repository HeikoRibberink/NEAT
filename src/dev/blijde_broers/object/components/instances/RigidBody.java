package dev.blijde_broers.object.components.instances;

import java.awt.Graphics;

import dev.blijde_broers.main.Game;
import dev.blijde_broers.misc.Texture;
import dev.blijde_broers.misc.collisionComponentParts.IntersectionReturn;
import dev.blijde_broers.misc.math.Line;
import dev.blijde_broers.misc.math.Point;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.components.ObjectComponent;
import dev.blijde_broers.object.components.simulations.Simulatable;

public class RigidBody extends ObjectComponent implements Simulatable {

	private Vector2 posVel;
	public double rotVel = 0;
	public boolean xTied = false, yTied = false, rotTied = false;
	public double posResistance = 0, rotResistance = 0, mass = 1;
	public boolean doCollisionResolution = false;
	public boolean gravity = false;

	private Transform transform;
	private CollisionComponent collisionComponent;

	public static Vector2 gravitationalForce = new Vector2(0, 0);

	private Line renderImpulse;

	public RigidBody(GameObject parent, Vector2 posVel, double rotVel, boolean xTied, boolean yTied, boolean rotTied,
			double posResistance, double rotResistance, double mass, boolean gravity, boolean doCollisionResolution) {
		super(parent);
		this.posVel = posVel;
		this.rotVel = rotVel;
		this.xTied = xTied;
		this.yTied = yTied;
		this.rotTied = rotTied;
		this.posResistance = posResistance;
		this.rotResistance = rotResistance;
		this.mass = mass;
		this.gravity = gravity;
		transform = parent.getTransform();
		this.doCollisionResolution = doCollisionResolution;
		// if (doCollisionResolution)
		collisionComponent = parent.getComponentManager().getCollisionComponent();
	}

	public RigidBody(GameObject parent, Vector2 posVel, double rotVel, double posResistance, double rotResistance,
			double mass, boolean gravity, boolean doCollisionResolution) {
		super(parent);
		this.posVel = posVel;
		this.rotVel = rotVel;
		this.posResistance = posResistance;
		this.rotResistance = rotResistance;
		this.mass = mass;
		this.gravity = gravity;
		transform = parent.getTransform();
		this.doCollisionResolution = doCollisionResolution;
		// if (doCollisionResolution)
		collisionComponent = parent.getComponentManager().getCollisionComponent();
	}

	public RigidBody(GameObject parent, boolean doCollisionResolution) {
		super(parent);
		this.posVel = new Vector2();
		transform = parent.getTransform();
		this.doCollisionResolution = doCollisionResolution;
		// if (doCollisionResolution)
		collisionComponent = parent.getComponentManager().getCollisionComponent();
	}

	@Override
	public void tick() {
		if (gravity) {
			addPosForce(gravitationalForce.multiply(mass));
		}
		posVel.multiplyThis(1 - (posResistance / mass));
		rotVel *= 1 - (rotResistance / mass);
	}

	@Override
	public void render(Graphics g) {
		if (Game.debug) {
			if (renderImpulse != null) {
				Vector2 temp = new Vector2(renderImpulse.v1.asPoint(), renderImpulse.v2.asPoint());
				double rot = temp.getDirection();
				temp = new Vector2(temp.getDist(), 10);
				Transform tr = new Transform(
						renderImpulse.v1.plus(new Vector2(renderImpulse.v1, renderImpulse.v2).divide(2)), temp);
				tr.rotate(rot);
				TextureComponent.renderImage(g, Texture.dot.img, tr);
				TextureComponent.renderImage(g, Texture.dot.img,
						new Transform(new Vector2(renderImpulse.v2), new Vector2(15, 15)));
			}
		}
	}

	@Override
	public void simulateTick(int simsPerTick) {
		doCollisionResolution = (collisionComponent == null) ? false : true;
		// if (Float.isFinite(mass)) {
		Vector2 posVel = new Vector2(this.posVel);
		double rotVel = this.rotVel;
		if (xTied) {
			posVel.x = 0;
		}
		if (yTied) {
			posVel.y = 0;
		}
		if (rotTied) {
			rotVel = 0;
		}
		posVel.divideThis(simsPerTick);
		rotVel /= simsPerTick;
		if (doCollisionResolution) {
			transform.mid.increment(posVel);
			transform.rotate(rotVel);
			if (collisionComponent.intersects()) {
				IntersectionReturn[] intersections = collisionComponent.intersection();
				for (int j = 0; j < intersections.length; j++) {
					for (int k = 0; k < intersections[j].points.length; k++) {
//						resoluteStatic(intersections, simsPerTick);
						resoluteDynamic(intersections[j].cc, intersections[j].points[k], simsPerTick);
					}
				}
			}
		} else {
			transform.mid.increment(posVel);
			transform.rotate(rotVel);
		}
	}

	private void resoluteDynamic(CollisionComponent cc, Point p, int simsPerTick) {
		RigidBody otherRB = cc.getParent().getComponentManager().getRigidBody();
		int test = 0;
		while (collisionComponent.intersects()) {
			transform.mid.increment(posVel.min().divide(simsPerTick));
			transform.rotate(-rotVel / (simsPerTick));
			test++;
			if (test > 1) {
				System.out.println("loop");
				break;
			}
		}
		if (test > 1)
			System.out.println(test);
		Vector2 pAsVector2 = p.asVector2();
		Vector2 vThis = pAsVector2.minus(transform.mid);
		Vector2 vOther = pAsVector2.minus(otherRB.transform.mid);
		double rotVThisDir = vThis.getDirection() + (Math.toRadians(90) * (rotVel / Math.abs(rotVel)));
		double rotVOtherDir = vOther.getDirection()
				+ (Math.toRadians(90) * (otherRB.rotVel / Math.abs(otherRB.rotVel)));
		if (Double.isNaN(rotVThisDir))
			rotVThisDir = 0;
		if (Double.isNaN(rotVOtherDir))
			rotVOtherDir = 0;
		Vector2 thisRotV = new Vector2(rotVel * vThis.getDist(), 0).setDirection(rotVThisDir);
		Vector2 otherRotV = new Vector2(otherRB.rotVel * vOther.getDist(), 0).setDirection(rotVOtherDir);
		Vector2 thisImpulse = new Vector2(posVel, otherRB.posVel).plus(new Vector2(thisRotV, otherRotV));
		Vector2 otherImpulse = new Vector2(otherRB.posVel, posVel).plus(new Vector2(otherRotV, thisRotV));
		double thisImpulseDir = thisImpulse.getDirection();
		double otherImpulseDir = otherImpulse.getDirection();
		double thisImpulseEnergy = (1d / 2d) * mass * Math.pow(thisImpulse.getDist(), 2) / 10;
		double otherImpulseEnergy = (1d / 2d) * otherRB.mass * Math.pow(otherImpulse.getDist(), 2) / 10;
		// double thisImpulseEnergy = (1d / 2d) * Math.pow(thisImpulse.getDist(), 2);
		// double otherImpulseEnergy = (1d / 2d) * Math.pow(otherImpulse.getDist(), 2);
		if (Double.isInfinite(otherRB.mass) || !otherRB.doCollisionResolution) {
			otherImpulseEnergy = 0;
		}
		double totalImpulseEnergy = thisImpulseEnergy + otherImpulseEnergy;
		if (Double.isFinite(otherRB.mass) && otherRB.doCollisionResolution) {
			resoluteToImpulse(vThis, thisImpulseDir, totalImpulseEnergy / 2);
			otherRB.resoluteToImpulse(vOther, otherImpulseDir, totalImpulseEnergy / 2);
		} else {
			resoluteToImpulse(vThis, thisImpulseDir, totalImpulseEnergy);
		}
		renderImpulse = new Line(p.asVector2().plus(thisImpulse.multiply(10f)), p.asVector2());
		// otherRB.renderImpulse = new Line(pAsVector2.plus(otherImpulse.multiply(1f)),
		// p.asVector2());
	}
	
	private void resoluteStatic(IntersectionReturn[] intersections, int simsPerTick) {
		for (IntersectionReturn intersection : intersections) {
//			if (Double.isInfinite(intersection.cc.getParent().getComponentManager().getRigidBody().mass)) {
				RigidBody otherRB = intersection.cc.getParent().getComponentManager().getRigidBody();
				Vector2 moveThis = new Vector2(intersection.wayOut);
				Vector2 moveOther = new Vector2(intersection.wayOut);
				double totalMass = mass + otherRB.mass;
				if (Double.isFinite(totalMass)) {
					moveThis.multiplyThis(otherRB.mass / totalMass);
					moveOther.multiplyThis(mass / totalMass);
				} else {
					moveOther = new Vector2();
				}

				//			System.out.println("----");
				//			System.out.println(parent.getType());
				//			System.out.println(intersection.wayOut);
				//			System.out.println(moveThis);
				//			System.out.println(moveOther);
				//			System.out.println(totalMass);
				transform.mid.increment(moveThis);
				otherRB.transform.mid.increment(moveOther);
//			} else {
//				while (collisionComponent.intersects()) {
//					transform.mid.increment(posVel.min().divide(simsPerTick));
//					transform.rotate(-rotVel / (simsPerTick));
//				}
//			}
		}
	}

	private double ErToVr(double E) {
		double r = collisionComponent.getCollisionTemplate().getRadius();
		double inertia = (0.25d) * mass * Math.pow(r, 2);
		double omega = Math.sqrt(E / (inertia / 2));
		return omega;
	}

	private double getEtoR(Vector2 impulseFrom, double direction) {
		double maxHeight = collisionComponent.getCollisionTemplate().getRadius();
		Vector2 tempImpulseFrom = new Vector2(impulseFrom);
		tempImpulseFrom.rotate(-direction);
		double EtoR = tempImpulseFrom.y / (maxHeight * 2);
		if (EtoR > 0.5)
			EtoR = 0.5;
		if (EtoR < -0.5)
			EtoR = -0.5;
		return EtoR;
	}

	public void resoluteToImpulse(Vector2 impulseFrom, double impulseDirection, double impulseEnergy) {
		double EtoR = getEtoR(impulseFrom, impulseDirection);
		double EtoP = (1 - Math.abs(EtoR));
		double rotVel = 0;
		if (EtoR != 0) {
			rotVel = ErToVr(Math.abs(impulseEnergy * EtoR)) * (-EtoR / Math.abs(EtoR));
		}

		this.rotVel += rotVel;
		addPosForce(impulseDirection, EtoP * impulseEnergy);
	}

	public void addPosForce(double direction, double energy) {
		double v = Math.sqrt(energy / mass * 2);
		Vector2 posVel = new Vector2(v, 0f).setDirection(direction);
		this.posVel.increment(posVel);
	}

	public void addPosForce(Vector2 force) {
		double energy = force.getDist();
		double direction = force.getDirection();
		double v = Math.sqrt(energy / mass * 2);
		Vector2 posVel = new Vector2(v, 0f).setDirection(direction);
		this.posVel.increment(posVel);
	}

	public void addRotForce(double force) {
		rotVel += force / mass;
	}

	public Vector2 getPosVel() {
		return posVel;
	}

}
