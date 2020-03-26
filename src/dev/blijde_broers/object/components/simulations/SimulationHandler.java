package dev.blijde_broers.object.components.simulations;

import dev.blijde_broers.object.GameState;

public abstract class SimulationHandler {
	
	protected final int simsPerTick;
	protected GameState[] states;

	public SimulationHandler(int simsPerTick, GameState[] states) {
		this.simsPerTick = simsPerTick;
		this.states = states;
	}
	
	public SimulationHandler(int simsPerTick, GameState states) {
		this.simsPerTick = simsPerTick;
		this.states = new GameState[] {states};
	}
	
	public abstract void simulate();

	public GameState[] getStates() {
		return states;
	}

	public void setStates(GameState[] states) {
		this.states = states;
	}

	public int getSimsPerTick() {
		return simsPerTick;
	}

}
