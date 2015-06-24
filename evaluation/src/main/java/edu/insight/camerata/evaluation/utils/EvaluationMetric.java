package edu.insight.camerata.evaluation.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.question.xml.Answer;
import edu.insight.camerata.evaluation.question.xml.Passage;
import edu.insight.camerata.evaluation.question.xml.Question;
import edu.insight.camerata.evaluation.question.xml.QuestionsXmlHandler;
import edu.insight.camerata.evaluation.question.xml.QuestionsXmlReader;

public class EvaluationMetric {

	public static String goldQuestionsXml =  "src/main/resources/data/Submission/camerata_gold_standard_2014.xml";
	//public static String runQuestionsXml =  "src/main/resources/data/Submission/me14camerata_unlp01_old.xml";
	//public static String runQuestionsXml =  "src/main/resources/data/Submission/me14camerata_unlp02_old.xml";
	public static String runQuestionsXml = "src/main/resources/data/me14camerata_unlp01.xml";

	public static List<Question> goldQuestions = new ArrayList<Question>();
	public static List<Question> runQuestions = new ArrayList<Question>();

	public static void main(String[] args) {
		Map<Question, Answer> questionAnswerMap = new HashMap<Question, Answer>();

		DefaultHandler handler = new QuestionsXmlHandler(goldQuestions);
		QuestionsXmlReader lReader = new QuestionsXmlReader(goldQuestionsXml);
		lReader.read(handler);

		handler = new QuestionsXmlHandler(runQuestions);
		lReader = new QuestionsXmlReader(runQuestionsXml);
		lReader.read(handler);

		Map<String, Question> goldAnswers = new HashMap<String, Question>();

		for(Question question : goldQuestions){
			goldAnswers.put(question.number, question);			
		}

		double overallBeatCorrectPassagesCount = 0.0;		
		double overallMeasureCorrectPassagesCount = 0.0;	
		double overallTotalGoldPassages = 0.0;
		double overallTotalRunPassages = 0.0;

		for(Question run : runQuestions){
			Answer answer = new Answer();
			double measureCorrectPassagesCount = 0.0;
			Question gold = goldAnswers.get(run.number);
			double totalGoldPassages = gold.passages.size();
			double totalRunPassages = run.passages.size();
			for(Passage goldPassage : gold.passages){
				for(Passage runPassage : run.passages){
					if(goldPassage.startBar == runPassage.startBar && 
							goldPassage.endBar == runPassage.endBar) {					
						measureCorrectPassagesCount++;						
						run.passages.remove(runPassage);
						break;
					}					 
				}
			}
			answer.passagesMeasureCorrect = measureCorrectPassagesCount;
			answer.goldCorrectPassages = totalGoldPassages;
			answer.passagesReturned = totalRunPassages;
			questionAnswerMap.put(gold, answer);
			overallMeasureCorrectPassagesCount = overallMeasureCorrectPassagesCount + measureCorrectPassagesCount;
			overallTotalGoldPassages = overallTotalGoldPassages + totalGoldPassages;
			overallTotalRunPassages = overallTotalRunPassages + totalRunPassages;			
			//		System.out.println("MP:\t"  +  measureCorrectPassagesCount/totalRunPassages);
			//		System.out.println("MR:\t" + measureCorrectPassagesCount/totalGoldPassages);	
		}

		//	System.out.println("Overall MP:\t" + overallMeasureCorrectPassagesCount/overallTotalGoldPassages);
		//	System.out.println("Overall MR:\t" + overallMeasureCorrectPassagesCount/overallTotalRunPassages);

		runQuestions = new ArrayList<Question>();

		handler = new QuestionsXmlHandler(runQuestions);
		lReader = new QuestionsXmlReader(runQuestionsXml);
		lReader.read(handler);


		overallBeatCorrectPassagesCount = 0.0;		
		overallTotalGoldPassages = 0.0;
		overallTotalRunPassages = 0.0;

		for(Question run : runQuestions){
			double beatCorrectPassagesCount = 0.0;
			Question gold = goldAnswers.get(run.number);
			Answer answer = questionAnswerMap.get(gold);

			double totalGoldPassages = gold.passages.size();
			double totalRunPassages = run.passages.size();
			for(Passage goldPassage : gold.passages){
				for(Passage runPassage : run.passages){
					if(goldPassage.startBar == runPassage.startBar && 
							goldPassage.endBar == runPassage.endBar) {					
						if(goldPassage.startOffset == runPassage.startOffset && 
								goldPassage.endOffset == runPassage.endOffset){					
							beatCorrectPassagesCount++;
							run.passages.remove(runPassage);
							break;
						}						
					}					 
				}
			}			
			answer.passagesBeatCorrect = beatCorrectPassagesCount;			
			overallBeatCorrectPassagesCount = overallBeatCorrectPassagesCount + beatCorrectPassagesCount;
			overallTotalGoldPassages = overallTotalGoldPassages + totalGoldPassages;
			overallTotalRunPassages = overallTotalRunPassages + totalRunPassages;			
			//		System.out.println("BP:\t"  +  beatCorrectPassagesCount/totalRunPassages);
			//		System.out.println("BR:\t" + beatCorrectPassagesCount/totalGoldPassages);	
		}


		Map<String, Answer> questionTypeAnswer = new HashMap<String, Answer>();
		String overallQuestionType = "overall";
		questionTypeAnswer.put(overallQuestionType, new Answer());
		
		String questionTypeToBePrinted = "stave_spec";
		
		for(Question question : questionAnswerMap.keySet()){			
			if(!questionTypeAnswer.containsKey(question.questionType)){
				questionTypeAnswer.put(question.questionType, new Answer());				
			} 
			Answer typeAnswer = questionTypeAnswer.get(question.questionType);
			Answer thisQuestionAnswer = questionAnswerMap.get(question);
			typeAnswer.goldCorrectPassages  = typeAnswer.goldCorrectPassages + thisQuestionAnswer.goldCorrectPassages;
			typeAnswer.passagesBeatCorrect  = typeAnswer.passagesBeatCorrect + thisQuestionAnswer.passagesBeatCorrect;
			typeAnswer.passagesMeasureCorrect  = typeAnswer.passagesMeasureCorrect + thisQuestionAnswer.passagesMeasureCorrect;
			typeAnswer.passagesReturned  = typeAnswer.passagesReturned + thisQuestionAnswer.passagesReturned;
			typeAnswer.totalQuestionsCount++;
			Answer overallAnswer = questionTypeAnswer.get(overallQuestionType);
			overallAnswer.goldCorrectPassages = overallAnswer.goldCorrectPassages + thisQuestionAnswer.goldCorrectPassages;
			overallAnswer.passagesBeatCorrect  = overallAnswer.passagesBeatCorrect + thisQuestionAnswer.passagesBeatCorrect;
			overallAnswer.passagesMeasureCorrect  = overallAnswer.passagesMeasureCorrect + thisQuestionAnswer.passagesMeasureCorrect;
			overallAnswer.passagesReturned  = overallAnswer.passagesReturned + thisQuestionAnswer.passagesReturned;
			overallAnswer.totalQuestionsCount++;			
			if(questionTypeToBePrinted.equalsIgnoreCase(question.questionType)){
				System.out.println(question.question);
				System.out.println(question.number);
				System.out.println(question.musicXmlFile);
				System.out.println(thisQuestionAnswer.shortAnswer());
			}			
		}
	
		for(String questionType : questionTypeAnswer.keySet()){	
			System.out.println("\n" + questionType);
			System.out.println(questionTypeAnswer.get(questionType));
		}
		
		System.out.println("Overall BP:\t" + overallBeatCorrectPassagesCount/overallTotalRunPassages);		
		System.out.println("Overall BR:\t" + overallBeatCorrectPassagesCount/overallTotalGoldPassages);
		System.out.println("Overall MP:\t" + overallMeasureCorrectPassagesCount/overallTotalRunPassages);		
		System.out.println("Overall MR:\t" + overallMeasureCorrectPassagesCount/overallTotalGoldPassages);


	}	

}
