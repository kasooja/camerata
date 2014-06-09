package edu.insight.camerata.evaluation.xml;

import java.util.LinkedHashMap;
import java.util.Map;

public class Part {

	public String partId;
	public String partName;
	public String partAbbreviation;
	public ScoreInstrument scoreInstrument;
	public MidiInstrument midiInstrument;	
	public Map<String, Measure> measures;

	public Part(){
		measures = new LinkedHashMap<String, Measure>();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\tPart:\t\t");
		buffer.append(partId + "\t" + partName + "\t" + partAbbreviation + "\n");
		buffer.append(scoreInstrument + "\n");
		buffer.append(midiInstrument + "\n");	
		for(String measureNumber : measures.keySet()){
			buffer.append("\n*****************\n");
			buffer.append(measures.get(measureNumber));	
		}
		return buffer.toString().trim();
	}
	
}
