package edu.insight.camerata.evaluation.rough;

import java.io.File;

import org.jfugue.integration.MusicXmlParser_J;
import org.jfugue.integration.MusicXmlParser_R;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;


public class MusicXMLDemo {

	public static void main(String[] args) { 
		try {
	//		System.out.println("START");
			//String inputFile = "src/main/resources/Tonight.xml";
			//String inputFile = "src/main/resources/DebuMandSample.xml";
			String inputFile = "/Users/kartik/git/camerata/evaluation/src/main/resources/data/camerata_questions_2014/bach_chorale_507b.xml";
			MusicXmlParser_R j = new MusicXmlParser_R();
		//	StaccatoParserListener listener = new StaccatoParserListener();
	     //   j.addParserListener(listener);
		//	j.parse(new File(inputFile));
			//MusicXmlParser_J = new MusicXmlParser_J();
			MusicXmlParser_J r = new MusicXmlParser_J();
			MyParserListener mylistener = new MyParserListener();
			r.addParserListener(mylistener);
			r.parse(new File(inputFile));
			System.out.println("There are " + mylistener.counter + " 'C' notes in this music.");
			
		//	Pattern staccatoPattern = listener.getPattern();
			
		//	System.out.println(staccatoPattern);
		//	System.out.println(staccatoPattern);
			Player player = new Player();
	      //  player.play(staccatoPattern);
	        
	        
	        
			//MusicXmlParser parser = new MusicXmlParser();
			//read(new File(inputFile));
			//System.out.println(read.getPropertiesAsParagraph());
			//parser.parse(new File(inputFile));
			//System.out.println(parser);

//			MusicXMLDemo fileHelper = new MusicXMLDemo();
//
//			Pattern readPatt = fileHelper.read(new File(inputFile));
//			System.out.println("k");
//
//			//String outputFile = "src/main/resources/mxmlfhout.xml";
//			//Pattern pattern = new Pattern("KEmaj V0 E4i+B4i B3i E4i+B4i B3i E4i+B4i B3i E4i+B4i B3i V1 E1h E1i F#1i G#1i B1i");
//
//			//	fileHelper.write(new File(outputFile), pattern);
//
//			Player player = new Player();
//			System.out.println(readPatt.getMusicString());
//			player.play(readPatt);
//			//player.play(pattern);
			System.out.println("FINISH");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

//	public void write(File outputFile, Pattern musicPattern) throws Exception {
//		MusicXmlRenderer renderer = new MusicXmlRenderer();
//		MusicStringParser parser = new MusicStringParser();
//		parser.addParserListener(renderer);
//		parser.parse(musicPattern);
//		FileHelper fileHelper = new FileHelper();
//		fileHelper.write(outputFile, renderer.getMusicXMLString());
//	}
//
//	public static Pattern read(File inputFile) throws Exception {
//		MusicStringRenderer renderer = new MusicStringRenderer();
//		MusicXmlParser parser = new MusicXmlParser();
//		parser.addParserListener(renderer);
//		parser.parse(inputFile);
//		return new Pattern(renderer.getPattern());
//	}

}

class MyParserListener extends ParserListenerAdapter {
    public int counter;

    public void onNoteParsed(Note note) {
        // A "C" note is in the 0th position of an octave
            counter++;
    }
}
