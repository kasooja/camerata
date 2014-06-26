package edu.insight.camerata.evaluation.run;

import edu.insight.camerata.evaluation.xml.Note;

public class Pair {

	public Note startNote;
	public Note endNote;
	
	@Override
	public boolean equals(Object obj) {
		Pair pair = (Pair) obj;
		if(startNote == pair.startNote && endNote == pair.endNote)
			return true;
		return false;
	}

}
