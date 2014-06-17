package edu.insight.camerata.evaluation.xml;

import org.xml.sax.helpers.DefaultHandler;


public class Answers {
	String start_time_sig;
	String end_time_sig;
	double start_divs;
	double end_divs;

	public static Music music1 = new Music();
	public static String dataPath1 =  "src/main/resources/data/training/f4.xml";

	Part first_part;
	Measure first_measure;
	Part end_part;
	Measure end_measure;


	public void get_ans(){

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
		/* Object creation */
		DefaultHandler handler1 = new XmlHandler(music1);
		MusicReader lReader1 = new MusicReader(dataPath1);
		lReader1.read(handler1);

		Answers ans = new Answers();

		ans.get_ans(); 
	}
}
