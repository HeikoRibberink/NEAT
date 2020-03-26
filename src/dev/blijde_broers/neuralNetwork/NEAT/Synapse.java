package dev.blijde_broers.neuralNetwork.NEAT;

import java.io.Serializable;

public class Synapse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Neuron in, out;
	protected int inIndex, outIndex;
	protected double weight;
	protected int timesUsedThisIteration = 0;

//	public Synapse(Neuron in, int inIndex, double weight, Neuron out, int outIndex) {
//		this.in = in;
//		this.inIndex = inIndex;
//		this.weight = weight;
//		this.out = out;
//		this.outIndex = outIndex;
//	}
	
	public Synapse(SynapseGene gene) {
		inIndex = gene.getInputId();
		outIndex = gene.getOutputId();
		weight = gene.getWeight();
	}

	public Synapse() {

	}

	public double getWeightedOut() {
		return in.activation * weight;
	}
	
	public double calcRecursively() {
		if(timesUsedThisIteration > 20) return in.activation * weight;
		timesUsedThisIteration++;
		return in.calcRecursively() * weight;
	}

}
