package edu.insight.camerata.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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

	public static void main(String[] args) {
		String questionText = "G5";
		questionText = questionText.toLowerCase();
	//	PitchExtractor.getPitch(music, step, octave, alter, rest);		
		//Map<String, Set<String>> musicalPhraseElements = getMusicalPhraseElements(questionText);
	}

}
