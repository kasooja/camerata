package edu.insight.camerata.evaluation.xml;

public class Pitch {

	public String step;
	public String octave;
	public String alter;
	public boolean rest = false;

	@Override
	public String toString() {
		if(!rest)
			return "\tPitch:\t\t" + step + "\t" + octave + "\t" + alter;
		else return "\tPitch:\t\t" + "rest";
	}
}
