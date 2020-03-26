package dev.blijde_broers.object.components.simulations.instances;

import dev.blijde_broers.object.GameObject;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.Handler;
import dev.blijde_broers.object.components.ObjectComponent;
import dev.blijde_broers.object.components.simulations.Simulatable;
import dev.blijde_broers.object.components.simulations.SimulationHandler;

public class PhysicsSimulation extends SimulationHandler {
	
	public PhysicsSimulation() {
		super(10, GameState.Game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void simulate() {
		for (int i = 0; i < simsPerTick; i++) {
			for (GameObject o : Handler.objects) {
				for(int j = 0; j < o.getComponentManager().getObjectComponents().size(); j++) {
					ObjectComponent c = o.getComponentManager().getObjectComponents().get(j);
					if(c instanceof Simulatable) {
						Simulatable sim = (Simulatable) c;
						sim.simulateTick(simsPerTick);
					}
				}
				
			}
		}

	}

}
