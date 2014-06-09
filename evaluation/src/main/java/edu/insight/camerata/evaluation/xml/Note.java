package edu.insight.camerata.evaluation.xml;

public class Note {

	public Pitch pitch;
	public double duration;
	public String type;
	public String voice;
	public String stem;

	@Override
	public String toString() {
		return "\tNote:\t\t" + "Duration:\t" + duration + "\t" + "Type:\t" + type + "\t" + 
				"Voice:\t" + voice + "\t" + "Stem:\t" + stem + "\n" + pitch;
	}

}
