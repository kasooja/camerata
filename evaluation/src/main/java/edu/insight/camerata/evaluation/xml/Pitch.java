package edu.insight.camerata.evaluation.xml;

public class Pitch {

	public String step;
	public String octave;
	public String alter;
	public boolean rest = false;

	@Override
	public String toString() {
		if(!rest)
			return "\tNote's Pitch:\t\t" + "Step =\t" + step + "\t" + "Octave =\t" + octave + "\t" + "Alter =\t" + alter;
		else return "\tNote's Pitch:\t\t" + "rest";
	}
}
