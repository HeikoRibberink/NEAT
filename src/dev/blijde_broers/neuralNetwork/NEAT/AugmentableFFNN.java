package dev.blijde_broers.neuralNetwork.NEAT;

import java.io.Serializable;
import java.util.ArrayList;

import dev.blijde_broers.neuralNetwork.NeuralNetwork;
import dev.blijde_broers.neuralNetwork.NEAT.NeuronGene.NeuronType;

public class AugmentableFFNN implements NeuralNetwork, Serializable {

	private static final long serialVersionUID = 1L;
	
	public ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	public ArrayList<Synapse> synapses = new ArrayList<Synapse>();
	public Integer[] inputIndices, outputIndices;
	public Genome genome;
	
	public static double mutationMultiplier = .02;
	
	public AugmentableFFNN(int inputs, int outputs) {
		inputIndices = new Integer[inputs];
		outputIndices = new Integer[outputs];
		for(int i = 0; i < inputs; i++) {
			neurons.add(new Neuron());
			inputIndices[i] = neurons.size() - 1;
		}
		for(int i = 0; i < outputs; i++) {
			neurons.add(new Neuron());
			outputIndices[i] = neurons.size() - 1;
		}
	}
	
	public AugmentableFFNN(Genome genome) throws InvalidGenomeException {
		ArrayList<Integer> inputIndices = new ArrayList<Integer>(), outputIndices = new ArrayList<Integer>();
		for(NeuronGene g : genome.getNeurons()) {
			if(g.getType() == NeuronType.Hidden) {
				Neuron n = new Neuron();
				neurons.add(n);
			} else if(g.getType() == NeuronType.Input) {
				inputIndices.add(g.getId());
				Neuron n = new Neuron();
				n.input = true;
				neurons.ensureCapacity(g.getId());
				neurons.add(g.getId(), n);
			} else if(g.getType() == NeuronType.Output) {
				outputIndices.add(g.getId());
				Neuron n = new Neuron();
				neurons.ensureCapacity(g.getId());
				neurons.add(g.getId(), n);
			} else throw new InvalidGenomeException();
		}
		for(SynapseGene g : genome.getSynapses()) {
			Synapse s = new Synapse(g);
			Neuron in = neurons.get(s.inIndex);
			Neuron out = neurons.get(s.outIndex);
			s.in = in;
			s.out = out;
			in.out.add(s);
			out.in.add(s);
			synapses.add(s);
		}
		this.inputIndices = new Integer[inputIndices.size()];
		inputIndices.toArray(this.inputIndices);
		this.outputIndices = new Integer[outputIndices.size()];
		outputIndices.toArray(this.outputIndices);
		this.genome = genome;
	}
	
	@Override
	public double[] calculate(double[] in) throws StackOverflowError {
		for (Synapse s : synapses) {
			s.timesUsedThisIteration = 0;
		}
//		if(Math.random() == -1) throw new StackOverflowError();
		for(int i = 0; i < in.length; i++) {
			neurons.get(inputIndices[i]).activation = in[i];
		}
		double[] out = new double[outputIndices.length];
		for(int i = 0; i < out.length; i++) {
			out[i] = neurons.get(outputIndices[i]).calcRecursively(); 
		}
		return out;
	}

	@Override
	public void train(double[] correctAnswer) {
		
	}

}
