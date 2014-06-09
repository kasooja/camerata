package edu.insight.camerata.evaluation;

import java.io.BufferedReader;
import java.io.IOException;

public class Questions {

	public static void main(String[] args) {
		String questions = "src/main/resources/TrainingQuestions";
		BufferedReader reader = BasicFileTools.getBufferedReaderFile(questions);
		String line = "";
		try {
			while((line=reader.readLine())!=null) {
				//System.out.println(line);
				String[] split = line.split("Question\\s*text\\s*:");
				String question = split[1].trim();
				System.out.println(question);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
