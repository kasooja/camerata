package edu.insight.camerata.evaluation.xml;

import org.xml.sax.helpers.DefaultHandler;

public class MusicXMLReaderTest {	
	public static Music music = new Music();
	//public static String dataPath =  "src/main/resources/data/training/f5.xml";
	public static String dataPath =  "src/main/resources/data/camerata_questions_2015/bach_brand_conc_no2_bwv1047_andante.xml";

	public static void main(String[] args) {		
		DefaultHandler handler = new MusicXmlHandler(music);
		
		MusicReader lReader = new MusicReader(dataPath);
		lReader.read(handler);
		System.out.println(music);
	}	

}
