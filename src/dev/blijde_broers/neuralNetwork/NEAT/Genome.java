package dev.blijde_broers.neuralNetwork.NEAT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import dev.blijde_broers.neuralNetwork.NEAT.NeuronGene.NeuronType;

public class Genome implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int innovation = 0;

	private ArrayList<NeuronGene> neurons = new ArrayList<NeuronGene>();
	private ArrayList<SynapseGene> synapses = new ArrayList<SynapseGene>();
	public Integer[] inputIndices, outputIndices;

	public Genome(AugmentableFFNN nn) {
		boolean[] alreadyVisited = new boolean[nn.neurons.size()];
		for (int i = 0; i < alreadyVisited.length; i++) {
			alreadyVisited[i] = false;
		}
		this.inputIndices = nn.inputIndices.clone();
		this.outputIndices = nn.outputIndices.clone();
		for (int i : nn.inputIndices) {
			NeuronGene g = new NeuronGene(i, NeuronType.Input);
			neurons.add(g);
			alreadyVisited[i] = true;
		}
		for (int i : nn.outputIndices) {
			NeuronGene g = new NeuronGene(i, NeuronType.Output);
			neurons.add(g);
			alreadyVisited[i] = true;
		}
		for (int i = 0; i < nn.neurons.size(); i++) {
			if (!alreadyVisited[i]) {
				NeuronGene g = new NeuronGene(i, NeuronType.Hidden);
				neurons.add(g);
				alreadyVisited[i] = true;
			}
		}
		for (int i = 0; i < nn.synapses.size(); i++) {
			Synapse n = nn.synapses.get(i);
			SynapseGene g = new SynapseGene(n.inIndex, n.outIndex, n.weight, true, innovation);
			synapses.add(g);
			innovation++;
		}
	}

	public String toString() {
		String out = "Neurons: \n";
		for (NeuronGene g : neurons) {
			out += g.toString() + "\n";
		}
		out += "Synapses: \n";
		for (SynapseGene g : synapses) {
			out += g.toString() + "\n";
		}
		return out;
	}

	// All mutations

	public void mutateRandom() {
		double mutationMultiplier = AugmentableFFNN.mutationMultiplier;
		for (SynapseGene s : synapses) {
			if(Math.random() <= .8) {
				if(Math.random() <= .1) {
					s.setWeight((Math.random() * 2 - 1) * 2 * mutationMultiplier);
				} else { 
					if (Math.random() >= 0.5) {
						s.setWeight(s.getWeight() * randomDouble(1, 2) * mutationMultiplier);
					} else {
						s.setWeight(s.getWeight() / (randomDouble(1, 2) * mutationMultiplier));
					}
				}
			}
		}
		if(Math.random() < .03) {
			addNodeBetweenLink();
		}
		if(Math.random() < .05) {
			addLink();
		}
	}


	private boolean addNodeBetweenLink() {
		if (synapses.size() < 1)
			return false;
		SynapseGene g = synapses.get(randomInt(0, synapses.size()));
		g.setEnabled(false);
		int startNodeIndex = g.getInputId();
		int endNodeIndex = g.getOutputId();
		int newNodeIndex = neurons.size();
		NeuronGene newNode = new NeuronGene(newNodeIndex, NeuronType.Hidden);
		SynapseGene startLink = new SynapseGene(startNodeIndex, newNodeIndex, 1, true, innovation);
		innovation++;
		SynapseGene endLink = new SynapseGene(newNodeIndex, endNodeIndex, g.getWeight(), true, innovation);
		innovation++;
		neurons.add(newNode);
		synapses.add(startLink);
		synapses.add(endLink);
		return true;
	}

	private boolean addLink() {
		double mutationMultiplier = AugmentableFFNN.mutationMultiplier;
		NeuronGene in, out;
		do {
			in = neurons.get(randomInt(0, neurons.size()));
			out = neurons.get(randomInt(0, neurons.size()));
		} while (in.getId() == out.getId());

		boolean reversed = false;
		if (in.getType() == NeuronType.Hidden && out.getType() == NeuronType.Input)
			reversed = true;
		else if (in.getType() == NeuronType.Output && out.getType() == NeuronType.Hidden)
			reversed = true;
		else if (in.getType() == NeuronType.Output && out.getType() == NeuronType.Input)
			reversed = true;
//		System.out.println(in.getType().toString() + in.getId() + ", " + out.getType() + out.getId() + ": " + reversed);

		for (SynapseGene s : synapses) {
			if (s.getInputId() == in.getId() && s.getOutputId() == out.getId()) {
				return false;
			} else if (s.getInputId() == out.getId() && s.getOutputId() == in.getId()) {
				return false;
			}

		}
		SynapseGene s;
		if (reversed) {
			s = new SynapseGene(out.getId(), in.getId(), (Math.random() * 2 * - 1) * mutationMultiplier, true,
					innovation);
		} else {
			s = new SynapseGene(in.getId(), out.getId(), (Math.random() * 2 * - 1) * mutationMultiplier, true,
					innovation);
		}
		innovation++;
		synapses.add(s);
		return true;
	}

	private boolean mutateEnableDisable() {
		if (synapses.size() < 1)
			return false;
		SynapseGene s = synapses.get(randomInt(0, synapses.size()));
		s.setEnabled(s.isEnabled() ? false : true);
		return true;
	}

	// Some extra math operations

	public ArrayList<NeuronGene> getNeurons() {
		return neurons;
	}

	public void setNeurons(ArrayList<NeuronGene> neurons) {
		this.neurons = neurons;
	}

	public ArrayList<SynapseGene> getSynapses() {
		return synapses;
	}

	public void setSynapses(ArrayList<SynapseGene> synapses) {
		this.synapses = synapses;
	}

	public static int getInnovation() {
		return innovation;
	}
	
	public static void resetInnovation() {
		innovation = 0;
	}

	private double randomDouble(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	private int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	public static Genome load(String path) throws IOException, ClassNotFoundException, InvalidGenomeException {
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);

		Object object = ois.readObject();
		if (!(object instanceof Genome)) {
			ois.close();
			fis.close();
			throw new InvalidGenomeException("File does not contain neural network.");
		}

		ois.close();
		fis.close();
		return (Genome) object;
	}

}
