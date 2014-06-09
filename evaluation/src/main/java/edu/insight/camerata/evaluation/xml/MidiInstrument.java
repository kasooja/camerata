package edu.insight.camerata.evaluation.xml;

public class MidiInstrument {

	public String id;
	public String midiChannel;
	public String midiProgram;
	public String volume;
	public String pan;
	
	@Override
	public String toString() {
		return "\tMidiInstrument: " + id + "\t" + midiChannel + "\t" + midiProgram + "\t" + volume + "\t" + pan;
	}
}
