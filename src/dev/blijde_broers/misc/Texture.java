package dev.blijde_broers.misc;

import java.awt.Image;
import java.io.IOException;
import java.util.LinkedList;

import dev.blijde_broers.main.ResourceLoader;

public class Texture {
	
	public static Texture dot = new Texture("res\\textures\\dot.png");
	
	public static LinkedList<Texture> textures = new LinkedList<Texture>();
	public Image img;
	public String imagePath;

	public Texture(String imagePath) {
		this.imagePath = imagePath;
		try {
			img = ResourceLoader.loadTexture(imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Texture getTexture(String imagePath) {
		for(Texture t : textures) {
			if(t.imagePath.equals(imagePath)) {
				return t;
			}
		}
		Texture temp = new Texture(imagePath);
		textures.add(temp);
		System.out.println("loaded new texture: " + imagePath + " || Now got " + textures.size() + " in storage");
		return temp;
	}

}
