package edu.insight.camerata.evaluation.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.question.xml.Question;
import edu.insight.camerata.evaluation.question.xml.QuestionsXmlHandler;
import edu.insight.camerata.evaluation.question.xml.QuestionsXmlReader;
import edu.insight.camerata.evaluation.utils.BasicFileTools;
import edu.insight.camerata.evaluation.xml.Clef;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.MeasureAttributes;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.MusicReader;
import edu.insight.camerata.evaluation.xml.MusicXmlHandler;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.extractors.MeasureExtractor;
import edu.insight.camerata.nlp.MusicEntityRecognizer;

public class SequenceRunWithOffset {

	public static String getCompleteAnswer(Measure m, Music music1, String divisionValue, Pair answerPair) {
		int start_beats = 2;
		int start_beat_type = 2;
		int end_beats = 2;
		int end_beat_type = 2;
		double start_divisions = Double.parseDouble(divisionValue.trim());
		double end_divisions = Double.parseDouble(divisionValue.trim());
		int start_bar = m.measureNumber;
		int start_offset = 1;
		int end_bar = m.measureNumber;
		int end_offset = 1;

		double divisions = m.computedAttributes.divisions;

		double durationCounter = divisions / start_divisions;

		double offset = 0;
		///double lastDuration = 0.0;
		for(Integer noteNumber : m.notes.keySet()) {			
			Note note = m.notes.get(noteNumber);
			double noteDuration = note.duration;	
			if(note.backUp){
				offset = offset - noteDuration / durationCounter;
			} else if(note.chord) {
				//offset = offset - lastDuration;
			} else {
				offset = offset + noteDuration / durationCounter;
			}
			if(m.answerPairs.contains(answerPair)) {
				if(note == answerPair.startNote) {
					if(note.chord){
						//offset = offset - lastDuration;
						start_offset = (int) (offset - (noteDuration / durationCounter)  + 1.0) ;
					} else {
						start_offset = (int) (offset - (noteDuration / durationCounter)  + 1.0) ;
					}
				}
				if(note == answerPair.endNote) 
					end_offset = (int) offset;				
			}
			//	lastDuration = noteDuration/durationCounter;				

		}

		StringBuffer answerString = new StringBuffer();

		if(m.computedAttributes.time!=null){		
			start_beats = (int) m.computedAttributes.time.beats;
			start_beat_type = (int) m.computedAttributes.time.beatType;
			end_beats = (int) m.computedAttributes.time.beats;
			end_beat_type = (int) m.computedAttributes.time.beatType;

			//			<passage end_bar="2" end_beat_type="4" end_beats="4"
			//					end_divisions="1" end_offset="3" start_bar="2" start_beat_type="4"
			//					start_beats="4" start_divisions="1" start_offset="1" />
			//				

			answerString.append("<passage ");
			answerString.append("end_bar=" + "\"" + end_bar + "\" " + "end_beat_type=" + "\"" + end_beat_type  + "\" " + "end_beats=" + "\"" + end_beats + "\"\n");
			answerString.append("end_divisions=" + "\"" + (int) end_divisions + "\" ");
			answerString.append("end_offset=" + "\"" + end_offset + "\" ");
			answerString.append("start_bar=" + "\"" + start_bar + "\" " + "start_beat_type=" + "\"" + start_beat_type + "\"\n");
			answerString.append("start_beats=" + "\"" + start_beats + "\" " + "start_divisions=" + "\"" + (int) start_divisions + "\" ");
			answerString.append("start_offset=" + "\"" + start_offset + "\" />\n");	
		}
		return answerString.toString().trim();
	}

	public static String getCompleteAnswerForQuestionsWithMoreNotes(Pair pair, Music music, String divisionValue) {
		int start_beats = 2;
		int start_beat_type = 2;
		int end_beats = 2;
		int end_beat_type = 2;
		double start_divisions = Double.parseDouble(divisionValue.trim());
		double end_divisions = Double.parseDouble(divisionValue.trim());
		int start_bar = pair.startNote.measure.measureNumber;
		int start_offset = 1;
		int end_bar = pair.startNote.measure.measureNumber;
		int end_offset = 1;

		double offset = 0;

		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);

