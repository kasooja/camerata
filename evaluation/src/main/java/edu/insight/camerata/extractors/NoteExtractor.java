package edu.insight.camerata.extractors;

import java.util.HashSet;
import java.util.Set;

import edu.insight.camerata.evaluation.run.Pair;
import edu.insight.camerata.evaluation.xml.Clef;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.MeasureAttributes;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.Pitch;

public class NoteExtractor {

	public static Set<Measure> getNote(Music music, Note note, String instrument, String clef) {
		MeasureAttributes ma = null;
		Pitch pitch = note.pitch;
		String step = null;
		String octave = null; 
		String alter = null; 
		
		boolean rest = false;

		if(pitch != null) { 
			step = pitch.step;			
			octave = pitch.octave; 
			alter = pitch.alter; 
			rest = pitch.rest;
		}

		boolean dot = note.dot;
		String type = note.type;
		String staff = note.staff;

		boolean stepEqual = false;		
		boolean octaveEqual = false;
		boolean alterEqual = false;
		boolean restEqual = false;
		boolean dotEqual = false;
		boolean typeEqual = false;
		boolean instrumentEqual = false;
		boolean staffEqual = false;
		boolean clefEqual = false;
		//boolean pitchEqual = false;

		//if(pitch == null)
		//	pitchEqual = true;

		if(step == null)
			stepEqual = true;
		if(octave == null)
			octaveEqual = true;
		if(alter == null)
			alterEqual = true;
		if(rest == false)
			restEqual = true;		
		if(dot == false)
			dotEqual = true;
		if(type == null)
			typeEqual = true;
		if(instrument == null)
			instrumentEqual = true;
		if(staff == null)
			staffEqual = true;
		if(clef == null)
			clefEqual = true;
		

		Set<Measure> noteMeasures = new HashSet<Measure>();
		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);
			if(instrument != null) 
				instrumentEqual = part.scoreInstrument.instrumentName.equalsIgnoreCase(instrument.trim());
			for (String measureNumber : part.measures.keySet()) {				
				Measure measure = part.measures.get(measureNumber);
				if(measure.attributes != null)
					ma = measure.attributes;
				for(Clef clefMa : ma.clefs){
					if(clefMa.sign.equalsIgnoreCase(clef))
						clefEqual = true;
				}
				for (Integer noteNumber: measure.notes.keySet()) {
					Note notee = measure.notes.get(noteNumber);					
					if(step != null && notee.pitch.step != null)
						stepEqual = notee.pitch.step.equalsIgnoreCase(step);
					if(alter != null && notee.pitch.alter != null)
						alterEqual = notee.pitch.alter.equalsIgnoreCase(alter);
					if(octave != null && notee.pitch.octave != null)
						octaveEqual = notee.pitch.octave.equalsIgnoreCase(octave);
					dotEqual = dot == notee.dot;
					restEqual = rest == notee.pitch.rest;
					if(type != null && notee.type != null)
						typeEqual = notee.type.equalsIgnoreCase(type);
					if(staff != null && notee.staff != null)
						staffEqual = notee.staff.equalsIgnoreCase(staff);
					
					if(stepEqual && octaveEqual && alterEqual && restEqual && dotEqual && typeEqual && instrumentEqual && 
							staffEqual && clefEqual) {
						measure.computedAttributes = ma;
						Pair answerPair = new Pair();
						answerPair.startNote = notee;
						answerPair.endNote = notee;						
						measure.answerPairs.add(answerPair);
						noteMeasures.add(measure);					
					}
				}
			}			
		}	
		return noteMeasures;	
	}

}
