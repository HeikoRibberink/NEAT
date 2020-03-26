package dev.blijde_broers.object;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import dev.blijde_broers.camera.MainCamera;
import dev.blijde_broers.main.Game;
import dev.blijde_broers.menu.Menu;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.object.components.simulations.SimulationHandler;
import dev.blijde_broers.object.instances.foods.Food;

public class Handler {

	public static ArrayList<GameObject> objects = new ArrayList<GameObject>();
	public static ArrayList<Food> foods = new ArrayList<Food>();
	
	public static ArrayList<SimulationHandler> simulations = new ArrayList<SimulationHandler>();

	public static Menu menu = new Menu();

	public static MainCamera mainCamera = new MainCamera(new Transform());

	public void removeAll() {
		while(objects.size() > 0) {
			objects.remove(0);
		}
	}

	public void tick() {
		for (int i = 0; i < objects.size(); i++) {
			if (Arrays.asList(objects.get(i).getStates()).contains(Game.STATE)) {
				objects.get(i).manageTick();
			}
		}
		for (int i = 0; i < simulations.size(); i++) {
			if (Arrays.asList(simulations.get(i).getStates()).contains(Game.STATE)) {
				simulations.get(i).simulate();
			}
		}
		if (Game.STATE == GameState.Menu) {
			menu.tick();
		}
		if (Arrays.asList(MainCamera.STATES).contains(Game.STATE)) {
			mainCamera.manageTick();
			mainCamera.getComponentManager().getRigidBody().simulateTick(1);
		}
	}

	public void render(Graphics g) {
		for (int i = 0; i < foods.size(); i++) {
			if (Arrays.asList(foods.get(i).getStates()).contains(Game.STATE)) {
				foods.get(i).manageRender(g);
			}
		}
		for (int i = 0; i < objects.size(); i++) {
			if (Arrays.asList(objects.get(i).getStates()).contains(Game.STATE)) {
				objects.get(i).manageRender(g);
			}
		}
		
		if (Game.STATE == GameState.Menu) {
			menu.render(g);
		}
	}

}