			for (String measureNumber : part.measures.keySet()) {
				if(start_bar == Integer.parseInt(measureNumber.trim())){
					Measure measure = part.measures.get(measureNumber);
					if(measure.partNumber!=null){
						if(measure.partNumber.equalsIgnoreCase(pair.startNote.measure.partNumber)	){
							double divisions = measure.computedAttributes.divisions;
							double durationCounter = divisions / start_divisions;
							for(Integer noteNumber : measure.notes.keySet()) {			
								Note note = measure.notes.get(noteNumber);
								double noteDuration = note.duration;	
								if(note.backUp){
									offset = offset - noteDuration / durationCounter;
								} else if(note.chord) {
								} else {
									offset = offset + noteDuration / durationCounter;
								}
								if(note == pair.startNote) {
									if(note.chord){
										start_offset = (int) (offset - (noteDuration / durationCounter)  + 1.0) ;
									} else {
										start_offset = (int) (offset - (noteDuration / durationCounter)  + 1.0) ;
									}
								}			
							}
						}
					}
				}
				offset = 0;
				if(end_bar == Integer.parseInt(measureNumber.trim())){				
					Measure measure = part.measures.get(measureNumber);
					if(measure.partNumber!=null){
						if(measure.partNumber.equalsIgnoreCase(pair.endNote.measure.partNumber)){
							double divisions = measure.computedAttributes.divisions;
							double durationCounter = divisions / start_divisions;
							for(Integer noteNumber : measure.notes.keySet()) {			
								Note note = measure.notes.get(noteNumber);
								double noteDuration = note.duration;	
								if(note.backUp){
									offset = offset - noteDuration / durationCounter;
								} else if(note.chord) {
								} else {
									offset = offset + noteDuration / durationCounter;
								}				
								if(note == pair.endNote) 
									end_offset = (int) offset;
							}
						}
					}
				}
			}
		}

		StringBuffer answerString = new StringBuffer();

		if(pair.startNote.measure.computedAttributes.time!=null || pair.endNote.measure.computedAttributes.time!=null){
			Measure measure = pair.startNote.measure;
			if(pair.startNote.measure.computedAttributes.time==null){
				measure = pair.endNote.measure;				
			}

			start_beats = (int) measure.computedAttributes.time.beats;
			start_beat_type = (int) measure.computedAttributes.time.beatType;
			end_beats = (int) measure.computedAttributes.time.beats;
			end_beat_type = (int) measure.computedAttributes.time.beatType;

			//			<passage end_bar="2" end_beat_type="4" end_beats="4"
			//					end_divisions="1" end_offset="3" start_bar="2" start_beat_type="4"
			//					start_beats="4" start_divisions="1" start_offset="1" />
			//				

			answerString.append("<passage ");
			answerString.append("end_bar=" + "\"" + end_bar + "\" " + "end_beat_type=" + "\"" + end_beat_type  + "\" " + "end_beats=" + "\"" + end_beats + "\"\n");
			answerString.append("end_divisions=" + "\"" + (int) end_divisions + "\" ");
			answerString.append("end_offset=" + "\"" + end_offset + "\" ");
			answerString.append("start_bar=" + "\"" + start_bar + "\" " + "start_beat_type=" + "\"" + start_beat_type + "\"\n");
			answerString.append("start_beats=" + "\"" + start_beats + "\" " + "start_divisions=" + "\"" + (int) start_divisions + "\" ");
			answerString.append("start_offset=" + "\"" + start_offset + "\" />\n");	
		}
		return answerString.toString().trim();
	}


	public static String getCompleteAnswerWithNotes(Pair pair, Music music, String divisionValue) {
		int start_beats = 2;
		int start_beat_type = 2;
		int end_beats = 2;
		int end_beat_type = 2;
		double start_divisions = Double.parseDouble(divisionValue.trim());
		double end_divisions = Double.parseDouble(divisionValue.trim());
		int start_bar = pair.startNote.measure.measureNumber;
		int start_offset = 1;
		int end_bar = pair.endNote.measure.measureNumber;
		int end_offset = 1;

		//	double divisions = pair.startNote.measure.computedAttributes.divisions;
		//	double durationCounter = divisions / start_divisions;	
		//start_offset = (int) (pair.startNote.questionStdDuration - (pair.startNote.duration / durationCounter)  + 1.0)-1 ;
		start_offset = (int) pair.startNote.startPosition;
		end_offset = (int) pair.endNote.endPosition;
		StringBuffer answerString = new StringBuffer();
		if(pair.startNote.measure.computedAttributes.time!=null || pair.endNote.measure.computedAttributes.time!=null){
			Measure measure = pair.startNote.measure;
			if(pair.startNote.measure.computedAttributes.time==null){
				measure = pair.endNote.measure;				
			}
			start_beats = (int) measure.computedAttributes.time.beats;
			start_beat_type = (int) measure.computedAttributes.time.beatType;
			end_beats = (int) measure.computedAttributes.time.beats;
			end_beat_type = (int) measure.computedAttributes.time.beatType;
			answerString.append("<passage ");
			answerString.append("end_bar=" + "\"" + end_bar + "\" " + "end_beat_type=" + "\"" + end_beat_type  + "\" " + "end_beats=" + "\"" + end_beats + "\"\n");
			answerString.append("end_divisions=" + "\"" + (int) end_divisions + "\" ");
			answerString.append("end_offset=" + "\"" + end_offset + "\" ");
			answerString.append("start_bar=" + "\"" + start_bar + "\" " + "start_beat_type=" + "\"" + start_beat_type + "\"\n");
			answerString.append("start_beats=" + "\"" + start_beats + "\" " + "start_divisions=" + "\"" + (int) start_divisions + "\" ");
			answerString.append("start_offset=" + "\"" + start_offset + "\" />\n");	
		}
		return answerString.toString().trim();
	}


	public static void setPositionsOfNotes(Music music, String divisionValue) {
		double start_divisions = Double.parseDouble(divisionValue.trim());
		MeasureAttributes ma = null;
		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);								
			for (String measureNumber : part.measures.keySet()) {
				double endPosition = 1;
				Measure measure = part.measures.get(measureNumber);
				if(measureNumber.equals("20")){
					System.out.println();
				}
				measure.partNumber = partNumber;
				if(measure.attributes != null){
					if(measure.attributes.time==null){
						if(ma!=null){
							if(ma.time!=null){
								measure.attributes.time = ma.time;
							}
						}
					}

					if(measure.attributes.staves == -1){
						if(ma!=null){
							if(ma.staves!=-1){
								measure.attributes.staves = ma.staves;
							} else {
								measure.attributes.staves = 1;
							}
						}					
					} 

					if(measure.attributes.clefs==null || measure.attributes.clefs.size()==0){
						if(ma!=null){
							if(ma.clefs!=null){
								measure.attributes.clefs = ma.clefs;
							}
						}
					} else {
						if(measure.attributes.staves>1){
							if(measure.attributes.staves != measure.attributes.clefs.size()){
								if(ma!=null){
									if(ma.clefs!=null){								
										List<Integer> missingClefNumbers = new ArrayList<Integer>();
										for(int i =1; i<=measure.attributes.staves; i++){
											boolean found = false;
											for(Clef currentClef : measure.attributes.clefs){
												if(currentClef.clefNumber == i){
													found = true;
												}
											}
											if(!found){
												missingClefNumbers.add(i);
											}
										}
										for(Clef prevClef : ma.clefs){
											if(missingClefNumbers.contains(prevClef.clefNumber)){
												measure.attributes.clefs.add(prevClef);
											}																	}
									}
								}
							}
						}
					}

					if(measure.attributes.divisions==0.0){
						measure.attributes.divisions = ma.divisions;
					}
					ma = measure.attributes;
				}
				measure.computedAttributes = ma;

				double divisions = measure.computedAttributes.divisions;
				double durationCounter = divisions / start_divisions;

				for(Integer noteNumber : measure.notes.keySet()) {			
					Note note = measure.notes.get(noteNumber);

					if(note.staff==null){
						note.staff = "1";
					}
					if(!note.backUp || !note.forward){
						if(ma.clefs!=null){
							if(ma.clefs.size()!=0){
								for(Clef clef : ma.clefs){
									if(Integer.parseInt(note.staff.trim()) == (clef.clefNumber)){
										note.clef = clef;
										break;
									}
								}
							}
						}
					}
					note.measure = measure;
					double noteDuration = note.duration;	
					if(note.backUp){
						endPosition = endPosition - noteDuration / durationCounter;
					} else if(note.chord) {
					} else {
						endPosition = endPosition + noteDuration / durationCounter;
					}
					note.questionStdDuration = noteDuration / durationCounter;
					note.endPosition = endPosition;
					note.startPosition = (int) (note.endPosition - (note.duration / durationCounter)  + 1.0)-1 ;
					note.endPosition = note.endPosition - 1;
					if(note.backUp){
						note.startPosition = note.endPosition;
						if((int)note.endPosition == 0.0){
							//						note.startPosition = 1;
							//						note.endPosition = 1;
						}
					}
					if(!note.backUp && !note.forward){
						if((int) note.endPosition <= 0.0){
							note.endPosition = 1;
						} 
						if((int) note.startPosition <= 0.0){
							note.startPosition = 1;
						}
					}
					//	System.out.println(note.startPosition + "\t" + note.endPosition);
				}
			}
		}
	}

	public static void setPositionNotesInMeasures(Music music) {
		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);								
			for (String measureNumber : part.measures.keySet()) {
				Measure measure = part.measures.get(measureNumber);
				measure.partNumber = partNumber;
				LinkedHashMap<Double, List<Note>> notSortedPositionNotes = new LinkedHashMap<Double, List<Note>>();  
				for(Integer noteNumber : measure.notes.keySet()) {			
					Note note = measure.notes.get(noteNumber);
					double startPosition = note.startPosition;
					if(!notSortedPositionNotes.containsKey(startPosition)){
						List<Note> notes = new ArrayList<Note>();
						notSortedPositionNotes.put(startPosition, notes);
					}
					List<Note> notes = notSortedPositionNotes.get(startPosition);
					notes.add(note);				
				}				
				Double[] positionsArray = new Double[notSortedPositionNotes.keySet().size()];
				positionsArray = notSortedPositionNotes.keySet().toArray(positionsArray);				
				Arrays.sort(positionsArray);
				for(Double position : positionsArray){
					List<Note> notes = notSortedPositionNotes.get(position);
					measure.positionNotes.put(position, notes);
				}		
			}
		}		
	}		

	public static void main(String[] args) {
		StringBuffer answerXml = new StringBuffer();
		answerXml.append("<?xml version=\"1.0\" ?>\n");
		answerXml.append("<questions task=\"camerata\" year=\"2014\" task_no=\"1\"\n" + 
				"runtag=\"unlp01\" organisation=\"The Insight Centre for Data Analytics\"\n" + 
				"group=\"Unit for Natural Language Processing\">\n");	

		List<Question> questions = new ArrayList<Question>();
		//	String trainingXml =  "src/main/resources/data/training/training_v1.xml";
		String testQuestionsXml =  "src/main/resources/data/camerata_questions_2014/camerata_questions_2014.xml";
		DefaultHandler questionsHandler = new QuestionsXmlHandler(questions);
		QuestionsXmlReader questionsReader = new QuestionsXmlReader(testQuestionsXml);
		questionsReader.read(questionsHandler);
		System.out.println(questions);
		for(Question question : questions) {
			if(question.number.equals("7")) {
				System.out.println("debug");
			}
			answerXml.append("<question divisions=" + "\"" + question.divisions + "\"" + " music_file=\"" + question.musicXmlFile + "\" " + 
					"number=\"" + question.number + "\">\n");
			answerXml.append("<text>" + question.question + "</text>\n");
			answerXml.append("<answer>\n");
			Music music = new Music();			
			String questionString = question.question;
			if(questionString.contains("mordent") || questionString.contains("harmonic") || questionString.contains("unison") ||
					questionString.contains("melody") || questionString.contains("against") || questionString.contains("arpeggio") || 
					questionString.contains("chord")  || questionString.contains("trill")
					|| questionString.contains("fermata") || questionString.contains("staccat") || 
					questionString.contains("descending")){
				answerXml.append("</answer>\n");
				answerXml.append("</question>\n");
				continue;
			}
			System.out.println(questionString + "\t" + question.number);

			DefaultHandler musicHandler = new MusicXmlHandler(music);
			MusicReader musicReader = new MusicReader("src/main/resources/data/camerata_questions_2014/" + question.musicXmlFile);
			musicReader.read(musicHandler);	
			List<Note> notes = MusicEntityRecognizer.recognizeNoteTimesAndPitches(questionString);
			String instrumentName = MusicEntityRecognizer.extractInstrument(questionString);
			String staff = MusicEntityRecognizer.extractStaff(questionString);
			String clef = MusicEntityRecognizer.extractClef(questionString);			
			String bars = MusicEntityRecognizer.extractBars(questionString);

			List<Note> finalNotes = new ArrayList<Note>();
			for(Note note : notes) {
				if(note.entityRecognized) {
					note.staff = staff;
					finalNotes.add(note);
				}
			}
			if(finalNotes.size()>0){
				setPositionsOfNotes(music, question.divisions);
				setPositionNotesInMeasures(music);
				Set<Pair> pairs = MeasureExtractor.getMeasuresForMultipleNotesUsingPosMap(music, finalNotes, instrumentName, clef, bars);				
				System.out.println(pairs.size());
				Set<String> answerPassages = new HashSet<String>();
				for(Pair pair : pairs) {
					String passage = getCompleteAnswerWithNotes(pair, music, question.divisions);
					answerPassages.add(passage);
				}

				for(String passage : answerPassages){
					answerXml.append(passage);		
				}
			}
			answerXml.append("</answer>\n");
			answerXml.append("</question>\n");
		}
		answerXml.append("</questions>");

		BasicFileTools.writeFile("src/main/resources/data/me14camerata_unlp01.xml", answerXml.toString().trim());
	}

}
