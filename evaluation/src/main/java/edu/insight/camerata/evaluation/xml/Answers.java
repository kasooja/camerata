package edu.insight.camerata.evaluation.xml;

import java.util.Set;

import org.xml.sax.helpers.DefaultHandler;


public class Answers {
	String start_time_sig;
	String end_time_sig;
	String start_divs;
	String end_divs;

	public static Music music1 = new Music();
	public static String dataPath1 =  "src/main/resources/data/training/f5.xml";

	Part first;
	Measure first1;
	Part end;
	Measure end1;


	public void get_ans(){

		// To fetch start time signature
		for (String key : music1.musicPartMap.keySet()) {
			first = music1.musicPartMap.get(key);
			break;
		}
		for (String key : first.measures.keySet()) {
			first1 = first.measures.get(key);
			break;
		}
		start_time_sig = first1.attributes.time.beats + "/" + first1.attributes.time.beatType;
		System.out.println("Start Time Signature : " + start_time_sig);

		// To fetch end time signature
		for (String key : music1.musicPartMap.keySet()) {
			end = music1.musicPartMap.get(key);
		}
		for (String key : end.measures.keySet()) {
			end1 = end.measures.get(key);
			break;
		}
		end_time_sig = end1.attributes.time.beats + "/" + end1.attributes.time.beatType;
		System.out.println("End Time Signature : " + end_time_sig);		

		// To fetch start divisions

		double start_no_divisions = first1.attributes.divisions;
		double count = 0;
		for (Integer key : first1.notes.keySet()){
			Note temp = first1.notes.get(key);
			count = count + temp.duration;
		}
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
