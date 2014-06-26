package edu.insight.camerata.extractors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.insight.camerata.evaluation.run.Pair;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.MeasureAttributes;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.Pitch;


public class PitchExtractor {

	public static Set<Measure> getPitch(Music music, Pitch pitch) {
		MeasureAttributes ma = null;
		String step = pitch.step;
		String octave = pitch.octave; 
		String alter = pitch.alter; 
		boolean rest = pitch.rest;

		boolean stepEqual = false;		
		boolean octaveEqual = false;
		boolean alterEqual = false;
		boolean restEqual = false;

		if(step == null)
			stepEqual = true;
		if(octave == null)
			octaveEqual = true;
		if(alter == null)
			alterEqual = true;
		if(rest == false)
			restEqual = true;		

		Set<Measure> pitchMeasures = new HashSet<Measure>();

		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);
			for (String measureNumber : part.measures.keySet()) {
				Measure measure = part.measures.get(measureNumber);
				if(measure.attributes != null)
					ma = measure.attributes;
				for (Integer noteNumber: measure.notes.keySet()){
					Note note = measure.notes.get(noteNumber);					
					if(step != null && note.pitch.step != null)
						stepEqual = note.pitch.step.equalsIgnoreCase(step);
					if(alter != null && note.pitch.alter != null)
						alterEqual = note.pitch.alter.equalsIgnoreCase(alter);
					if(octave != null && note.pitch.octave != null)
						octaveEqual = note.pitch.octave.equalsIgnoreCase(octave);
					restEqual = rest == note.pitch.rest;
					if(stepEqual && octaveEqual && alterEqual && restEqual){
						measure.computedAttributes = ma;
						Pair answerPair = new Pair();
						answerPair.startNote = note;
						answerPair.endNote = note;						
						measure.answerPairs.add(answerPair);
						pitchMeasures.add(measure);					
					}
				}
			}
		}
		return pitchMeasures;	
	}

	public static List<Measure> getPitchSin(Music music, Pitch pitch){
		String step = pitch.step;
		String octave = pitch.octave; 
		String alter = pitch.alter; 
		boolean rest = pitch.rest;

		List<Measure> pitchMeasures = new ArrayList<Measure>();
		List<Pitch> pitchnotes = new ArrayList<Pitch>();

		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);
			for (String measureNumber : part.measures.keySet()) {
				Measure measure = part.measures.get(measureNumber);
				for (Integer noteNumber: measure.notes.keySet()){
					Note note = measure.notes.get(noteNumber);					

					//for questions type : Note Accidental
					if(note.pitch.alter != null){
						if(note.pitch.step.equals(step) && note.pitch.alter.equals(alter)){
							pitchMeasures.add(measure);
							pitchnotes.add(note.pitch);
						}
					}
					//for questions type : Note Octave
					if(note.pitch.octave != null){
						if(note.pitch.step.equals(step) && note.pitch.octave.equals(octave)){
							pitchMeasures.add(measure);
							pitchnotes.add(note.pitch);
						}
					}
					//for question on rest
					if(rest){
						pitchMeasures.add(measure);
						pitchnotes.add(note.pitch);
					}

				}
			}
		}

		System.out.println(pitchnotes.size() + ":" + pitchnotes);
		//System.out.println(pitchMeasures.size() + ":" + pitchMeasures);
		return pitchMeasures;	
	}

}
