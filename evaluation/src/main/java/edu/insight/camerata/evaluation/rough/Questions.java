package edu.insight.camerata.evaluation.rough;

import java.io.BufferedReader;
import java.io.IOException;

import edu.insight.camerata.evaluation.utils.BasicFileTools;

public class Questions {

	public static void main(String[] args) {
		//
		String questions = "src/main/resources/TrainingQuestions";
		BufferedReader reader = BasicFileTools.getBufferedReaderFile(questions);
		String line = "";
		try {
			while((line=reader.readLine())!=null) {
				String[] split = line.split("Question\\s*text\\s*:");
				String question = split[1].trim();
				System.out.println(question);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
