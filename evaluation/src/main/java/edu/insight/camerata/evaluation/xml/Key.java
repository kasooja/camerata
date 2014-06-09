package edu.insight.camerata.evaluation.xml;

public class Key {

	public double fifths;
	public String mode;
	
	@Override
	public String toString() {
		return "\tKey:\t\t" + fifths + "\t" + mode;
	}
}
