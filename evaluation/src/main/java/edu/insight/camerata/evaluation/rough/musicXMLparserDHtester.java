package edu.insight.camerata.evaluation.rough;



import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dorien Herremans on 05/02/15.
 */
public class musicXMLparserDHtester {

	public static void main (String[]args)throws IOException {

		System.out.println("Reading in file... ");
		String[] songSequence = null;
		String[] songSequenceParsed = null;
		//String filename = "src/main/resources/data/camerata_questions_2014/cutting_galliard_11.xml";
		String filename = "src/main/resources/data/camerata_questions_2014/scarlatti_a_se_florindo.xml";

		musicXMLparserDH parser = new musicXMLparserDH(filename);

		//prints out the note sounding at the same slice (each division of the musicxml file
		String[] flatSong = parser.parseMusicXML();
		ArrayList<Note> notes = parser.notesOfSong;
	
		
		//print out the songI j
		for (int i = 0; i < flatSong.length; i++) {
			System.out.println(flatSong[i]);
		}

		//returns an ArrayList containing all the note objects
		ArrayList<Note> songSequenceOfNoteObjects = parser.getNotesOfSong();



	}
}
