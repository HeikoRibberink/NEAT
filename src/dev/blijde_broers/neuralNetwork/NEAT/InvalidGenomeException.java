package dev.blijde_broers.neuralNetwork.NEAT;

public class InvalidGenomeException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidGenomeException() {
		super();
	}
	
	public InvalidGenomeException(String message) {
		super(message);
	}
}
