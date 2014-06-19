package edu.insight.camerata.extractors;
import java.util.ArrayList;
import java.util.List;

import edu.insight.camerata.evaluation.xml.Clef;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.MeasureAttributes;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.Part;


public class ClefExtractor {

	public static List<Measure> getClef(Music music, int clefNo, String sign, String line){
		List<Measure> clefMeasures = new ArrayList<Measure>();

		Clef req_clef = new Clef();
		req_clef.clefNumber = clefNo;
		req_clef.line = line;
		req_clef.sign = sign;

		for (String key : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(key);
			for (String key1 : part.measures.keySet()) {
				Measure measure = part.measures.get(key1);
				if(measure.attributes != null){
					MeasureAttributes attributes = measure.attributes;
					if (attributes.clefs.contains(req_clef)){					
						System.out.println("Measure contains required clef");
						clefMeasures.add(measure);
					}
				}
			}
		}
		System.out.println(clefMeasures);
		return clefMeasures;	
	}
}
