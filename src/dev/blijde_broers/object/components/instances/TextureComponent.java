package dev.blijde_broers.object.components.instances;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import dev.blijde_broers.main.Game;
import dev.blijde_broers.misc.Texture;
import dev.blijde_broers.misc.collisionComponentParts.CollisionTemplate;
import dev.blijde_broers.misc.math.Line;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.Handler;
import dev.blijde_broers.object.components.ObjectComponent;

public class TextureComponent extends ObjectComponent {

	private Texture texture;
	private Transform offset;

	public TextureComponent(GameObject parent, String img, Transform offset) {
		super(parent);
		texture = Texture.getTexture(img);
		if (offset == null) {
			offset = new Transform(new Vector2(0, 0), parent.getTransform().getDimensions());
		}
		this.offset = offset;
	}

	public TextureComponent(GameObject parent, Texture texture, Transform offset) {
		super(parent);
		this.texture = texture;
		if (offset == null) {
			offset = new Transform(new Vector2(0, 0), parent.getTransform().getDimensions());
		}
		this.offset = offset;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		// Vector2 absPos = new Vector2();
		// long start = System.nanoTime();
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform old = g2D.getTransform();

		// Translates to middle of the window
		// absPos.increment(Game.GAME.getWindow().getWidth() / 2,
		// Game.GAME.getWindow().getHeight() / 2);
		g2D.translate((int) (Game.GAME.getWindow().getWidth() / 2), (int) (Game.GAME.getWindow().getHeight() / 2));

		// Translates to position of the camera
		// absPos.increment(Handler.mainCamera.getTransform().mid.x,
		// Handler.mainCamera.getTransform().mid.y);
		g2D.translate((int) (-Handler.mainCamera.getTransform().mid.x * Handler.mainCamera.zoom),
				(int) (-Handler.mainCamera.getTransform().mid.y * Handler.mainCamera.zoom));

		// Translates to the position of the gameobject multiplied by the camera zoom
		// absPos.increment(parent.getTransform().mid.x *
		// Handler.mainCamera.zoom,
		// parent.getTransform().mid.y * Handler.mainCamera.zoom);
		g2D.translate((int) (parent.getTransform().mid.x * Handler.mainCamera.zoom),
				(int) (parent.getTransform().mid.y * Handler.mainCamera.zoom));

		// Rotates the graphics to the gameobject's rotation
		g2D.rotate(parent.getTransform().getRotation());

		// Translates to the offset's position
		// absPos.increment(offset.mid.x, offset.mid.y);
		g2D.translate((int) (offset.mid.x * Handler.mainCamera.zoom), (int) (offset.mid.y * Handler.mainCamera.zoom));

		// Rotates the graphics to the offset's rotation
		g2D.rotate(offset.getRotation());
		// Scales the graphics object to MainCamera.zoom
		g2D.drawImage(texture.img, (int) -(offset.getDimensions().x / 2 * Handler.mainCamera.zoom),
				(int) -(offset.getDimensions().y / 2 * Handler.mainCamera.zoom),
				(int) (offset.getDimensions().x * Handler.mainCamera.zoom),
				(int) (offset.getDimensions().y * Handler.mainCamera.zoom), null);
		g2D.setTransform(old);
		// System.out.println(parent.toString() + ": TextureComponent.render(Graphics
		// g): " + ((System.nanoTime() - start) / 1000));
		// System.out.println(absPos.toString() + "|| Cam: " +
		// Handler.mainCamera.getTransform().mid.toString() + "|| Parent: " +
		// parent.getTransform().mid.toString() + "|| Parent.size: " +
		// parent.getTransform().getDimensions().toString());
	}

	public static void renderCollisionTemplate(Graphics g, CollisionComponent cc, Color c) {
		g.setColor(c);
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform old = g2D.getTransform();
		CollisionTemplate ct = cc.getCollisionTemplate();
		g2D.translate((int) (Game.GAME.getWindow().getWidth() / 2), (int) (Game.GAME.getWindow().getHeight() / 2));
		g2D.translate((int) (-Handler.mainCamera.getTransform().mid.x * Handler.mainCamera.zoom),
				(int) (-Handler.mainCamera.getTransform().mid.y * Handler.mainCamera.zoom));
		for (int i = 0; i < ct.getRibs().length; i++) {
			Line rib = ct.getRibs()[i];
			if (rib != null) {
				g2D.drawLine((int) (Handler.mainCamera.zoom * rib.v1.x), (int) (Handler.mainCamera.zoom * rib.v1.y),
						(int) (Handler.mainCamera.zoom * rib.v2.x), (int) (Handler.mainCamera.zoom * rib.v2.y));
			}
		}
		g2D.setColor(Color.white);
		g2D.drawArc((int) ((ct.getTransform().mid.x - (ct.getRadius())) * Handler.mainCamera.zoom),
				(int) ((ct.getTransform().mid.y - (ct.getRadius())) * Handler.mainCamera.zoom),
				(int) ((ct.getRadius() * 2) * Handler.mainCamera.zoom),
				(int) ((ct.getRadius() * 2) * Handler.mainCamera.zoom), 0, 360);
		g2D.setTransform(old);
	}

	public static void renderImage(Graphics g, Image img, Transform transform) {
		// Vector2 absPos = new Vector2();
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform old = g2D.getTransform();

		// Translates to middle of the window
		g2D.translate((int) (Game.GAME.getWindow().getWidth() / 2), (int) (Game.GAME.getWindow().getHeight() / 2));

		// Translates to position of the camera
		g2D.translate((int) (-Handler.mainCamera.getTransform().mid.x * Handler.mainCamera.zoom),
				(int) (-Handler.mainCamera.getTransform().mid.y * Handler.mainCamera.zoom));

		// Translates to the transform's position
		g2D.translate((int) (transform.mid.x * Handler.mainCamera.zoom),
				(int) (transform.mid.y * Handler.mainCamera.zoom));

		// Rotates the graphics to the offset's rotation
		g2D.rotate(transform.getRotation());
		g2D.drawImage(img, (int) -(transform.getDimensions().x / 2 * Handler.mainCamera.zoom),
				(int) -(transform.getDimensions().y / 2 * Handler.mainCamera.zoom),
				(int) (transform.getDimensions().x * Handler.mainCamera.zoom),
				(int) (transform.getDimensions().y * Handler.mainCamera.zoom), null);
		g2D.setTransform(old);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Transform getOffset() {
		return offset;
	}

	public void setOffset(Transform offset) {
		this.offset = offset;
	}

}
