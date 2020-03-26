package dev.blijde_broers.menu;

import java.awt.Graphics;
import java.awt.Rectangle;

import dev.blijde_broers.main.Game;
import dev.blijde_broers.object.GameState;

public class Menu {
	
	private Button start = new Button(new Rectangle(100, 100, Game.GAME.getWindow().getCanvas().getWidth() - 200, 100), "Start New Game");
	private Button exit = new Button(new Rectangle(100, 300, Game.GAME.getWindow().getCanvas().getWidth() - 200, 100), "Exit");

	public Menu() {
		
	}
	
	public void tick() {
		if(start.clicked()) {
			Game.STATE = GameState.Game;
		}
		if(exit.clicked()) {
			try {
				Game.GAME.stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void render(Graphics g) {
		start.render(g);
		exit.render(g);
	}

}
