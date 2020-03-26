package dev.blijde_broers.misc.collisionComponentParts;

import java.util.ArrayList;

import dev.blijde_broers.misc.math.Line;
import dev.blijde_broers.misc.math.Math2D;
import dev.blijde_broers.misc.math.Point;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.components.instances.CollisionComponent;

public abstract class CollisionTemplate {

	protected CollisionComponent parent;
	protected Vector2[] corners;
	private Line[] border;
	private Line[] diagonals;
	private double radiusSquared, radius;
	private boolean finishedInit = false;
	public boolean checking = false;

	public CollisionTemplate(Vector2[] corners) {
		this.corners = corners;
		border = new Line[corners.length];
		diagonals = new Line[corners.length];
		calcRadius();
	}

	public CollisionTemplate() {

	}

	public boolean intersects(CollisionTemplate cT) {
		if (finishedInit) {
			if (Math2D.dist(getTransform().mid.asPoint(),
					cT.getTransform().mid.asPoint()) < (radiusSquared + cT.radiusSquared)) {
				checking = true;
				for (Line thisL : border) {
					for (Line otherL : cT.border) {
						if (otherL != null && thisL != null) {
							if (thisL.intersects(otherL)) {
								return true;
							}
						}
					}
				}
				return false;
			}
		}
		finishedInit = true;
		return false;

	}

	public IntersectionReturn intersection(CollisionTemplate cT) {
		if (finishedInit) {
			if (Math2D.dist(getTransform().mid.asPoint(),
					cT.getTransform().mid.asPoint()) < (radius + cT.radius)) {
				checking = true;
				ArrayList<Point> points = new ArrayList<Point>();
				ArrayList<Line> otherIntersectedLines = new ArrayList<Line>();
				ArrayList<Line> thisIntersectedLines = new ArrayList<Line>();
				// Calculate intersection
				for (Line thisL : border) {
					for (Line otherL : cT.border) {
						if (otherL != null && thisL != null) {
							Point intersection;
							if ((intersection = thisL.intersection(otherL)) != null) {
								points.add(intersection);
								otherIntersectedLines.add(otherL);
								thisIntersectedLines.add(thisL);
							}
							if(points.size() >= 2) {
								break;
							}
						}
					}
					if(points.size() >= 2) {
						break;
					}
				}
				
				if(points.size() < 1) return null;
				
				
				Vector2 wayOut = new Vector2(0, 0);
				int divisor = 0;
				for (Line otherBorder : otherIntersectedLines) {
					for (Line thisDiagonal : diagonals) {
						if (otherBorder != null && thisDiagonal != null) {
							Point intersection;
							if ((intersection = thisDiagonal.intersection(otherBorder)) != null) {
								wayOut.increment(new Vector2(intersection.asVector2(), thisDiagonal.v2));
								divisor++;
							}
						}
					}
				}
				
				for (Line thisBorder : thisIntersectedLines) {
					for (Line otherDiagonal : cT.diagonals) {
						if (thisBorder != null && otherDiagonal != null) {
							Point intersection;
							if ((intersection = otherDiagonal.intersection(thisBorder)) != null) {
								wayOut.increment(new Vector2(otherDiagonal.v2, intersection.asVector2()));
								divisor++;
							}
						}
					}
				}
				wayOut.divideThis(divisor);
				IntersectionReturn out;
				Point[] pointsOut = new Point[points.size()];
				points.toArray(pointsOut);
				out = new IntersectionReturn(null, pointsOut, wayOut);
				return out;
			}
		}
		finishedInit = true;
		return null;
	}

	// Updates all lines to their absolute coordinates. This is done by adding the
	// Transform absolute coordinates to all corners.
	public void updateTemplate() {
		if (corners.length > 0) {
			for (int i = 0; i < corners.length - 1; i++) {
				// Initialize ribs for each corner
				border[i] = new Line(
						corners[i].plus(getTransform().mid).rotate(getTransform().getRotation(),
								getTransform().mid.asPoint()),
						corners[i + 1].plus(getTransform().mid).rotate(getTransform().getRotation(),
								getTransform().mid.asPoint()));

				// Initialize diagonals for each corner
				diagonals[i] = new Line(
						getTransform().mid,
						corners[i].plus(getTransform().mid)
						.rotate(getTransform().getRotation(), getTransform().mid.asPoint()));
			}
			border[corners.length - 1] = new Line(
					corners[corners.length - 1].plus(getTransform().mid).rotate(getTransform().getRotation(),
							getTransform().mid.asPoint()),
					corners[0].plus(getTransform().mid).rotate(getTransform().getRotation(),
							getTransform().mid.asPoint()));
			diagonals[corners.length - 1] = new Line(getTransform().mid, corners[corners.length - 1].plus(getTransform().mid)
					.rotate(getTransform().getRotation(), getTransform().mid.asPoint()));
		}
	}

	public void calcRadius() {
		radiusSquared = 0;
		for (Vector2 c : corners) {
			double tempRadius = Math2D.distSqr(new Point(), c.asPoint());
			if (tempRadius > radiusSquared) {
				radiusSquared = tempRadius;
			}
		}
		radius = Math.sqrt(radiusSquared);
	}

	// Updates all points to their relative position originating from the Transform
	// position.
	public void setRotation(double rotation) {
		getTransform().setRotation(rotation);
		for (Vector2 c : corners) {
			c.setDirection(rotation);
		}
	}

	public void rotate(double theta) {
		getTransform().rotate(theta);
		for (Vector2 c : corners) {
			c.rotate(theta);
		}
	}

	public Transform getTransform() {
		if (parent.getParent() != null)
			return parent.getParent().getTransform();
		return new Transform();
	}

	public CollisionComponent getParent() {
		return parent;
	}

	public void setParent(CollisionComponent parent) {
		this.parent = parent;
	}

	public Vector2[] getCorners() {
		return corners;
	}

	public void setCorners(Vector2[] corners) {
		this.corners = corners;
		calcRadius();
	}

	public Line[] getRibs() {
		return border;
	}

	public void setRibs(Line[] ribs) {
		this.border = ribs;
	}

	public Line[] getDiagonals() {
		return diagonals;
	}

	public void setDiagonals(Line[] diagonals) {
		this.diagonals = diagonals;
	}

	public double getRadiusSquared() {
		return radiusSquared;
	}

	public double getRadius() {
		return radius;
	}

}
