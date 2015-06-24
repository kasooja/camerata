package edu.insight.camerata.evaluation.xml;

import java.util.ArrayList;
import java.util.List;

public class MeasureAttributes {

	public double divisions;
	public List<Clef> clefs;
	public Key key;
	public Time time;
	public double staves = -1;

	public MeasureAttributes() {
		clefs = new ArrayList<Clef>();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(Clef clef : clefs)
			buffer.append("Divisions = " + divisions + "\t" + clef + "\t" + key + "\t" + time + "\t" +  "Staves = " + staves + "\n");
		return buffer.toString().trim();
	}
	
}
