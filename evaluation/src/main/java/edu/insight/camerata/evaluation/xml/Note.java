package edu.insight.camerata.evaluation.xml;

public class Note {

	public Pitch pitch;
	public double duration;
	public String type;
	public String voice;
	public String stem;
	public String staff;
	public boolean backUp = false;
	public boolean forward = false;
	public boolean dot = false;
	public boolean entityRecognized = false;
	public boolean chord = false;
	public String matchedString;
	public double startPosition = -1;
	public double questionStdDuration = -1;
	public double endPosition = -1;
	public Clef clef;
	
	public Measure measure;
	
	

	@Override
	public String toString() {
		return "\tNote:\t" + "Duration: " + duration + "\t" + "Type: " + type + "\t" + 
				"Voice: " + voice + "\t" + "Stem: " + stem + "\t" + "\n" + "Staff: " + staff + "\t" + "Dot: "  + dot + "\n" + pitch;
	}

}
