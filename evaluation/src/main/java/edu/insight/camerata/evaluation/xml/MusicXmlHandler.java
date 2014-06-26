package edu.insight.camerata.evaluation.xml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MusicXmlHandler extends DefaultHandler {

	private String currentPartId = null;
	private Part currentPart = null;
	private Measure currentMeasure = null;
	private MeasureAttributes currentMeasureAttributes = null;
	private Note currentNote = null;	
	private Clef currentClef = new Clef();
	private Music music;
	private boolean takeText = false;
	private String tagStringValue = null;
	private int noteNumber = 0;


	public MusicXmlHandler(Music music) {
		this.music = music;		
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {

		if (qName.equalsIgnoreCase("score-partwise")) {
		}

		if (qName.equalsIgnoreCase("part-list")) {
		}

		if (qName.equalsIgnoreCase("score-part")) {
			currentPart = new Part();			
			currentPart.partId = attributes.getValue("id");
			music.musicPartMap.put(currentPart.partId, currentPart);			
		}

		if (qName.equalsIgnoreCase("part-name")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("part-abbreviation")) {
			takeText = true;					
		}

		if (qName.equalsIgnoreCase("score-instrument")) {
			ScoreInstrument scoreInstrument = new ScoreInstrument();
			scoreInstrument.id = attributes.getValue("id");
			currentPart.scoreInstrument = scoreInstrument;
		}

		if (qName.equalsIgnoreCase("instrument-name")) {
			takeText = true;					
		}

		if (qName.equalsIgnoreCase("midi-instrument")) {
			MidiInstrument midiInstrument = new MidiInstrument();
			midiInstrument.id = attributes.getValue("id");
			currentPart.midiInstrument = midiInstrument;
		}

		if (qName.equalsIgnoreCase("midi-channel")) {
			takeText = true;					
		}
		if (qName.equalsIgnoreCase("midi-program")) {
			takeText = true;					
		}
		if (qName.equalsIgnoreCase("volume")) {
			takeText = true;					
		}
		if (qName.equalsIgnoreCase("pan")) {
			takeText = true;					
		}

		if (qName.equalsIgnoreCase("part")) {
			currentPartId = attributes.getValue("id");
			currentPart = music.musicPartMap.get(currentPartId);			
		}

		if (qName.equalsIgnoreCase("measure")) {
			noteNumber = 0;
			currentMeasure = new Measure();			
			currentMeasure.measureNumber = Integer.parseInt(attributes.getValue("number"));
			currentPart.measures.put(String.valueOf(currentMeasure.measureNumber), currentMeasure);
		}

		if (qName.equalsIgnoreCase("attributes")) {
			currentMeasureAttributes = new MeasureAttributes();
			currentMeasure.attributes = currentMeasureAttributes;
		}

		if (qName.equalsIgnoreCase("divisions")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("key")) {
			currentMeasureAttributes.key = new Key();			
		}

		if (qName.equalsIgnoreCase("fifths")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("mode")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("time")) {
			currentMeasureAttributes.time= new Time();			
		}

		if (qName.equalsIgnoreCase("beats")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("beat-type")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("staves")) {
			takeText = true;
		}


		if (qName.equalsIgnoreCase("clef")) {
			currentClef = new Clef();
			if(attributes.getValue("number") != null)
				currentClef.clefNumber = Integer.parseInt(attributes.getValue("number"));
			currentMeasureAttributes.clefs.add(currentClef);			
		}

		if (qName.equalsIgnoreCase("sign")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("line")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("backup")) {

		}
		
		if (qName.equalsIgnoreCase("forward")) {

		}

		if (qName.equalsIgnoreCase("note")) {
			noteNumber++;
			currentNote = new Note();
			currentMeasure.notes.put(noteNumber, currentNote);
		}

		
		if (qName.equalsIgnoreCase("pitch")) {
			currentNote.pitch = new Pitch();			
		}

		if (qName.equalsIgnoreCase("rest")) {
			currentNote.pitch = new Pitch();
			currentNote.pitch.rest = true;
		}	

		if (qName.equalsIgnoreCase("step")) {
			takeText = true;
		}

		if (qName.equalsIgnoreCase("alter")) {
			takeText = true;				
		}	

		if (qName.equalsIgnoreCase("octave")) {
			takeText = true;				
		}

		if (qName.equalsIgnoreCase("duration")) {
			takeText = true;				
		}	

		if (qName.equalsIgnoreCase("voice")) {
			takeText = true;				
		}	

		if (qName.equalsIgnoreCase("type")) {
			takeText = true;				
		}	

		if (qName.equalsIgnoreCase("stem")) {			
			takeText = true;				
		}	
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("part-name"))
			currentPart.partName = tagStringValue;
		if(qName.equalsIgnoreCase("part-abbreviation"))
			currentPart.partAbbreviation = tagStringValue;
		if(qName.equalsIgnoreCase("instrument-name"))
			currentPart.scoreInstrument.instrumentName = tagStringValue;
		if(qName.equalsIgnoreCase("midi-channel"))
			currentPart.midiInstrument.midiChannel = tagStringValue;
		if(qName.equalsIgnoreCase("midi-program"))
			currentPart.midiInstrument.midiProgram = tagStringValue;
		if(qName.equalsIgnoreCase("volume"))
			currentPart.midiInstrument.volume = tagStringValue;
		if(qName.equalsIgnoreCase("pan"))
			currentPart.midiInstrument.pan = tagStringValue;
		if(qName.equalsIgnoreCase("divisions"))
			currentMeasureAttributes.divisions = Double.parseDouble(tagStringValue.trim());
		if(qName.equalsIgnoreCase("fifths"))
			currentMeasureAttributes.key.fifths = Double.parseDouble(tagStringValue.trim());
		if(qName.equalsIgnoreCase("mode"))
			currentMeasureAttributes.key.mode = tagStringValue;
		if(qName.equalsIgnoreCase("beats"))
			currentMeasureAttributes.time.beats = Double.parseDouble(tagStringValue.trim());
		if(qName.equalsIgnoreCase("beat-type"))
			currentMeasureAttributes.time.beatType = Double.parseDouble(tagStringValue);
		if(qName.equalsIgnoreCase("beat-type"))
			currentMeasureAttributes.staves = Double.parseDouble(tagStringValue);		
		if(qName.equalsIgnoreCase("sign"))
			currentClef.sign = tagStringValue;
		if(qName.equalsIgnoreCase("line"))
			currentClef.line = tagStringValue;
		if(qName.equalsIgnoreCase("step"))
			currentNote.pitch.step = tagStringValue;
		if(qName.equalsIgnoreCase("alter"))
			currentNote.pitch.alter = tagStringValue;
		if(qName.equalsIgnoreCase("octave"))
			currentNote.pitch.octave = tagStringValue;
		if(qName.equalsIgnoreCase("duration"))
			currentNote.duration = Double.parseDouble(tagStringValue);
		if(qName.equalsIgnoreCase("voice"))
			currentNote.voice = tagStringValue;
		if(qName.equalsIgnoreCase("type"))
			currentNote.type = tagStringValue;
		if(qName.equalsIgnoreCase("stem"))
			currentNote.stem = tagStringValue;



	}

	public void characters(char ch[], int start, int length) throws SAXException {
		if (takeText) {
			tagStringValue = new String(ch, start, length);
			takeText = false;
		} 
	}

}