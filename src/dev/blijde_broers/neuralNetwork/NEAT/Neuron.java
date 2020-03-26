package dev.blijde_broers.neuralNetwork.NEAT;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<Synapse> in = new ArrayList<Synapse>(), out = new ArrayList<Synapse>();
	protected double activation;
	protected double expectedValue;
	protected boolean input = false;

//	public Neuron(Synapse[] in, double bias, Synapse[] out) {
//		this.in = in;
//		this.bias = bias;
//		this.out = out;
//	}
	
	public Neuron() {
		
	}

	public double weightedSumWithActivation() {
		double weightedSum = 0;
		for (Synapse in : in) {
			weightedSum += in.getWeightedOut();
		}
		activation = sigmoid(weightedSum);
		return activation;
	}
	
	public double calcRecursively() {
		if(input) return activation;
		if(in.isEmpty()) return 0;
		double weightedSum = 0;
		for (Synapse in : in) {
			weightedSum += in.calcRecursively();
		}
		activation = sigmoid(weightedSum);
		return activation;
	}
	
	public static double sigmoid(double in) {
		return 1 / (1 + (Math.pow(Math.E, -4.9 * in)));
	}

}
