package edu.insight.camerata.extractors;
import java.util.ArrayList;
import java.util.List;

import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.Pitch;


public class PitchExtractor {

	public static List<Measure> getPitch(Music music, String step, String octave, String alter, boolean rest){
		List<Measure> pitchMeasures = new ArrayList<Measure>();
		List<Pitch> pitchnotes = new ArrayList<Pitch>();
		for (String key : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(key);
			for (String key1 : part.measures.keySet()) {
				Measure measure = part.measures.get(key1);
				for (Integer key2: measure.notes.keySet()){
					Note note = measure.notes.get(key2);

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
