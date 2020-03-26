package dev.blijde_broers.neuralNetwork.NEAT;

public class NeuronGene {
	
	enum NeuronType {
		Input,
		Hidden,
		Output;
	}
	
	private int id;
	private NeuronType type;

	public NeuronGene(int id, NeuronType type) {
		this.id = id;
		this.type = type;
	}
	
	public String toString() {
		String out = type.toString() + " Neuron: id=" + id + ";";
		return out;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NeuronType getType() {
		return type;
	}

	public void setType(NeuronType type) {
		this.type = type;
	}

}
