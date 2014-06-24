package edu.insight.camerata.evaluation.run;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.MusicReader;
import edu.insight.camerata.evaluation.xml.MusicXmlHandler;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.Pitch;
import edu.insight.camerata.extractors.PitchExtractor;
import edu.insight.camerata.nlp.MusicEntityRecognizer;

public class Evaluation {

	private static String startTimeSig;
	private static String endTimeSig;
	private static double startDivs;
	private static double endDivs;
	private static Part firstPart;
	private static Measure firstMeasure;
	private static Part endPart;
	private static Measure endMeasure;

	public static void getAnswer(String question, Music music1) {
		for (String key : music1.musicPartMap.keySet()) {
			firstPart = music1.musicPartMap.get(key);
			break;
		}
		for (String key : firstPart.measures.keySet()) {
			firstMeasure = firstPart.measures.get(key);
			break;
		}
		startTimeSig = firstMeasure.attributes.time.beats + "/" + firstMeasure.attributes.time.beatType;
		System.out.println("Start Time Signature : " + startTimeSig);

		// To fetch end time signature
		for (String key : music1.musicPartMap.keySet()) {
			endPart = music1.musicPartMap.get(key);
		}
		for (String key : endPart.measures.keySet()) {
			endMeasure = endPart.measures.get(key);
			break;
		}
		endTimeSig = endMeasure.attributes.time.beats + "/" + endMeasure.attributes.time.beatType;
		System.out.println("End Time Signature : " + endTimeSig);		

		// To fetch start divisions
		startDivs = firstMeasure.attributes.time.beatType / 4;
		System.out.println("Start Divisions : " + startDivs);		

		// To fetch end divisions
		endDivs = endMeasure.attributes.time.beatType / 4;
		System.out.println("End Divisions : " + endDivs);		

	}

	public static void main(String []args) {
		List<Question> questions = new ArrayList<Question>();
		String trainingXml =  "src/main/resources/data/training/training_v1.xml";
		String testQuestionsXml =  "src/main/resources/data/camerata_questions_2014/camerata_questions_2014.xml";
	
		DefaultHandler questionsHandler = new QuestionsXmlHandler(questions);
		QuestionsXmlReader questionsReader = new QuestionsXmlReader(testQuestionsXml);
		questionsReader.read(questionsHandler);
		System.out.println(questions);
		for(Question question : questions){
			Music music1 = new Music();			
			String questionString = question.question;
			DefaultHandler musicHandler = new MusicXmlHandler(music1);
			MusicReader musicReader = new MusicReader("src/main/resources/data/camerata_questions_2014/" + question.musicXmlFile);
			musicReader.read(musicHandler);			
			Pitch pitch = MusicEntityRecognizer.recognizePitchEntities(questionString);
			List<Measure> measures = PitchExtractor.getPitch(music1, pitch);
			
			
		}
	}

}
