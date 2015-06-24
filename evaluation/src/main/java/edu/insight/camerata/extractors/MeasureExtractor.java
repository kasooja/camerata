package edu.insight.camerata.extractors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.insight.camerata.evaluation.run.Pair;
import edu.insight.camerata.evaluation.xml.Clef;
import edu.insight.camerata.evaluation.xml.Measure;
import edu.insight.camerata.evaluation.xml.MeasureAttributes;
import edu.insight.camerata.evaluation.xml.Music;
import edu.insight.camerata.evaluation.xml.Note;
import edu.insight.camerata.evaluation.xml.Part;
import edu.insight.camerata.evaluation.xml.Pitch;

public class MeasureExtractor {

	public static Set<Measure> getMeasures(Music music, Note note, String instrument, String clef) {
		int count = 0;
		MeasureAttributes ma = null;
		Pitch pitch = note.pitch;
		String step = null;
		String octave = null; 
		String alter = null; 

		boolean rest = false;

		if(pitch != null) { 
			step = pitch.step;			
			octave = pitch.octave; 
			alter = pitch.alter; 
			rest = pitch.rest;
		}

		boolean dot = note.dot;
		String type = note.type;
		String staff = note.staff;

		boolean stepEqual = false;		
		boolean octaveEqual = false;
		boolean alterEqual = false;
		boolean restEqual = false;
		boolean dotEqual = false;
		boolean typeEqual = false;
		boolean instrumentEqual = false;
		boolean staffEqual = false;
		boolean clefEqual = false;
		//boolean pitchEqual = false;

		//if(pitch == null)
		//	pitchEqual = true;

		if(step == null)
			stepEqual = true;
		if(octave == null)
			octaveEqual = true;
		if(alter == null)
			alterEqual = true;
		if(rest == false)
			restEqual = true;		
		if(dot == false)
			dotEqual = true;
		if(type == null)
			typeEqual = true;
		if(instrument == null)
			instrumentEqual = true;
		if(staff == null)
			staffEqual = true;
		if(clef == null)
			clefEqual = true;


		Set<Measure> noteMeasures = new HashSet<Measure>();
		for (String partNumber : music.musicPartMap.keySet()) {
			Part part = music.musicPartMap.get(partNumber);
			if(instrument != null) 
				instrumentEqual = part.scoreInstrument.instrumentName.equalsIgnoreCase(instrument.trim());		
			for (String measureNumber : part.measures.keySet()) {	
				if("17".equals(measureNumber.trim())){
					System.out.println("debug");
				}
				Measure measure = part.measures.get(measureNumber);
				if(measure.attributes != null){
					if(measure.attributes.time==null){
						measure.attributes.time = ma.time;
					}
					if(measure.attributes.divisions==0.0){
						measure.attributes.divisions = ma.divisions;
					}
					ma = measure.attributes;
				}
				for(Clef clefMa : ma.clefs){
					if(clefMa.sign.equalsIgnoreCase(clef))
						clefEqual = true;
				}
				//	boolean consecutive = false;

				for (Integer noteNumber: measure.notes.keySet()) {
					//	int answerPairsAdded = measure.answerPairs.size(); 
					Note notee = measure.notes.get(noteNumber);
					if(notee.backUp || notee.forward) {
						continue;
					}
					if(step != null && notee.pitch.step != null)
						stepEqual = notee.pitch.step.equalsIgnoreCase(step);
					if(alter != null && notee.pitch.alter != null)
						alterEqual = notee.pitch.alter.equalsIgnoreCase(alter);
					if(octave != null && notee.pitch.octave != null)
						octaveEqual = notee.pitch.octave.equalsIgnoreCase(octave);
					if(dot != false) {
						dotEqual = dot == notee.dot;
					}					
					restEqual = rest == notee.pitch.rest;
					if(type != null && notee.type != null){
						typeEqual = notee.type.equalsIgnoreCase(type);
						if(note.dot != notee.dot){
							typeEqual = false;									
						}						
					}
					if(staff != null && notee.staff != null)
						staffEqual = notee.staff.equalsIgnoreCase(staff);

					if(stepEqual && octaveEqual && alterEqual && restEqual && dotEqual && typeEqual && instrumentEqual && 
							staffEqual && clefEqual) {

						measure.computedAttributes = ma;
						Pair answerPair = new Pair();						
						//						if(consecutive == true){
						//							answerPair = measure.answerPairs.get(answerPairsAdded - 1);
						//							answerPair.endNote = notee;	
						//						} else {						
						answerPair.startNote = notee;
						answerPair.endNote = notee;
						measure.answerPairs.add(answerPair);							
						//		}
						noteMeasures.add(measure);
						System.out.println(measure.measureNumber);
						System.out.println(notee);
						count++;
						//						consecutive = true;
					}// else {
					//consecutive = false;
					//	}
				}
			}			
		}	

		System.out.println(count);
		return noteMeasures;	
	}

