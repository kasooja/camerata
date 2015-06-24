package edu.insight.camerata.evaluation.question.xml;

public class Answer {

	public double passagesReturned = 0.0;
	public double passagesBeatCorrect = 0.0;
	public double passagesMeasureCorrect = 0.0;
	public double goldCorrectPassages = 0.0;	
	public double totalQuestionsCount = 0.0;
	


	@Override
	public String toString() {
		StringBuilder answerBld = new StringBuilder();
		answerBld.append("Total Questions: " + totalQuestionsCount + "\n\n");
		answerBld.append("Passages returned: " + passagesReturned + "\n");
		answerBld.append("Passages beat correct: " + passagesBeatCorrect + "\n");
		answerBld.append("Passages measure correct:" + passagesMeasureCorrect + "\n");
		answerBld.append("Gold correct passages: " + goldCorrectPassages + "\n");
		answerBld.append("\n");	
		answerBld.append("Beat Precision: " + passagesBeatCorrect/passagesReturned + "\n");
		answerBld.append("Beat Recall: " + passagesBeatCorrect/goldCorrectPassages + "\n");
		answerBld.append("Measure Precision: " + passagesMeasureCorrect/passagesReturned + "\n");
		answerBld.append("Measure Recall: " + passagesMeasureCorrect/goldCorrectPassages + "\n");
		answerBld.append("**-----------------------------------**");		
		return answerBld.toString().trim();
	}
	
	public String shortAnswer() {
		StringBuilder answerBld = new StringBuilder();
		answerBld.append("Beat Precision: " + passagesBeatCorrect/passagesReturned + "\n");
		answerBld.append("Beat Recall: " + passagesBeatCorrect/goldCorrectPassages + "\n");
		answerBld.append("Measure Precision: " + passagesMeasureCorrect/passagesReturned + "\n");
		answerBld.append("Measure Recall: " + passagesMeasureCorrect/goldCorrectPassages + "\n");
		answerBld.append("**-----------------------------------**");		
		return answerBld.toString().trim();
	}


}
