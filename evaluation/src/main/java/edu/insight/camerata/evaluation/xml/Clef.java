package edu.insight.camerata.evaluation.xml;

public class Clef {

	public int clefNumber;
	public String line;
	public String sign;
	
	@Override
	public String toString() {	
		return "Clef: " + "Line = " + line + "\t" + "Sign = " + sign;
	}
		
}
