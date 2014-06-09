package edu.insight.camerata.evaluation.xml;

public class Time {

	public double beats;
	public double beatType;
	
	@Override
	public String toString() {
		return " Time: " + "\tBeats: " + beats + "\tBeatType: " + beatType;
	}
	
}
