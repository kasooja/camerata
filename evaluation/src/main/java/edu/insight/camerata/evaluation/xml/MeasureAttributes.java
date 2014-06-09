package edu.insight.camerata.evaluation.xml;

public class MeasureAttributes {
	
	public double divisions;
	public Clef clef;
	public Key key;

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Divisions =\t" + divisions + "\t" + clef + "\t" + key);
		return buffer.toString().trim();
	}

}
