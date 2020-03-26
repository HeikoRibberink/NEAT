package dev.blijde_broers.neuralNetwork.NEAT;

public class SynapseGene {
	
	private int inputId, outputId;
	private double weight;
	private boolean enabled;
	private int innovation;

	public SynapseGene(int inputId, int outputId, double weight, boolean enabled, int innovation) {
		this.inputId = inputId;
		this.outputId = outputId;
		this.weight = weight;
		this.enabled = enabled;
		this.innovation = innovation;
	}
	
	public String toString() {
		String out = "Synapse: inputId=" + inputId + "; outputId=" + outputId + "; weight=" + weight + "; enabled=" + enabled + "; innovation=" + innovation + ";";
		return out;
	}

	public int getInputId() {
		return inputId;
	}

	public void setInputId(int inputId) {
		this.inputId = inputId;
	}

	public int getOutputId() {
		return outputId;
	}

	public void setOutputId(int outputId) {
		this.outputId = outputId;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getInnovation() {
		return innovation;
	}

	public void setInnovation(int innovation) {
		this.innovation = innovation;
	}
	
	

}
