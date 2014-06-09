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
		buffer.append("\tMeasure:\t\t");
		buffer.append(measureNumber + "\n");
		buffer.append(attributes + "\n");
		for(Integer noteNumber : notes.keySet()){
			buffer.append("Note Number:\t" + noteNumber + "\n");
			buffer.append(notes.get(noteNumber) + "\n");			
		}
		return buffer.toString().trim();
	}

}
