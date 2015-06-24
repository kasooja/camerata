package edu.insight.camerata.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.insight.camerata.evaluation.utils.BasicFileTools;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.evaluation.xml.Pitch;

public class MusicEntityRecognizer {

	public static String vocabFilePath = "src/main/resources/MusicVocabularyTest";
	public static Map<String, Set<String>> classWithVocab = new HashMap<String, Set<String>>();
	public static String pitchAllRegex = "(A|B|C|D|E|F|G|rest)(#|b?)(\\d)?\\s*(sharp|natural|flat)?";	
	public static String noteTimeRegex = "(dotted)?\\s*(maxima|octuple\\s*whole|longa|quadruple\\s*whole|breve|double\\s*whole|semibreve|whole|minim|half|crotchet|quarter|quaver|eighth|semiquaver|sixteenth|demisemiquaver|thirty[-\\s]second|hemidemisemiquaver|sixty-fourth|semihemidemisemiquaver|hundred\\s*twenty-eighth|demisemihemidemisemiquaver|two hundred fifty-sixth)\\s*(note)?";
	public static String timePitchRegex = "(" + "(dotted)?\\s*(maxima|octuple\\s*whole|longa|quadruple\\s*whole|breve|double\\s*whole|semibreve|whole|minim|half|crotchet|quarter|quaver|eighth|semiquaver|sixteenth|demisemiquaver|thirty[-\\s]second|hemidemisemiquaver|sixty-fourth|semihemidemisemiquaver|hundred\\s*twenty-eighth|demisemihemidemisemiquaver|two hundred fifty-sixth)\\s*(note)?" + ")|(" + 
			"(A|B|C|D|E|F|G|rest|Do|Re|Mi|Fa|Sol|La|Ti|Si)(#|b?)(\\d)?\\s*(sharp|natural|flat)?" + ")";
	public static Pattern noteAllPattern = Pattern.compile(pitchAllRegex);
	public static Pattern noteTimePattern = Pattern.compile(noteTimeRegex);
	public static Pattern timePitchPattern = Pattern.compile(timePitchRegex);
	public static String barRegex = "(bar|bars|measure|measures)\\s*(\\d+)(\\s*-(\\d+))?";
	public static Pattern barPattern = Pattern.compile(barRegex);

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

	public static String removeCapitalizedWordsExceptNotes(String queryString){
		Set<String> instruments = new HashSet<String>();
		instruments.add("Alto");		
		instruments.add("Bass");
		instruments.add("Alt");	
		for(String instrument : instruments) {			
			queryString = queryString.replaceAll(instrument, " ").trim();
		}
		return queryString;
	}

