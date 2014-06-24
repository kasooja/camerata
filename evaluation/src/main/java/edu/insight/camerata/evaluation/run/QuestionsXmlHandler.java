package edu.insight.camerata.evaluation.run;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class QuestionsXmlHandler extends DefaultHandler {

	private Question currentQuestion;
	private boolean takeText;
	private String tagStringValue;
	private List<Question> questions; 

	public QuestionsXmlHandler(List<Question> questions) {
		this.questions = questions;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {		
		if (qName.equalsIgnoreCase("question")) {
			currentQuestion = new Question();
			questions.add(currentQuestion);
			currentQuestion.musicXmlFile = attributes.getValue("music_file");
			currentQuestion.divisions = attributes.getValue("divisions");
			currentQuestion.number = attributes.getValue("number");
		}

		if (qName.equalsIgnoreCase("text")) {
			takeText = true;
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("text"))
			currentQuestion.question = tagStringValue;
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		if (takeText) {
			tagStringValue = new String(ch, start, length);
			takeText = false;
		} 
	}

}