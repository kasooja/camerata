package edu.insight.camerata.evaluation.rough;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileHelper {
	
	public static void main(String[] args) {
		try {
			System.out.println("START");
			String outputPath = "/Users/Scott/workspace/myrepo/my-app/output/text.txt";
			FileHelper fileHelper = new FileHelper();
			fileHelper.write(new File(outputPath), "Hello World! My First HTML Page!");
			String string = fileHelper.read(new File(outputPath));
			System.out.println("From FILE:"+string);
			System.out.println("FINISH");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String read(File inputFile) throws Exception {
		// Initialization
		String string = "";
		String line;
		
		// Read from file
		FileReader fr = new FileReader(inputFile);
		BufferedReader br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
			string = string + line;
		}
		br.close();
		fr.close();
		
		return string;
	}

	public void write(File outputFile, String string) throws Exception {
		// Initialization
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		// Write to file
		FileWriter fw = new FileWriter(outputFile);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(string);
		bw.close();
		fw.close();

	}
}