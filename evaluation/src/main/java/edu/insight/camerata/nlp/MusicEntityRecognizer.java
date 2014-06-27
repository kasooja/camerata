package edu.insight.camerata.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.insight.camerata.evaluation.utils.BasicFileTools;
import edu.insight.camerata.evaluation.xml.Pitch;

public class MusicEntityRecognizer {

	public static String vocabFilePath = "src/main/resources/MusicVocabularyTest";
	public static Map<String, Set<String>> classWithVocab = new HashMap<String, Set<String>>();
	public static String noteOctaveRegex = "(A|B|C|D|E|F|G)(.d*)";
	public static String noteAllRegex = "(A|B|C|D|E|F|G|rest)(#|b?)(\\d)?\\s*(sharp|natural|flat)?";
	public static String noteTimeRegex = "(dotted)?\\s*(maxima|octuple whole|longa|quadruple whole|breve|double whole|semibreve|whole|minim|half|crotchet|quarter|quaver|eighth|semiquaver|sixteenth|demisemiquaver|thirty-second|hemidemisemiquaver|sixty-fourth|semihemidemisemiquaver|hundred twenty-eighth|demisemihemidemisemiquaver|two hundred fifty-sixth)\\s*(note)?";
	public static Pattern noteAllPattern = Pattern.compile(noteAllRegex);
	public static Pattern noteOctavePattern = Pattern.compile(noteOctaveRegex);

	static {
		BufferedReader buffReader = BasicFileTools.getBufferedReaderFile(vocabFilePath);
		String line = null;
		String label = null;
		try {
			while((line = buffReader.readLine()) != null) {
				if(line.contains(":")) {
					line = line.replace(":", " ").trim();
					label = PhraseElementTypes.getEnum(line).toString();				
					if(classWithVocab.get(PhraseElementTypes.getEnum(line)) == null) 
						classWithVocab.put(label, new HashSet<String>());
				} else if(!line.trim().equalsIgnoreCase("")) {
					//					classWithVocab.get(PhraseElementTypes.getEnum(label)).add(line.toLowerCase().trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Set<String>> getMusicalPhraseElements(String text) {
		Map<String, Set<String>> entities = new HashMap<String, Set<String>>();
		System.out.println(text);
		for(String label : classWithVocab.keySet()){
			Set<String> vocab = classWithVocab.get(label);
			for(String phraseElement : vocab) {
				Pattern pattern = Pattern.compile("\\s*" + phraseElement + "\\s+", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(text);
				if(matcher.find()){
					if(entities.get(PhraseElementTypes.getEnum(label)) == null) 
						entities.put(label, new HashSet<String>());
					else 
						entities.get(PhraseElementTypes.getEnum(label)).add(matcher.group(1).trim());
				}

			}
		}	
		return entities;
	}

	public static Pitch recognizePitchEntities(String query) {
		Matcher matcher = noteOctavePattern.matcher(query);
		Pitch pitch = new Pitch();

		while(matcher.find()) {
			String step = matcher.group(1).trim();
			String octave = matcher.group(2).trim();		
			pitch.octave = octave;
			pitch.step = step;
			pitch.entityRecognized = true;
		}			

		return pitch;
	}


	public static List<Pitch> recognizePitches(String query) {
		Matcher matcher = noteAllPattern.matcher(query);
		List<Pitch> pitches = new ArrayList<Pitch>();
		while(matcher.find()) {
			Pitch pitch = new Pitch();
			String step = null;
			String alter1 = null;
			String octave = null;
			String alter2 = null;
			if(matcher.group(1) != null){
				if(matcher.group().equalsIgnoreCase("rest"))
					pitch.rest = true;
				else
					step = matcher.group(1).trim();
			}
			if(matcher.group(2) != null)				
				alter1 = matcher.group(2).trim();
			if(matcher.group(3) != null)				
				octave = matcher.group(3).trim();
			if(matcher.group(4) != null)				
				alter2 = matcher.group(4).trim();					
			pitch.octave = octave;
			pitch.step = step;
			pitch.entityRecognized = true;
			if(alter1 != null) {
				if(alter1.equalsIgnoreCase("#"))
					pitch.alter = "1";
				else if(alter1.equalsIgnoreCase("b"))
					pitch.alter = "-1";
			}
			if(alter2 != null){
				if(alter2.equalsIgnoreCase("sharp")){
					pitch.alter = "1";
				}
				else if(alter2.equalsIgnoreCase("flat")){
					pitch.alter = "-1";
				}
				else if(alter2.equalsIgnoreCase("natural")){
					pitch.alter = null;
				}
			}			
			pitch.entityRecognized = true;
			pitches.add(pitch);
		}			
		return pitches;
	}


	public static void main(String[] args) {
		String questionText = "	1. G5, B, E5, B4, A sharp, A#, A natural, A flat, G#5, F2 sharp, B";
		List<Pitch> pitches = recognizePitches(questionText);

		for(Pitch pitch : pitches)
			System.out.println(pitch);

		//	PitchExtractor.getPitch(music, step, octave, alter, rest);		
		//Map<String, Set<String>> musicalPhraseElements = getMusicalPhraseElements(questionText);

	}

}