	public static Set<Pair> getMeasuresForMultipleNotes(Music music, List<Note> notes, String instrument, String clef) {
		MeasureAttributes ma = null;
		Set<Note> firstNotesVisited = new HashSet<Note>();
		Set<Pair> answerPairs = new HashSet<Pair>();
		int lastSize = 0;
		while(true) {			
			for (String partNumber : music.musicPartMap.keySet()) {
				Part part = music.musicPartMap.get(partNumber);
				boolean instrumentEqual = false;					
				if(instrument != null) 
					instrumentEqual = part.scoreInstrument.instrumentName.equalsIgnoreCase(instrument.trim());
				int i = 0;
				Pair answerPair = new Pair();
				int noteCount = 0;
				int noteCountOfLastNoteFound = -1;
				for (String measureNumber : part.measures.keySet()) {
					if(measureNumber.equals("1")){
						System.out.println();
					}
					Measure measure = part.measures.get(measureNumber);
					if(measure.attributes != null){
						if(measure.attributes.time==null){
							measure.attributes.time = ma.time;
						}
						if(measure.attributes.divisions==0.0){
							measure.attributes.divisions = ma.divisions;
						}

						ma = measure.attributes;
					}
					//	boolean consecutive = false;				
					for (Integer noteNumber: measure.notes.keySet()) {
						//	int answerPairsAdded = measure.answerPairs.size();
						Note noteToFind = notes.get(i);
						noteCount++;

						Pitch pitch = noteToFind.pitch;
						String step = null;
						String octave = null; 
						String alter = null; 

						boolean rest = false;

						if(pitch != null) { 
							step = pitch.step;			
							octave = pitch.octave; 
							alter = pitch.alter; 
							rest = pitch.rest;
						}

						boolean dot = noteToFind.dot;
						String type = noteToFind.type;
						String staff = noteToFind.staff;

						boolean stepEqual = false;		
						boolean octaveEqual = false;
						boolean alterEqual = false;
						boolean restEqual = false;
						boolean dotEqual = false;
						boolean typeEqual = false;
						boolean staffEqual = false;
						boolean clefEqual = false;

						if(step == null)
							stepEqual = true;
						if(octave == null)
							octaveEqual = true;
						if(alter == null)
							alterEqual = true;
						if(rest == false)
							restEqual = true;		
						if(dot == false)
							dotEqual = true;
						if(type == null)
							typeEqual = true;
						if(instrument == null)
							instrumentEqual = true;
						if(staff == null)
							staffEqual = true;
						if(clef == null)
							clefEqual = true;

						for(Clef clefMa : ma.clefs){
							if(clefMa.sign.equalsIgnoreCase(clef))
								clefEqual = true;
						}					

						Note notee = measure.notes.get(noteNumber);
						if(notee.backUp || notee.forward) {
							noteCount--;
							continue;
						}

						if(step != null && notee.pitch.step != null)
							stepEqual = notee.pitch.step.equalsIgnoreCase(step);
						if(alter != null && notee.pitch.alter != null)
							alterEqual = notee.pitch.alter.equalsIgnoreCase(alter);
						if(octave != null && notee.pitch.octave != null)
							octaveEqual = notee.pitch.octave.equalsIgnoreCase(octave);
						if(dot != false) {
							dotEqual = dot == notee.dot;
						}					
						restEqual = rest == notee.pitch.rest;
						if(type != null && notee.type != null){
							typeEqual = notee.type.equalsIgnoreCase(type);
							if(noteToFind.dot != notee.dot){
								typeEqual = false;									
							}						
						}
						if(staff != null && notee.staff != null)
							staffEqual = notee.staff.equalsIgnoreCase(staff);

						if(stepEqual && octaveEqual && alterEqual && restEqual && dotEqual && typeEqual && instrumentEqual && 
								staffEqual && clefEqual) {			
							//boolean firstNoteNotVisited = true;
							if(i==0){
								if(!firstNotesVisited.contains(notee)){
									answerPair.startNote = notee;
									measure.partNumber = partNumber;
									notee.measure = measure;									
									noteCountOfLastNoteFound = noteCount - 1; firstNotesVisited.add(notee);
								}

							} 
							if(i>0){
								measure.partNumber = partNumber;
								notee.measure = measure;								
								answerPair.endNote = notee;								
							}
							if(i+1>=notes.size()){								
								//	measure.answerPairs.add(answerPair);
								//finalMeasures.add(measure);
								if(answerPair.endNote==null){
									answerPair.endNote = answerPair.startNote;
								}
								if(answerPair.endNote!=null && answerPair.startNote!=null){
									answerPairs.add(answerPair);		
								}
								//firstNotesVisited.add(notee);
								answerPair = new Pair();										
							}
							if((noteCount - noteCountOfLastNoteFound == 1)){
								i = i+1;						
								if(i>=notes.size()){
									i = 0;
								}
							} 
							//						else {
							//							i = i-1;
							//						}

							measure.computedAttributes = ma;
							//	noteMeasures.add(measure);
							System.out.println(measure.measureNumber);
							System.out.println(notee);
							if(i!=0){
								noteCountOfLastNoteFound = noteCount;
							}
						} else if(i!=0){
							i = 0;
							answerPair = new Pair();
						}
					}

				}
			}
			if(firstNotesVisited.size()==lastSize){
				break;
			} else {
				lastSize = firstNotesVisited.size();
			}
		}
		return answerPairs;
	}

