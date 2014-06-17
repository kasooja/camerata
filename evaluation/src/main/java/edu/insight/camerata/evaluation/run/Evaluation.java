package edu.insight.camerata.evaluation.run;

import org.xml.sax.helpers.DefaultHandler;

import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.MusicReader;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.MusicXmlHandler;

public class Evaluation {

	private String start_time_sig;
	private String end_time_sig;
	private double start_divs;
	private double end_divs;
	private static Music music1 = new Music();
	private static String dataPath =  "src/main/resources/data/training/f4.xml";
	private Part first_part;
	private Measure first_measure;
	private Part end_part;
	private Measure end_measure;
	
	public void get_ans(String question) {
		//String entity = "B";
		//String type = 
		// To fetch start time signature
		for (String key : music1.musicPartMap.keySet()) {
			first_part = music1.musicPartMap.get(key);
			break;
		}
		for (String key : first_part.measures.keySet()) {
			first_measure = first_part.measures.get(key);
			break;
		}
		start_time_sig = first_measure.attributes.time.beats + "/" + first_measure.attributes.time.beatType;
		System.out.println("Start Time Signature : " + start_time_sig);

		// To fetch end time signature
		for (String key : music1.musicPartMap.keySet()) {
			end_part = music1.musicPartMap.get(key);
		}
		for (String key : end_part.measures.keySet()) {
			end_measure = end_part.measures.get(key);
			break;
		}
		end_time_sig = end_measure.attributes.time.beats + "/" + end_measure.attributes.time.beatType;
		System.out.println("End Time Signature : " + end_time_sig);		

		// To fetch start divisions
		start_divs = first_measure.attributes.time.beatType / 4;
		System.out.println("Start Divisions : " + start_divs);		

		// To fetch end divisions
		end_divs = end_measure.attributes.time.beatType / 4;
		System.out.println("End Divisions : " + end_divs);		

	}
	
	public static void main(String []args){
		DefaultHandler handler = new MusicXmlHandler(music1);
		MusicReader lReader = new MusicReader(dataPath);
		lReader.read(handler);
		String question = "demisemiquaver B";		
		Answers ans = new Answers();
		ans.get_ans(question);		
	}
	
}
