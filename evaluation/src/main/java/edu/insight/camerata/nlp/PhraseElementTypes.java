package edu.insight.camerata.nlp;

public enum PhraseElementTypes {

	Cadence, Step;
	
	public static PhraseElementTypes getEnum(String type){
		if(type.equalsIgnoreCase(Cadence.toString()))
			return Cadence;
		if(type.equalsIgnoreCase(Step.toString()))
			return Step;		
		return null;
	}
	
}
