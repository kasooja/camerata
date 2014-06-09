package edu.insight.camerata.evaluation.xml;

public class ScoreInstrument {

	public String id;
	public String instrumentName;
	
	@Override
	public String toString() {
		return "\tScoreInstrument:\t\t" + id + "\t" + instrumentName;
	}
		
}
