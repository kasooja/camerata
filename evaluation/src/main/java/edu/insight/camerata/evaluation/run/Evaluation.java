package edu.insight.camerata.evaluation.run;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.utils.BasicFileTools;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.MusicReader;
import edu.insight.camerata.evaluation.xml.MusicXmlHandler;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.Pitch;
import edu.insight.camerata.extractors.PitchExtractor;
import edu.insight.camerata.nlp.MusicEntityRecognizer;

public class Evaluation {


	public static void getAnswer(Measure m, Music music1) {
		String startTimeSig;
		String endTimeSig;
		Part firstPart = null;
		Measure firstMeasure = null;
		Part endPart = null;
		Measure endMeasure = null;

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

		if(music1.musicPartMap.size() != 1){	
			// To fetch end time signature
			for (String key : music1.musicPartMap.keySet()) {
				endPart = music1.musicPartMap.get(key);
			}
			for (String key : endPart.measures.keySet()) {
				endMeasure = endPart.measures.get(key);
			}
			endTimeSig = endMeasure.attributes.time.beats + "/" + endMeasure.attributes.time.beatType;
			System.out.println("End Time Signature : " + endTimeSig);
		}
		else{
			endTimeSig = startTimeSig;
			System.out.println("End Time Signature : " + endTimeSig);			
		}
		/*
		// To fetch start divisions
		startDivs = firstMeasure.attributes.time.beatType / 4;
		System.out.println("Start Divisions : " + startDivs);		

		// To fetch end divisions
		endDivs = endMeasure.attributes.time.beatType / 4;
		System.out.println("End Divisions : " + endDivs);		
		 */
	}


	public static String getCompleteAnswer(Measure m, Music music1) {

		int start_beats = 2;
		int start_beat_type = 2;
		int end_beats = 2;
		int end_beat_type = 2;
		double start_divisions = 1;
		double end_divisions = 1;
		int start_bar = m.measureNumber;
		int start_offset = 1;
		int end_bar = m.measureNumber;
		int end_offset = 1;

		StringBuffer answerString = new StringBuffer();

		if(m.computedAttributes.time!=null){		
			start_beats = (int) m.computedAttributes.time.beats;
			start_beat_type = (int) m.computedAttributes.time.beatType;
			start_divisions = start_beat_type / 4; // but what happens if start_beat_type is 2; 
			answerString.append("<passage ");


			answerString.append("start_beats=\"" + start_beats + "\" " + "start_beat_type=" + "\"" + start_beat_type + "\"\n");
			answerString.append("end_beats=\"" + end_beats + "\" " + "end_beat_type=" + "\"" + end_beat_type + "\"\n");

			if(start_divisions < 1.0)
				answerString.append("start_divisions=\"" + start_divisions + "\" ");
			else
				answerString.append("start_divisions=\"" + (int) start_divisions + "\" ");
			if(end_divisions < 1.0)		
				answerString.append("end_divisions=" + "\"" + end_divisions + "\"\n");
			else 
				answerString.append("end_divisions=" + "\"" + (int) end_divisions + "\"\n");

			answerString.append("start_bar=\"" + start_bar + "\" " + "start_offset=" + "\"" + start_offset + "\"\n");
			answerString.append("end_bar=\"" + end_bar + "\" " + "end_offset=" + "\"" + end_offset + "\"/>\n");
		}
		return answerString.toString().trim();
	}


	public static void main(String[] args) {
		StringBuffer answerXml = new StringBuffer();
		answerXml.append("<?xml version=\"1.0\" ?>\n");
		answerXml.append("<questions task=\"camerata\" year=\"2014\" task_no=\"1\"\n" + 
				"runtag=\"unlp01\" organisation=\"The Insight Centre for Data Analytics\"\n" + 
				"group=\"Unit for Natural Language Processing\">\n");	

		//		  <question divisions="2" music_file="bach_minuet_in_g_bwv_anh114.xml" number="1">
		//		    <text>G5</text>
		//		    <answer>
		//		    </answer>
		//		  </question>


		//		<question number="001" music_file="f1.xml" divisions="1">
		//	    <text>F# followed two crotchets later by a G</text>
		//	    <answer>
		//	      <passage start_beats="2" start_beat_type="2"
		//	               end_beats="2" end_beat_type="2"
		//	               start_divisions="1" end_divisions="1"
		//	               start_bar="29" start_offset="3"
		//	               end_bar="30" end_offset="1" />
		//	    </answer>
		//	  </question>

		List<Question> questions = new ArrayList<Question>();
		String trainingXml =  "src/main/resources/data/training/training_v1.xml";
		String testQuestionsXml =  "src/main/resources/data/camerata_questions_2014/camerata_questions_2014.xml";

		DefaultHandler questionsHandler = new QuestionsXmlHandler(questions);
		QuestionsXmlReader questionsReader = new QuestionsXmlReader(testQuestionsXml);
		questionsReader.read(questionsHandler);
		System.out.println(questions);
		for(Question question : questions){
			answerXml.append("<question divisions=" + "\"" + question.divisions + "\"" + " music_file=\"" + question.musicXmlFile + "\" " + 
					"number=\"" + question.number + "\">\n");
			answerXml.append("<text>" + question.question + "</text>\n");
			answerXml.append("<answer>\n");
			Music music = new Music();			
			String questionString = question.question;
			System.out.println(questionString);
			DefaultHandler musicHandler = new MusicXmlHandler(music);
			MusicReader musicReader = new MusicReader("src/main/resources/data/camerata_questions_2014/" + question.musicXmlFile);
			musicReader.read(musicHandler);	

			Pitch pitch = MusicEntityRecognizer.recognizePitchEntities(questionString);
			if(pitch.entityRecognized){
				List<Measure> measures = PitchExtractor.getPitch(music, pitch);
				for(Measure measure : measures) {
					String passage = getCompleteAnswer(measure, music);
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
