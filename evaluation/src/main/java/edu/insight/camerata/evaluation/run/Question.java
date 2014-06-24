package edu.insight.camerata.evaluation.run;

public class Question {

	public String question;
	public String musicXmlFile; 
	public String divisions;
	public String number;

	@Override
	public String toString() {
		//return question + "\t" + musicXmlFile + "\n";
		return question + "\n";
	}
	
}
