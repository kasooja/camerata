package edu.insight.camerata.evaluation.question.xml;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

public class QuestionsXmlReader {
	public String path;
	
	public QuestionsXmlReader(String dataPath) {
		this.path = dataPath;
	}

	public void read(DefaultHandler handler){
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();		
			saxParser.parse(path, handler);
		} catch (Exception e){
			e.printStackTrace();
		}
	} 
}







