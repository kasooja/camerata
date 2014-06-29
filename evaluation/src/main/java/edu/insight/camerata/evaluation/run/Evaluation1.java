package edu.insight.camerata.evaluation.run;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.utils.BasicFileTools;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.MusicReader;
import edu.insight.camerata.evaluation.xml.MusicXmlHandler;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.extractors.NoteExtractor;
import edu.insight.camerata.nlp.MusicEntityRecognizer;

public class Evaluation1 {

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
		for(Integer noteNumber : m.notes.keySet()) {
			Note note = m.notes.get(noteNumber);
			double noteDuration = note.duration;
			offset = offset + noteDuration / durationCounter;
			if(m.answerPairs.contains(answerPair)) {
				if(note == answerPair.startNote) 
					start_offset = (int) (offset - (noteDuration / durationCounter)  + 1.0) ;		
				if(note == answerPair.endNote) 
					end_offset = (int) offset;				
			}
		}

		StringBuffer answerString = new StringBuffer();

		if(m.computedAttributes.time!=null){		
			start_beats = (int) m.computedAttributes.time.beats;
			start_beat_type = (int) m.computedAttributes.time.beatType;
			end_beats = (int) m.computedAttributes.time.beats;
			end_beat_type = (int) m.computedAttributes.time.beatType;

			answerString.append("<passage ");
			answerString.append("start_beats=\"" + start_beats + "\" " + "start_beat_type=" + "\"" + start_beat_type + "\"\n");
			answerString.append("end_beats=\"" + end_beats + "\" " + "end_beat_type=" + "\"" + end_beat_type + "\"\n");

			answerString.append("start_divisions=\"" + (int) start_divisions + "\" ");
			answerString.append("end_divisions=" + "\"" + (int) end_divisions + "\"\n");

			answerString.append("start_bar=\"" + start_bar + "\" " + "start_offset=" + "\"" + start_offset + "\"\n");
			answerString.append("end_bar=\"" + end_bar + "\" " + "end_offset=" + "\"" + end_offset + "\"/>\n");
		}
		return answerString.toString().trim();
	}


	//	public static void mainOld(String[] args) {
	//		StringBuffer answerXml = new StringBuffer();
	//		answerXml.append("<?xml version=\"1.0\" ?>\n");
	//		answerXml.append("<questions task=\"camerata\" year=\"2014\" task_no=\"1\"\n" + 
	//				"runtag=\"unlp01\" organisation=\"The Insight Centre for Data Analytics\"\n" + 
	//				"group=\"Unit for Natural Language Processing\">\n");	
	//
	//
	//		List<Question> questions = new ArrayList<Question>();
	//		String trainingXml =  "src/main/resources/data/training/training_v1.xml";
	//		String testQuestionsXml =  "src/main/resources/data/camerata_questions_2014/camerata_questions_2014.xml";
	//
	//		DefaultHandler questionsHandler = new QuestionsXmlHandler(questions);
	//		QuestionsXmlReader questionsReader = new QuestionsXmlReader(testQuestionsXml);
	//		questionsReader.read(questionsHandler);
	//		System.out.println(questions);
	//		for(Question question : questions){
	//			answerXml.append("<question divisions=" + "\"" + question.divisions + "\"" + " music_file=\"" + question.musicXmlFile + "\" " + 
	//					"number=\"" + question.number + "\">\n");
	//			answerXml.append("<text>" + question.question + "</text>\n");
	//			answerXml.append("<answer>\n");
	//			Music music = new Music();			
	//			String questionString = question.question;
	//			System.out.println(questionString);
	//			DefaultHandler musicHandler = new MusicXmlHandler(music);
	//			MusicReader musicReader = new MusicReader("src/main/resources/data/camerata_questions_2014/" + question.musicXmlFile);
	//			musicReader.read(musicHandler);	
	//
	//			Pitch pitch = MusicEntityRecognizer.recognizePitchEntities(questionString);
	//
	//
	//			if(pitch.entityRecognized) {
	//				Set<Measure> measures = PitchExtractor.getPitch(music, pitch);
	//				for(Measure measure : measures) {
	//					for(Pair answerPair : measure.answerPairs){
	//						String passage = getCompleteAnswer(measure, music, question.divisions, answerPair);
	//						answerXml.append(passage);				
	//					}
	//				}
	//			}
	//			answerXml.append("</answer>\n");
	//			answerXml.append("</question>\n");
	//		}
	//		answerXml.append("</questions>");
	//
	//		BasicFileTools.writeFile("src/main/resources/data/me14camerata_unlp01.xml", answerXml.toString().trim());
	//	}

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
			answerXml.append("<question divisions=" + "\"" + question.divisions + "\"" + " music_file=\"" + question.musicXmlFile + "\" " + 
					"number=\"" + question.number + "\">\n");
			answerXml.append("<text>" + question.question + "</text>\n");
			answerXml.append("<answer>\n");
			Music music = new Music();			
			String questionString = question.question;
			if(questionString.equalsIgnoreCase("E"))
				System.out.println("kartik gadhi hai");
			System.out.println(questionString);
			DefaultHandler musicHandler = new MusicXmlHandler(music);
			MusicReader musicReader = new MusicReader("src/main/resources/data/camerata_questions_2014/" + question.musicXmlFile);
			musicReader.read(musicHandler);	

			Set<Note> notes = MusicEntityRecognizer.extractNotes(questionString);			
			String instrumentName = MusicEntityRecognizer.extractInstrument(questionString);
			String staff = MusicEntityRecognizer.extractStaff(questionString);
			String clef = MusicEntityRecognizer.extractClef(questionString);

			Set<Measure> answerMeasures = new HashSet<Measure>();
			for(Note note : notes) {
				if(note.entityRecognized) {
					note.staff = staff;					
					Set<Measure> measures = NoteExtractor.getNote(music, note, instrumentName, clef);
					answerMeasures.addAll(measures);
				}
			}
			for(Measure measure : answerMeasures) {
				for(Pair answerPair : measure.answerPairs){
					String passage = getCompleteAnswer(measure, music, question.divisions, answerPair);
					answerXml.append(passage);	
				}
			}
			answerXml.append("</answer>\n");
			answerXml.append("</question>\n");
		}
		answerXml.append("</questions>");

		BasicFileTools.writeFile("src/main/resources/data/me14camerata_unlp02.xml", answerXml.toString().trim());
	}



}
