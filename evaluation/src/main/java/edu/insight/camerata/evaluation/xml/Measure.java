package edu.insight.camerata.evaluation.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.insight.camerata.evaluation.run.Pair;

public class Measure {	

	public Map<Integer, Note> notes;
	public int measureNumber;
	public MeasureAttributes attributes;
	public MeasureAttributes computedAttributes;
	public List<Pair> answerPairs = new ArrayList<Pair>();
	public String partNumber = null;
	public LinkedHashMap<Double, List<Note>> positionNotes = new LinkedHashMap<Double, List<Note>>();

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
