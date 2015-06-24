package edu.insight.camerata.evaluation.question.xml;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class QuestionsXmlHandler extends DefaultHandler {

	private Question currentQuestion;
	private Passage currentPassage;
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
			if(attributes.getIndex("type")!=-1){
				currentQuestion.questionType = attributes.getValue("type");
			}
		}
		if (qName.equalsIgnoreCase("passage")) {
			currentPassage = new Passage();
			currentQuestion.passages.add(currentPassage);
			currentPassage.endBar = Integer.parseInt(attributes.getValue("end_bar").trim());
			currentPassage.endBeats = Integer.parseInt(attributes.getValue("end_beats").trim());			
			currentPassage.endDivisions = Integer.parseInt(attributes.getValue("end_divisions").trim());
			currentPassage.endOffset = Integer.parseInt(attributes.getValue("end_offset").trim());
			currentPassage.endBeatType = Integer.parseInt(attributes.getValue("end_beat_type").trim());

			currentPassage.startBar = Integer.parseInt(attributes.getValue("start_bar").trim());
			currentPassage.startBeats = Integer.parseInt(attributes.getValue("start_beats").trim());
			currentPassage.startBeatType = Integer.parseInt(attributes.getValue("start_beat_type").trim());
			currentPassage.startOffset = Integer.parseInt(attributes.getValue("start_offset").trim());
			currentPassage.startDivisions = Integer.parseInt(attributes.getValue("start_divisions").trim());			
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