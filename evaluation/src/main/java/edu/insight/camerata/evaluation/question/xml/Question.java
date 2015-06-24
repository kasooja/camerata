package edu.insight.camerata.evaluation.question.xml;

import java.util.ArrayList;
import java.util.List;

public class Question {

	public String question;
	public String musicXmlFile; 
	public String divisions;
	public String number;
	public List<Passage> passages = new ArrayList<Passage>();
	public String questionType = null;

	@Override
	public String toString() {
		//return question + "\t" + musicXmlFile + "\n";
		return question + "\t" + questionType + "\n";
		
	}
	
}
