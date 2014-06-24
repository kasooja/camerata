package edu.insight.camerata.evaluation.run;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

public class QuestionsXmlReaderTest {	
	public static List<Question> questions = new ArrayList<Question>();
	public static String trainingXml =  "src/main/resources/data/training/training_v1.xml";
	public static String testQuestionsXml =  "src/main/resources/data/camerata_questions_2014.xml";

	public static void main(String[] args) {		
		DefaultHandler handler = new QuestionsXmlHandler(questions);
		QuestionsXmlReader lReader = new QuestionsXmlReader(testQuestionsXml);
		lReader.read(handler);
		System.out.println(questions);
	}	

}
