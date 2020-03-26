package dev.blijde_broers.misc.math;

import java.awt.Graphics;

import dev.blijde_broers.misc.Texture;
import dev.blijde_broers.object.components.instances.TextureComponent;

public class Transform {
	
	//The position in 2-dimensional space from the origin (x = 0; y = 0).
	public Vector2 mid;
	//The position in 2-dimensional space originating from Vector2 mid, used as the width and height of the object, this vector
	//does not rotate.
	private Vector2 lr;
	//The angle used to calculate the corners of thos object
	private double rotation;
	
	private Texture dot = Texture.dot;

	public Transform(Vector2 mid, Vector2 dimensions, double rotation) {
		this.mid = mid;
		dimensions.divideThis(2);
		this.lr = dimensions;
		this.rotation = rotation;
	}
	
	public Transform(Vector2 mid, Vector2 dimensions) {
		this.mid = mid;
		dimensions.divideThis(2);
		this.lr = dimensions;
		rotation = 0;
	}
	
	public Transform(Vector2 mid, double rotation) {
		this.mid = mid;
		lr = new Vector2();
		this.rotation = rotation;
	}
	
	public Transform(Vector2 mid) {
		this.mid = mid;
		lr = new Vector2();
		rotation = 0;
	}
	
	public Transform() {
		mid = new Vector2();
		lr = new Vector2();
		rotation = 0;
	}
	public Transform(Transform transform) {
		this.mid = new Vector2(transform.mid);
		this.lr = new Vector2(transform.lr);
		this.rotation = transform.rotation;
		
	}

	//Returns the vector representing the width and height of this object.
	public Vector2 getDimensions() {
		return this.lr.multiply(2);
	}
	//returns the absolute lower right corner of this object. 
	public Vector2 getLRCorner() {
		Vector2 v = new Vector2(lr);
		v.rotate(rotation);
		v.increment(mid);
		return v;
	}
	//returns the absolute upper left corner of this object.
	public Vector2 getULCorner() {
		Vector2 v = new Vector2(lr.min());
		v.rotate(rotation);
		v.increment(mid);
		return v;
	}
	//returns the absolute lower left corner of this object.
	public Vector2 getLLCorner() {
		Vector2 v = new Vector2(this.lr.min().x, this.lr.y);
		v.rotate(rotation);
		v.increment(mid);
		return v;
	}
	//returns the absolute upper right corner of this object.
	public Vector2 getURCorner() {
		Vector2 v = new Vector2(this.lr.x, this.lr.min().y);
		v.rotate(rotation);
		v.increment(mid);
		return v;
	}
	
	public Transform add(Transform tr) {
		return new Transform(mid.plus(tr.mid), rotation + tr.rotation);
	}
	
	public void increment(Transform tr) {
		mid.increment(tr.mid);
		rotation += tr.rotation;
	}
	
	public void increment(Vector2 v) {
		mid.increment(v);
	}
	
	public void rotate(double theta) {
		rotation += theta;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
		
	}
	
	public double getRotation() {
		if(rotation > Math.PI) {
			rotation = -Math.PI;
		}
		if(rotation < -Math.PI) {
			rotation = Math.PI;
		}
		return rotation;
	}
	
	public void moveOnRelativeXAxis(double distance) {
		mid.x += Math.cos(rotation) * distance;
		mid.y += Math.sin(rotation) * distance;
	}
	
	public void moveOnRelativeYAxis(double distance) {
		mid.x += Math.cos(rotation + 0.5 * Math.PI) * distance;
		mid.y += Math.sin(rotation + 0.5 * Math.PI) * distance;
	}
	
	public void render(Graphics g) {
		TextureComponent.renderImage(g, dot.img, new Transform(mid, new Vector2(2, 2)));
		TextureComponent.renderImage(g, dot.img, new Transform(getURCorner(), new Vector2(2, 2)));
		TextureComponent.renderImage(g, dot.img, new Transform(getLRCorner(), new Vector2(2, 2)));
		TextureComponent.renderImage(g, dot.img, new Transform(getLLCorner(), new Vector2(2, 2)));
		TextureComponent.renderImage(g, dot.img, new Transform(getULCorner(), new Vector2(2, 2)));
	}
	
	public void setDimensions(Vector2 dimensions) {
		dimensions.divideThis(2);
		this.lr = dimensions;
	}

}
