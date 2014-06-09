package edu.insight.camerata.evaluation.xml;

public class Key {

	public double fifths;
	public String mode;
	
	@Override
	public String toString() {
		return "Key:\t" + "Fifths =\t" + fifths + "\t" + "Mode =\t" + mode;
	}
}
