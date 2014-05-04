package edu.insight.camerata.evaluation;

import java.io.File;
import java.io.IOException;

import org.jfugue.Note;
import org.jfugue.Pattern;
import org.jfugue.PatternTransformer;
import org.jfugue.Player;
import org.jfugue.extras.ReversePatternTransformer;

/**
 * This class plays Johann Sebastian Bach's "Crab Canon".  This is an interesting song
 * in which there are two voices, and each voice mirrors the other.  If the first voice
 * is playing D A F E, the other voice is playing E F A D.
 *
 * <p>
 * This example illustrates how to use a PatternTransformer - specifically, the
 * ReversePatternTransformer, which is supplied in JFugue.jar.  The notes for one voice
 * are entered, then that pattern is duplicated and transformed.  Finally, the two patterns
 * are added together into one pattern, which plays Bach's "Crab Canon".
 * </p>
 *
 * <p>
 * To compile: <code>javac -classpath <i>directory-and-filename-of-jfugue.jar</i> CrabCanon.java</code><br />
 * To execute: <code>java -classpath <i>directory-and-filename-of-jfugue.jar</i> org.jfugue.examples.CrabCanon<br />
 * (note: Depending on your version of Java, you may have to use "-cp" instead of "-classpath" when executing)<br />
 * </p>
 *
 * @see org.jfugue.PatternTransformer
 * @see org.jfugue.extras.ReversePatternTransformer
 * @author David Koelle
 * @version 2.0
 */
public class CrabCanon
{
    public static void main(String[] args)
    {
        CrabCanon crab = new CrabCanon();
        Pattern pattern = crab.getPattern();

        Player player = new Player();
        player.play(pattern);

        try {
            player.saveMidi(pattern, new File("crabcanon.mid"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.close();
    }

    public Pattern getPattern()
    {
        Pattern canon = new Pattern("D5h E5h A5h Bb5h C#5h Rq A5q "+
                       "A5q Ab5h G5q G5q F#5h F5q F5q E5q Eb5q D5q "+
                       "C#5q A3q D5q G5q F5h E5h D5h F5h "+
                       "A5i G5i A5i D6i A5i F5i E5i F5i G5i A5i B5i C#6i "+
                       "D6i F5i G5i A5i Bb5i E5i F5i G5i A5i G5i F5i E5i "+
                       "F5i G5i A5i Bb5i C6i Bb5i A5i G5i A5i Bb5i C6i D6i "+
                       "Eb6i C6i Bb5i A5i B5i C#6i D6i E6i F6i D6i C#6i B5i "+
                       "C#6i D6i E6i F6i G6i E6i A5i E6i D6i E6i F6i G6i "+
                       "F6i E6i D6i C#6i D6q A5q F5q D5q");

        // Reverse the canon
        ReversePatternTransformer rpt = new ReversePatternTransformer();
        Pattern reverseCanon = rpt.transform(canon);

        // Lower the octaves of the reversed pattern
        PatternTransformer octaveTransformer = new PatternTransformer() {
            public void noteEvent(Note note)
            {
                byte currentValue = note.getValue();
                note.setValue((byte)(currentValue - 12));
                getReturnPattern().addElement(note);
            }
        };
        Pattern octaveCanon = octaveTransformer.transform(reverseCanon);

        // Combine the two parts
        Pattern pattern = new Pattern();
        pattern.add("T90 V0 " + canon.getMusicString());
        pattern.add("V1 " + octaveCanon.getMusicString());

        return pattern;
    }
}

