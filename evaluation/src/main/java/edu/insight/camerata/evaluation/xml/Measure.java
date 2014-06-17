package edu.insight.camerata.evaluation.xml;

import java.util.LinkedHashMap;
import java.util.Map;

public class Measure {	

	public Map<Integer, Note> notes;
	public int measureNumber;
	public MeasureAttributes attributes;

	public Measure() {
		notes = new LinkedHashMap<Integer, Note>();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\tMeasure:\n");
		buffer.append("\tMeasure Number =\t" +  measureNumber + "\n");
		buffer.append("\tMeasure Attributes: \t" + attributes + "\n");
		for(Integer noteNumber : notes.keySet()) {
			buffer.append("Note: " + "\n");			
			buffer.append("Note Number:\t" + noteNumber + "\n");
			buffer.append(notes.get(noteNumber) + "\n");			
		}
		return buffer.toString().trim();
	}

}
