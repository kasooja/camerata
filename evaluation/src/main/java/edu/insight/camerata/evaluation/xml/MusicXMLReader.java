package edu.insight.camerata.evaluation.xml;

import org.xml.sax.helpers.DefaultHandler;

public class MusicXMLReader {	
	public static Music music = new Music();
	public static String dataPath =  "src/main/resources/data/training/f1.xml";

	public static void main(String[] args) {		
		DefaultHandler handler = new XmlHandler(music);
		MusicReader lReader = new MusicReader(dataPath);
		lReader.read(handler);
		System.out.println(music);
	}	

}
