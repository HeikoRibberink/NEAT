package dev.blijde_broers.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import dev.blijde_broers.input.MouseManager;

public class Button {
	
	public String text;
	public Image img;
	public Rectangle clickBounds;

	public Button(Rectangle clickBounds, Image img) {
		
	}
	
	public Button(Rectangle clickBounds, String text) {
		this.clickBounds = clickBounds;
		this.text = text;
	}
	
	
	public boolean clicked() {
		return clickBounds.contains(MouseManager.getMousePosition()) && MouseManager.mouseDown;
	}
	
	
	public void render(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(clickBounds.x - 2, clickBounds.y - 2, clickBounds.width + 4, clickBounds.height + 4);
		g.setColor(Color.WHITE);
		g.fillRect(clickBounds.x, clickBounds.y, clickBounds.width, clickBounds.height);
		g.setColor(Color.BLACK);
		g.drawString(text, (clickBounds.x + clickBounds.width / 2) - text.length() * 3, (clickBounds.y + clickBounds.height / 2));
	}

}
