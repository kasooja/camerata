package edu.insight.camerata.evaluation.xml;

public class Pitch {

	public String step;
	public String octave;
	public String alter;
	public boolean rest = false;
	public boolean entityRecognized = false;
	public String matchedString;
	

	@Override
	public String toString() {
		if(!rest)
			return "\tNote's Pitch:\t" + "Step = " + step + "\t" + "Octave = " + octave + "\t" + "Alter = " + alter;
		else return "\tNote's Pitch:\t " + "rest";
	}
}