	public static Set<Pair> getMeasuresForMultipleNotesUsingPositions(Music music, List<Note> notes, String instrument, String clef) {
		MeasureAttributes ma = null;
		Set<Note> firstNotesVisited = new HashSet<Note>();
		Set<Pair> answerPairs = new HashSet<Pair>();
		int lastSize = 0;
		while(true) {			
			for (String partNumber : music.musicPartMap.keySet()) {
				Part part = music.musicPartMap.get(partNumber);
				boolean instrumentEqual = false;					
				if(instrument != null) 
					instrumentEqual = part.scoreInstrument.instrumentName.equalsIgnoreCase(instrument.trim());
				int i = 0;
				Pair answerPair = new Pair();
				int noteCount = 0;
				Note lastFoundNote = null;
				//double lastFoundNoteStartPosition = -1;
				for (String measureNumber : part.measures.keySet()) {
					if(measureNumber.equals("1")){
						System.out.println();
					}
					Measure measure = part.measures.get(measureNumber);
					ma = measure.computedAttributes;
					for (Integer noteNumber: measure.notes.keySet()) {
						Note noteToFind = notes.get(i);
						noteCount++;

						Pitch pitch = noteToFind.pitch;
						String step = null;
						String octave = null; 
						String alter = null; 

						boolean rest = false;

						if(pitch != null) { 
							step = pitch.step;			
							octave = pitch.octave; 
							alter = pitch.alter; 
							rest = pitch.rest;
						}

						boolean dot = noteToFind.dot;
						String type = noteToFind.type;
						String staff = noteToFind.staff;

						boolean stepEqual = false;		
						boolean octaveEqual = false;
						boolean alterEqual = false;
						boolean restEqual = false;
						boolean dotEqual = false;
						boolean typeEqual = false;
						boolean staffEqual = false;
						boolean clefEqual = false;

						if(step == null)
							stepEqual = true;
						if(octave == null)
							octaveEqual = true;
						if(alter == null)
							alterEqual = true;
						if(rest == false)
							restEqual = true;		
						if(dot == false)
							dotEqual = true;
						if(type == null)
							typeEqual = true;
						if(instrument == null)
							instrumentEqual = true;
						if(staff == null)
							staffEqual = true;
						if(clef == null)
							clefEqual = true;

						for(Clef clefMa : ma.clefs){
							if(clefMa.sign.equalsIgnoreCase(clef))
								clefEqual = true;
						}					

						Note notee = measure.notes.get(noteNumber);
						if(notee.backUp || notee.forward) {
							noteCount--;
							continue;
						}

						if(step != null && notee.pitch.step != null)
							stepEqual = notee.pitch.step.equalsIgnoreCase(step);
						if(alter != null && notee.pitch.alter != null)
							alterEqual = notee.pitch.alter.equalsIgnoreCase(alter);
						if(octave != null && notee.pitch.octave != null)
							octaveEqual = notee.pitch.octave.equalsIgnoreCase(octave);
						if(dot != false) {
							dotEqual = dot == notee.dot;
						}					
						restEqual = rest == notee.pitch.rest;
						if(type != null && notee.type != null){
							typeEqual = notee.type.equalsIgnoreCase(type);
							if(noteToFind.dot != notee.dot){
								typeEqual = false;									
							}						
						}
						if(staff != null && notee.staff != null)
							staffEqual = notee.staff.equalsIgnoreCase(staff);

						if(stepEqual && octaveEqual && alterEqual && restEqual && dotEqual && typeEqual && instrumentEqual && 
								staffEqual && clefEqual) {	
							boolean firstNote = false;
							if(i==0) {
								if(!firstNotesVisited.contains(notee)){
									answerPair.startNote = notee;									
									notee.measure = measure;
									lastFoundNote = notee;  
									firstNotesVisited.add(notee);	
									firstNote = true;
								}
							} 
							if(i>0){
								measure.partNumber = partNumber;
								notee.measure = measure;								
								answerPair.endNote = notee;								
							}
							if(i+1>=notes.size()){								
								if(answerPair.endNote==null){
									answerPair.endNote = answerPair.startNote;
								}
								if(answerPair.endNote!=null && answerPair.startNote!=null){

									if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber + 1){ 
										if(notee.startPosition == 1.0){
											answerPairs.add(answerPair);			
										}
									} if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber) {
										answerPairs.add(answerPair);
									}
								}						
								answerPair = new Pair();	
								i = 0;
							}
							if(!firstNote){
								if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber) {
									if(notee.startPosition == lastFoundNote.startPosition + lastFoundNote.questionStdDuration){
										i = i+1;						
										if(i>=notes.size()){
											i = 0;
										}
									}
								} else if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber + 1){ 
									if(notee.startPosition == 1.0){
										i = i+1;						
										if(i>=notes.size()){
											i = 0;
										}							
									}
								}
							} 
							if(firstNote){
								i = i+1;
								if(i>=notes.size()){
									i = 0;
								}							
							}
							System.out.println(measure.measureNumber);
							System.out.println(notee);
							if(i!=0){
								lastFoundNote = notee;
							}
						}

						//else if(i!=0){
						//i = 0;
						//answerPair = new Pair();
						//}
					}

				}
			}
			if(firstNotesVisited.size()==lastSize){
				break;
			} else {
				lastSize = firstNotesVisited.size();
			}
		}
		return answerPairs;
	}


	public static boolean exits(Double[] arr, double val){
		boolean exists = false;
		for(double v : arr){
			if(v == val)
				return true;
		}
		return exists;
	}

	public static Set<Pair> getMeasuresForMultipleNotesUsingPosMap(Music music, List<Note> notes, String instrument, String clef, String bars) {
		int startBar = -1;
		int endBar = -1;

		if(bars!=null){
			String[] split = bars.split("\t");
			startBar = Integer.parseInt(split[0].trim());
			if(split.length == 2){
				endBar = Integer.parseInt(split[1].trim());
			}
		}

		Set<Note> firstNotesVisited = new HashSet<Note>();
		Set<Pair> answerPairs = new HashSet<Pair>();
		int lastSize = 0;
		while(true) {			
			for (String partNumber : music.musicPartMap.keySet()) {
				Part part = music.musicPartMap.get(partNumber);
				boolean instrumentEqual = false;					
				if(instrument != null) 
					instrumentEqual = part.scoreInstrument.instrumentName.equalsIgnoreCase(instrument.trim());
				int i = 0;
				Pair answerPair = new Pair();
				Note lastFoundNote = null;
				for (String measureNumber : part.measures.keySet()) {
					int measureN = Integer.parseInt(measureNumber.trim());
					if(bars!=null){
						String[] split = bars.split("\t");
						if(split.length==2){
							if(!(measureN>=startBar && measureN<=endBar)){
								continue;
							}
						} else if(split.length==1){
							if(measureN!=startBar){
								continue;
							}
						}
					}
					//	double nextNoteStartPosition = 1.0;
					int currentArrayPosition = 0;
					//	int currentArrayChangedPosition = -1;

					Measure measure = part.measures.get(measureNumber);
					//System.out.println(measureNumber);
					if(measureNumber.equals("20")){
						//	System.out.println();
					}
					//ma = measure.computedAttributes;
					Double[] positionsArray = new Double[measure.positionNotes.keySet().size()];
					positionsArray = measure.positionNotes.keySet().toArray(positionsArray);
					Double maxPos = positionsArray[positionsArray.length-1];
					while(currentArrayPosition<=positionsArray.length-1){	
						List<Note> measureNotes = measure.positionNotes.get(positionsArray[currentArrayPosition]);
						for(Note notee : measureNotes) {
							if(lastFoundNote!=null && i!=0){
								if(notee.staff!=null && lastFoundNote.staff!=null){
									if(!notee.staff.trim().equalsIgnoreCase(lastFoundNote.staff.trim())){
										continue;
									}
								}

								if(!notee.backUp && !notee.forward){
									if(notee.measure.measureNumber == lastFoundNote.measure.measureNumber){
										if(exits(positionsArray, (lastFoundNote.endPosition + 1.0)) && 
												(lastFoundNote.endPosition + 1.0)<=maxPos){
											if(notee.startPosition<(lastFoundNote.endPosition + 1.0)){
												continue;
											} 
										}
										if(notee.startPosition !=  (lastFoundNote.endPosition + 1.0)){											
											i = 0;
											answerPair = new Pair();
											lastFoundNote = null;
										}

									} else if(notee.measure.measureNumber != lastFoundNote.measure.measureNumber + 1){
										i = 0;
										answerPair = new Pair();
										lastFoundNote = null;								
									}
								}
							}

							Note noteToFind = notes.get(i);
							Pitch pitch = noteToFind.pitch;
							String step = null;
							String octave = null; 
							String alter = null; 


							boolean rest = false;

							if(pitch != null) { 
								step = pitch.step;			
								octave = pitch.octave; 
								alter = pitch.alter; 
								rest = pitch.rest;
							}

							boolean dot = noteToFind.dot;
							String type = noteToFind.type;
							String staff = noteToFind.staff;

							boolean stepEqual = false;		
							boolean octaveEqual = false;
							boolean alterEqual = false;
							boolean restEqual = false;
							boolean dotEqual = false;
							boolean typeEqual = false;
							boolean staffEqual = false;
							boolean clefEqual = false;

							if(step == null)
								stepEqual = true;
							if(octave == null)
								octaveEqual = true;
							if(alter == null)
								alterEqual = true;
							if(rest == false)
								restEqual = true;		
							if(dot == false)
								dotEqual = true;
							if(type == null)
								typeEqual = true;
							if(instrument == null)
								instrumentEqual = true;
							if(staff == null)
								staffEqual = true;
							if(clef == null)
								clefEqual = true;

							if(notee.backUp || notee.forward) {					
								continue;
							}

							if(clef != null && notee.clef != null)
								clefEqual = notee.clef.sign.equalsIgnoreCase(clef);

							if(step != null && notee.pitch.step != null)
								stepEqual = notee.pitch.step.equalsIgnoreCase(step);
							if(alter != null && notee.pitch.alter != null)
								alterEqual = notee.pitch.alter.equalsIgnoreCase(alter);
							if(octave != null && notee.pitch.octave != null)
								octaveEqual = notee.pitch.octave.equalsIgnoreCase(octave);
							if(dot != false) {
								dotEqual = dot == notee.dot;
							}					
							restEqual = rest == notee.pitch.rest;
							if(type != null && notee.type != null){
								typeEqual = notee.type.equalsIgnoreCase(type);
								if(noteToFind.dot != notee.dot){
									typeEqual = false;									
								}						
							}
							if(staff != null && notee.staff != null)
								staffEqual = notee.staff.equalsIgnoreCase(staff);

							if(stepEqual && octaveEqual && alterEqual && restEqual && dotEqual && typeEqual && instrumentEqual && 
									staffEqual && clefEqual) {							
								if(i==0) {
									if(!firstNotesVisited.contains(notee)){
										answerPair.startNote = notee;							
										lastFoundNote = notee;  
										firstNotesVisited.add(notee);									
										//nextNoteStartPosition = lastFoundNote.endPosition + 1.0;
									}
								} 
								if(i>0){																
									answerPair.endNote = notee;								
								}
								if(i+1>=notes.size()){
									if(answerPair.endNote==null){
										answerPair.endNote = answerPair.startNote;
									}
									if(answerPair.endNote!=null && answerPair.startNote!=null){
										if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber + 1){ 
											if(notee.startPosition == 1.0) {
												if(notee.staff==null && lastFoundNote.staff==null){
													answerPairs.add(answerPair);

												} else if(notee.staff.trim().equalsIgnoreCase(lastFoundNote.staff.trim())){
													answerPairs.add(answerPair);

												}
											}
										} if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber) {
											if(notee.staff==null && lastFoundNote.staff==null){
												answerPairs.add(answerPair);										

											} else if(notee.staff.trim().equalsIgnoreCase(lastFoundNote.staff.trim())){
												answerPairs.add(answerPair);

											}
										}
									}
									answerPair = new Pair();
									lastFoundNote = null;
									i = 0;
								} else {
									if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber) {
										if((lastFoundNote.endPosition + 1)	== notee.startPosition) {
											i = i+1;
											//currentArrayPosition = currentArrayPosition + 1;
											lastFoundNote = notee;

											break;
										}
										if(lastFoundNote == notee){
											i = i+1;
											//currentArrayPosition = currentArrayPosition + 1;											
											break;
										}
									} 
									if(lastFoundNote!=null && notee.measure.measureNumber == 1 + lastFoundNote.measure.measureNumber) {
										if(notee.startPosition == 1.0){
											i = i+1;
											//currentArrayPosition = currentArrayPosition + 1;
											lastFoundNote = notee;											

											break;
										} 
									}
								}
							} else {
								i = 0; 
								answerPair = new Pair();
								lastFoundNote = null;						
							}
						}
						currentArrayPosition = currentArrayPosition + 1;						
					}



					//						boolean firstNote = false;
					//						if(i+1>=notes.size()){								
					//							if(answerPair.endNote==null){
					//								answerPair.endNote = answerPair.startNote;
					//							}
					//							if(answerPair.endNote!=null && answerPair.startNote!=null){
					//
					//								if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber + 1){ 
					//									if(notee.startPosition == 1.0){
					//										answerPairs.add(answerPair);			
					//									}
					//								} if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber) {
					//									answerPairs.add(answerPair);
					//								}
					//							}						
					//							answerPair = new Pair();	
					//							i = 0;
					//						}
					//						if(!firstNote){
					//							if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber) {
					//								if(notee.startPosition == lastFoundNote.startPosition + lastFoundNote.questionStdDuration){
					//									i = i+1;						
					//									if(i>=notes.size()){
					//										i = 0;
					//									}
					//								}
					//							} else if(lastFoundNote!=null && notee.measure.measureNumber == lastFoundNote.measure.measureNumber + 1){ 
					//								if(notee.startPosition == 1.0){
					//									i = i+1;						
					//									if(i>=notes.size()){
					//										i = 0;
					//									}							
					//								}
					//							}
					//						} 
					//						if(firstNote){
					//							i = i+1;
					//							if(i>=notes.size()){
					//								i = 0;
					//							}							
					//						}
					//						System.out.println(measure.measureNumber);
					//						System.out.println(notee);
					//						if(i!=0){
					//							lastFoundNote = notee;
					//						}
					//					}

					//else if(i!=0){
					//i = 0;
					//answerPair = new Pair();
					//}
					//				}

					//			}
					//		}

				}
			}

			if(firstNotesVisited.size()==lastSize){
				break;
			} else {
				lastSize = firstNotesVisited.size();
			}
		}
		return answerPairs;
	}
}
