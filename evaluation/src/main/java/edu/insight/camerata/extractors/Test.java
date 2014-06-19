package edu.insight.camerata.extractors;
import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.MusicReader;
import edu.insight.camerata.evaluation.xml.MusicXmlHandler;


public class Test{
	public static void main(String[] args) {

		Music music1 = new Music();
		String dataPath1 =  "src/main/resources/data/training/f4.xml";

		DefaultHandler handler1 = new MusicXmlHandler(music1);
		MusicReader lReader1 = new MusicReader(dataPath1);
		lReader1.read(handler1);

		PitchExtractor.getPitch(music1, "F", "5", "1", false);

	}
}