	public static List<Pitch> recognizePitches(String query) {		
		Matcher matcher = noteAllPattern.matcher(removeCapitalizedWordsExceptNotes(query));
		//(A|B|C|D|E|F|G|rest)(#|b?)(\\d)?\\s*(sharp|natural|flat)?
		List<Pitch> pitches = new ArrayList<Pitch>();
		while(matcher.find()) {
			Pitch pitch = new Pitch();
			pitch.matchedString = matcher.group().trim();
			String step = null;
			String alter1 = null;
			String octave = null;
			String alter2 = null;
			if(matcher.group(1) != null){
				if(matcher.group().trim().equalsIgnoreCase("rest"))
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
					pitch.alter = "0";
				}
			}			
			pitch.entityRecognized = true;
			pitches.add(pitch);
		}			
		return pitches;
	}

	public static double distance(String question, String pitch, String noteTime){
		double distance = 0.0;
		double diff = question.indexOf(noteTime) - question.indexOf(pitch);
		if(diff < 0.0)
			diff = diff * -1;
		distance = diff;
		return distance;
	}

	public static String extractStaff(String questionString) {
		if(questionString.toLowerCase().contains("right hand"))
			return "1";
		if(questionString.toLowerCase().contains("left hand"))
			return "2";
		return null;		
	}

	public static String extractClef(String questionString) {		
		if(questionString.toLowerCase().contains("bass clef") || questionString.toLowerCase().contains("F-clef") || questionString.contains("bass"))
			return "F";
		if(questionString.toLowerCase().contains("treble clef") || questionString.toLowerCase().contains("G-clef") || questionString.contains("treble"))
			return "G";
		if(questionString.toLowerCase().contains("alto clef") || questionString.toLowerCase().contains("C-clef") || questionString.contains("alto"))
			return "C";		
		return null;		
	}

	public static String extractClef(String questionString, List<Note> notes) {

		if(questionString.toLowerCase().contains("bass clef") || questionString.toLowerCase().contains("F-clef") || questionString.contains("bass")){
			return "F";
		}
		if(questionString.toLowerCase().contains("treble clef") || questionString.toLowerCase().contains("G-clef") || questionString.contains("treble")){
			return "G";
		}
		if(questionString.toLowerCase().contains("alto clef") || questionString.toLowerCase().contains("C-clef") || questionString.contains("alto")){
			return "C";		
		}
		return null;		
	}


	public static String extractBars(String questionString) {
		String bars = null;
		Matcher matcher = barPattern.matcher(questionString);
		if(matcher.find()){			
			String startBar = matcher.group(2);
			String endBar = null;
			if(matcher.group(4)!=null){
				endBar = matcher.group(4);
			}
			if(endBar!=null){
				bars = startBar + "\t" +  endBar;
			} else {
				bars = startBar + "\t";
			}
		}
		return bars;
	}

	public static String extractInstrument(String questionString) {
		Set<String> instruments = new HashSet<String>();
		instruments.add("viola");instruments.add("piano");instruments.add("Alto");
		instruments.add("Violoncello");instruments.add("Soprano");instruments.add("Tenor");
		instruments.add("Bass");instruments.add("Violin");instruments.add("Guitar");
		instruments.add("Sopran");instruments.add("Alt");instruments.add("Männer");
		instruments.add("Violine 1");instruments.add("Violine 2");instruments.add("Voice");
		instruments.add("Harpsichord"); instruments.add("Solo Violin"); 
		String question = questionString.replaceAll("bass", " ").trim().replaceAll("alto", " ").trim().replaceAll("treble", " ").
				toLowerCase().trim().replaceAll("bass clef" , " ").trim().replaceAll("alto clef", " ").trim();
		String[] tokens = question.split("\\s+");		
		for(String instrument : instruments) {
			for(String token : tokens){
				if(token.equalsIgnoreCase(instrument.toLowerCase()))
					return instrument;
			}
		}
		return null;
	}


	public static Set<Note> extractNotes(String questionText) {
		List<Pitch> pitches = recognizePitches(questionText);
		List<Note> noteTimes = recognizeNoteTimes(questionText);
		Set<Note> finalNotes = new HashSet<Note>();		
		Note finalNote = null;
		StringTokenizer tokenizer = new StringTokenizer(questionText);
		List<String> tokens = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()) 
			tokens.add(tokenizer.nextToken());
		for(Pitch pitch : pitches) {
			boolean	found = false;	
			for(Note note: noteTimes) {
				String noteString = note.matchedString;
				int length = noteString.length();
				//System.out.println(length);
				double distance = distance(questionText, pitch.matchedString, note.matchedString);
				System.out.println(distance);
				if(distance <= length + 4){
					note.pitch = pitch;
					finalNote = note;
					note.entityRecognized = true;
					finalNotes.add(note);
					found = true;
				} 
			}
			if(!found){
				Note note = new Note();
				note.pitch = pitch;
				note.entityRecognized = true;				
				finalNotes.add(note);				
			} else {
				noteTimes.remove(finalNote);
			}
		}
		for(Note note : noteTimes) {
			note.entityRecognized = true;
			finalNotes.add(note);
		}
		return finalNotes;
	}

	public static void main_old(String[] args) {
		//String questionText = "	1. G5, B, E5, B4, A sharp, A#, A natural, A flat, G#5, F2 sharp, B";
		String questionText = "demisemiquaver B flat is followed by crotchet C"; //D# crotchet dotted minim dotted crotchet A sharp";
		//String questionText = "semiquaver G5 is followed by F5"; //D# crotchet dotted minim dotted crotchet A sharp";		
		//String questionText = "G5 is followed by semiquaver F5 sharp";
		//String questionText = "crotchet rest followed by whole note";
		//String questionText = "rest followed by whole note";
		//String questionText = "G5";
		//String questionText = "F natural";
		//String questionText = "crotchet G5 followed by rest";
		List<Pitch> pitches = recognizePitches(questionText);
		List<Note> noteTimes = recognizeNoteTimes(questionText);
		//	List<Note> timesWithPitch = recognizeNoteTimesAndPitches(questionText);
		Set<Note> finalNotes = new HashSet<Note>();		
		Note finalNote = null;
		StringTokenizer tokenizer = new StringTokenizer(questionText);
		List<String> tokens = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()) 
			tokens.add(tokenizer.nextToken());
		for(Pitch pitch : pitches) {
			boolean	found = false;	
			for(Note note: noteTimes) {
				String noteString = note.matchedString;
				int length = noteString.length();
				//System.out.println(length);
				double distance = distance(questionText, pitch.matchedString, note.matchedString);
				System.out.println(distance);
				if(distance <= length + 4){
					note.pitch = pitch;
					finalNote = note;
					finalNotes.add(note);
					found = true;
				} 
			}
			if(!found){
				Note note = new Note();
				note.pitch = pitch;
				finalNotes.add(note);				
			} else {
				noteTimes.remove(finalNote);
			}
		}

		for(Note note : noteTimes)
			finalNotes.add(note);
		for(Note note : finalNotes)
			System.out.println("Note*****: " + note);
	}

	public static void main(String[] args) throws IOException {
		//String questionText = "	1. G5, B, E5, B4, A sharp, A#, A natural, A flat, G#5, F2 sharp, B";
		//	String questionText = "demisemiquaver B flat is followed by crotchet C"; //D# crotchet dotted minim dotted crotchet A sharp";
		//String questionText = "semiquaver G5 is followed by F5"; //D# crotchet dotted minim dotted crotchet A sharp";		
		//String questionText = "G5 is followed by semiquaver F5 sharp";
		//String questionText = "crotchet rest followed by whole note";
		//String questionText = "rest followed by whole note";
		//String questionText = "G5";
		//String questionText = "F natural";
		//String questionText = "crotchet G5 followed by rest";
		String filePath = "src/main/resources/SampleQuestions";
		BufferedReader br = BasicFileTools.getBufferedReaderFile(filePath);
		String line = null;
		while((line = br.readLine())!=null){
			System.out.println("\n" + line);
			List<Note> notes = recognizeNoteTimesAndPitches(line.trim());
			for(Note note : notes)
				System.out.println("Note*****: " + note);	
		}
	}


	public static List<Note> recognizeNoteTimesAndPitches(String questionText){
		String[]  split = questionText.split(",");
		List<Note> notes = new ArrayList<Note>();
		for(String text : split){
			Matcher matcher = timePitchPattern.matcher(removeCapitalizedWordsExceptNotes(text));
			//			boolean pitchDone = false;
			//			boolean timeDone = false;
			Note note = new Note();
			//	System.out.println(questionText);
			//	System.out.println(questionText.length());
			int lastOffset = -1;
			boolean newNote = true;
			while(matcher.find()) {				
				int offset = matcher.end();
				int startOffset = offset - matcher.group().length();
				if(lastOffset!=-1){
					if(startOffset-lastOffset>2){
						note.entityRecognized = true;
						notes.add(note);
						note = new Note();
						newNote = true;
						lastOffset = 0;
					} 
				}
				//	System.out.println(offset);
				//	System.out.println("First");
				String dotted = matcher.group(2);
				String time = matcher.group(3);

				if(dotted != null || time != null){
					if(newNote){
						note.matchedString = matcher.group();
					} else {						
						note.matchedString = note.matchedString + " " + matcher.group();
					}				
					if(dotted != null)
						if(dotted.equalsIgnoreCase("dotted"))
							note.dot = true;
					//(dotted)?\\s*(maxima|octuple whole|longa|quadruple whole|breve|double whole|semibreve|whole|minim|half|crotchet|quarter|quaver|eighth|semiquaver|sixteenth|demisemiquaver|thirty-second|hemidemisemiquaver|sixty-fourth|semihemidemisemiquaver|hundred twenty-eighth|demisemihemidemisemiquaver|two hundred fifty-sixth)\\s*(note)?
					if(time!=null){
						if(time.equalsIgnoreCase("maxima") || time.equalsIgnoreCase("octuple whole"))
							note.type = "maxima";
						else if(time.equalsIgnoreCase("longa") || time.equalsIgnoreCase("quadruple whole"))
							note.type = "long";
						else if(time.equalsIgnoreCase("breve") || time.equalsIgnoreCase("double whole"))
							note.type = "breve";
						else if(time.equalsIgnoreCase("semibreve") || time.equalsIgnoreCase("whole"))
							note.type = "whole";
						else if(time.equalsIgnoreCase("minim") || time.equalsIgnoreCase("half"))
							note.type = "half";
						else if(time.equalsIgnoreCase("crotchet") || time.equalsIgnoreCase("quarter"))
							note.type = "quarter";
						else if(time.equalsIgnoreCase("quaver") || time.equalsIgnoreCase("eighth"))
							note.type = "eighth";
						else if(time.equalsIgnoreCase("semiquaver") || time.equalsIgnoreCase("sixteenth"))
							note.type = "16th";
						else if(time.equalsIgnoreCase("demisemiquaver") || time.equalsIgnoreCase("thirty-second"))
							note.type = "32nd";
						else if(time.equalsIgnoreCase("hemidemisemiquaver") || time.equalsIgnoreCase("sixty-fourth"))
							note.type = "64th";
						else if(time.equalsIgnoreCase("semihemidemisemiquaver") || time.equalsIgnoreCase("hundred twenty-eighth"))
							note.type = "128th";
						else if(time.equalsIgnoreCase("demisemihemidemisemiquaver") || time.equalsIgnoreCase("two hundred fifty-sixth"))
							note.type = "256th";
					}
					note.entityRecognized = true;
				} else {
					//List<Pitch> pitches = new ArrayList<Pitch>();
					Pitch pitch = new Pitch();
					pitch.matchedString = matcher.group().trim();
					String step = null;
					String alter1 = null;
					String octave = null;
					String alter2 = null;
					if(matcher.group(6) != null){					
						if(matcher.group().trim().equalsIgnoreCase("rest"))
							pitch.rest = true;
						else{ 
							step = matcher.group(6).trim();
							if(matcher.group().trim().equals("Do")){
								step = "C";	
							}
							if(matcher.group().trim().equals("Re")){
								step = "D";	
							}
							if(matcher.group().trim().equals("Mi")){
								step = "E";
							}
							if(matcher.group().trim().equals("Fa")){
								step = "F";
							}
							if(matcher.group().trim().equals("Sol")){
								step = "G";
							}
							if(matcher.group().trim().equals("La")){
								step = "A";
							}
							if(matcher.group().trim().equals("Ti") || matcher.group().trim().equals("Si")){
								step = "B";
							}
						}


					}
					if(matcher.group(7) != null){// || matcher.group(7).trim().equals("")){
						if(!matcher.group(7).trim().equals(""))
							alter1 = matcher.group(7).trim();
					}

					if(matcher.group(8) != null) 
						octave = matcher.group(8).trim();

					if(matcher.group(9) != null) 	
						alter2 = matcher.group(9).trim();

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
							pitch.alter = "0";
						}
					}			
					pitch.entityRecognized = true;
					//pitches.add(pitch);				
					note.pitch = pitch;
					note.entityRecognized = true;
				}
				lastOffset = offset;			
			}			
			notes.add(note);
		}		
		return notes;
	}

	private static List<Note> recognizeNoteTimes(String questionText) {
		List<Note> notes = new ArrayList<Note>();
		Matcher matcher = noteTimePattern.matcher(questionText);
		while(matcher.find()) {
			String dotted = matcher.group(1);
			String time = matcher.group(2);
			Note note = new Note();
			note.matchedString = matcher.group();
			if(dotted != null)
				if(dotted.equalsIgnoreCase("dotted"))
					note.dot = true;
			//(dotted)?\\s*(maxima|octuple whole|longa|quadruple whole|breve|double whole|semibreve|whole|minim|half|crotchet|quarter|quaver|eighth|semiquaver|sixteenth|demisemiquaver|thirty-second|hemidemisemiquaver|sixty-fourth|semihemidemisemiquaver|hundred twenty-eighth|demisemihemidemisemiquaver|two hundred fifty-sixth)\\s*(note)?
			if(time!=null){
				if(time.equalsIgnoreCase("maxima") || time.equalsIgnoreCase("octuple whole"))
					note.type = "maxima";
				else if(time.equalsIgnoreCase("longa") || time.equalsIgnoreCase("quadruple whole"))
					note.type = "long";
				else if(time.equalsIgnoreCase("breve") || time.equalsIgnoreCase("double whole"))
					note.type = "breve";
				else if(time.equalsIgnoreCase("semibreve") || time.equalsIgnoreCase("whole"))
					note.type = "whole";
				else if(time.equalsIgnoreCase("minim") || time.equalsIgnoreCase("half"))
					note.type = "half";
				else if(time.equalsIgnoreCase("crotchet") || time.equalsIgnoreCase("quarter"))
					note.type = "quarter";
				else if(time.equalsIgnoreCase("quaver") || time.equalsIgnoreCase("eighth"))
					note.type = "eighth";
				else if(time.equalsIgnoreCase("semiquaver") || time.equalsIgnoreCase("sixteenth"))
					note.type = "16th";
				else if(time.equalsIgnoreCase("demisemiquaver") || time.equalsIgnoreCase("thirty-second"))
					note.type = "32nd";
				else if(time.equalsIgnoreCase("hemidemisemiquaver") || time.equalsIgnoreCase("sixty-fourth"))
					note.type = "64th";
				else if(time.equalsIgnoreCase("semihemidemisemiquaver") || time.equalsIgnoreCase("hundred twenty-eighth"))
					note.type = "128th";
				else if(time.equalsIgnoreCase("demisemihemidemisemiquaver") || time.equalsIgnoreCase("two hundred fifty-sixth"))
					note.type = "256th";
			}
			note.entityRecognized = true;		
			notes.add(note);
		}

		return notes;
	}

}
