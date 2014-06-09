package edu.insight.camerata.evaluation.rough;

import java.io.File;
import org.jfugue.MusicStringParser;
import org.jfugue.MusicStringRenderer;
import org.jfugue.MusicXmlParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;
import org.jfugue.Player;

import edu.insight.camerata.evaluation.xml.MusicXMLReaderTest;

public class MusicXMLDemo {

	public static void main(String[] args) { 
		try {
			System.out.println("START");
			//String inputFile = "src/main/resources/Tonight.xml";
			//String inputFile = "src/main/resources/DebuMandSample.xml";
			String inputFile = "src/main/resources/data/training" +
					"/f1.xml";
			MusicXmlParser parser = new MusicXmlParser();
			parser.parse(new File(inputFile));
			
			MusicXMLDemo fileHelper = new MusicXMLDemo();

			Pattern readPatt = fileHelper.read(new File(inputFile));
			System.out.println("k");

			//String outputFile = "src/main/resources/mxmlfhout.xml";
			//Pattern pattern = new Pattern("KEmaj V0 E4i+B4i B3i E4i+B4i B3i E4i+B4i B3i E4i+B4i B3i V1 E1h E1i F#1i G#1i B1i");

			//	fileHelper.write(new File(outputFile), pattern);

			Player player = new Player();
			System.out.println(readPatt.getMusicString());
			player.play(readPatt);
			//player.play(pattern);
			System.out.println("FINISH");
		} catch (Exception ex) {
		}
	}

	public void write(File outputFile, Pattern musicPattern) throws Exception {
		MusicXmlRenderer renderer = new MusicXmlRenderer();
		MusicStringParser parser = new MusicStringParser();
		parser.addParserListener(renderer);
		parser.parse(musicPattern);
		FileHelper fileHelper = new FileHelper();
		fileHelper.write(outputFile, renderer.getMusicXMLString());
	}

	public Pattern read(File inputFile) throws Exception {
		MusicStringRenderer renderer = new MusicStringRenderer();
		MusicXmlParser parser = new MusicXmlParser();
		parser.addParserListener(renderer);
		parser.parse(inputFile);
		return new Pattern(renderer.getPattern());
	}

}
