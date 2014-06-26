package edu.insight.camerata.evaluation.xml;

import java.util.LinkedHashMap;
import java.util.Map;


public class Music {

	public Map<String, Part> musicPartMap;

	public Music() {
		musicPartMap = new LinkedHashMap<String, Part>();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\tMusic:\t\t");
		buffer.append("\n There are " + musicPartMap.size() + " music parts. It is a score-part based music.\n");
		for(String key : musicPartMap.keySet())
			buffer.append(musicPartMap.get(key) + "\n ------------------------------ \n");		
		return buffer.toString().trim();
	}

}
