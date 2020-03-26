package dev.blijde_broers.misc.math;

public class ExtendedMath {

	public static double random(double min, double max) {
		double out = Math.random();
		out = min + (out * (max - min));
		return out;
	}
	
	public static double random(double max) {
		return Math.random() * max;
	}
	
}
