package edu.insight.camerata.evaluation.xml;

public class Note {

	public Pitch pitch;
	public double duration;
	public String type;
	public String voice;
	public String stem;
	public String staff;
	public boolean dot = false;
	public boolean entityRecognized = false;
	public String matchedString;

	@Override
	public String toString() {
		return "\tNote:\t" + "Duration: " + duration + "\t" + "Type: " + type + "\t" + 
				"Voice: " + voice + "\t" + "Stem: " + stem + "\n" + pitch;
	}

}
